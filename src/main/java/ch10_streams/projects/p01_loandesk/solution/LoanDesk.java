package ch10_streams.projects.p01_loandesk.solution;

import ch10_streams.projects.p01_loandesk.Data;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.BiFunction;

/**
 * SOLUTION du projet 1 - une conception possible parmi d'autres : le
 * correcteur ne juge que la sortie et l'API utilisee, pas les noms.
 */
public class LoanDesk implements Catalog {

    static final int GRACE_DAYS = 3;
    static final int FEE_CENTS_PER_DAY = 50;
    static final int FEE_CAP_CENTS = 1000;

    record Loan(String memberId, String isbn) {
    }

    // Les montants en centimes (int) : pas d'arrondi flottant, et un affichage exact sans Locale.
    record Return(Member member, Book book, int days, int feeCents) {
    }

    // TODO 5 : LinkedHashMap garde l'ordre de Data (la liste des emails du BILAN en depend).
    private final Map<String, Member> members = new LinkedHashMap<>();
    private final Map<String, Book> books = new LinkedHashMap<>();
    private final Map<String, Integer> stock = new HashMap<>();
    private final List<Loan> loans = new ArrayList<>();
    private final Map<String, Deque<String>> waitlists = new HashMap<>();
    private final List<Return> returns = new ArrayList<>();

    LoanDesk(List<String> bookLines, List<String> memberLines) {
        for (String line : bookLines) {
            Book b = Book.parse(line);
            books.put(b.isbn(), b);
            stock.put(b.isbn(), Integer.parseInt(line.split(";")[3]));
        }
        memberLines.stream().map(Member::parse).forEach(m -> members.put(m.id(), m));
    }

    @Override
    public Optional<Book> byIsbn(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    @Override
    public Optional<Book> byTitle(String title) {
        return books.values().stream().filter(b -> b.title().equalsIgnoreCase(title)).findFirst();
    }

    // Map.get rend null si la cle manque : ofNullable transforme ce null en Optional vide.
    Optional<Member> findMember(String id) {
        return Optional.ofNullable(members.get(id));
    }

    Optional<Loan> findLoan(Member m, Book b) {
        return loans.stream().filter(l -> l.memberId().equals(m.id()) && l.isbn().equals(b.isbn())).findFirst();
    }

    // orElseThrow(Supplier) pour un INVARIANT : chaque livre charge a un stock ; sinon c'est un bug
    // du programme (pas un refus metier) et on veut qu'il s'arrete net, avec un message clair.
    int available(Book b) {
        return Optional.ofNullable(stock.get(b.isbn()))
                .orElseThrow(() -> new IllegalStateException("stock absent pour " + b.isbn()));
    }

    static String money(int cents) {
        return String.format("%d.%02d", cents / 100, cents % 100);
    }

    // TODO 4 : un refus est une REPONSE, pas une erreur -> la chaine Optional rend le resultat OU "REFUS : ...".
    // Le traitement (une BiFunction, chapitre 8) n'est appele que si le membre ET le livre existent.
    String withMemberAndBook(String memberId, String isbn, BiFunction<Member, Book, String> action) {
        return findMember(memberId)
                .map(m -> byIsbn(isbn)
                        .map(b -> action.apply(m, b))
                        .orElse("REFUS : livre inconnu " + isbn))
                .orElse("REFUS : membre inconnu " + memberId);
    }

    // TODO 6
    String borrow(Member m, Book b) {
        // isPresent : on ne lit pas l'emprunt, on teste seulement son existence.
        if (findLoan(m, b).isPresent()) {
            return "REFUS : " + m.name() + " a deja " + b.title();
        }
        if (available(b) == 0) {
            Deque<String> queue = waitlists.computeIfAbsent(b.isbn(), k -> new ArrayDeque<>());
            queue.addLast(m.id());
            return "ATTENTE : " + m.name() + " en position " + queue.size() + " pour " + b.title();
        }
        return lend(m, b);
    }

    private String lend(Member m, Book b) {
        int left = stock.merge(b.isbn(), -1, Integer::sum);
        loans.add(new Loan(m.id(), b.isbn()));
        return "OK : " + m.name() + " emprunte " + b.title() + " (reste " + left + ")";
    }

    // TODO 7 : l'absence de penalite est une vraie absence (pas un 0 magique) -> Optional vide.
    static Optional<Integer> fee(int daysLate) {
        if (daysLate <= GRACE_DAYS) {
            return Optional.empty();
        }
        return Optional.of(Math.min((daysLate - GRACE_DAYS) * FEE_CENTS_PER_DAY, FEE_CAP_CENTS));
    }

    // TODO 8
    String giveBack(Member m, Book b, int daysLate) {
        return findLoan(m, b)
                .map(loan -> giveBack(m, b, loan, daysLate))
                .orElse("REFUS : " + m.name() + " n'a pas " + b.title());
    }

    private String giveBack(Member m, Book b, Loan loan, int daysLate) {
        List<String> lines = new ArrayList<>();
        loans.remove(loan);
        stock.merge(b.isbn(), 1, Integer::sum);
        Optional<Integer> fee = fee(daysLate);
        returns.add(new Return(m, b, daysLate, fee.orElse(0)));
        lines.add("RETOUR : " + m.name() + " rend " + b.title() + ", " + fee.map(c -> "penalite " + money(c)).orElse("sans penalite"));

        // flatMap et pas map : findMember rend deja un Optional -> map donnerait Optional<Optional<Member>>.
        Optional.ofNullable(waitlists.get(b.isbn()))
                .map(Deque::pollFirst)
                .flatMap(this::findMember)
                .ifPresent(next -> {
                    lines.add(lend(next, b));
                    lines.add(notice(next, b.title() + " vous attend"));
                });
        return String.join("\n", lines);
    }

    // orElseGet : le texte "par courrier" n'est construit que si l'email manque (orElse le construirait toujours).
    private String notice(Member m, String text) {
        return m.email().map(e -> "AVIS " + e + " : " + text)
                .orElseGet(() -> "AVIS par courrier a " + m.name() + " : " + text);
    }

    // TODO 9 : les deux branches en un appel, sans if.
    void contact(String memberId) {
        findMember(memberId).ifPresentOrElse(
                m -> System.out.println("CONTACT " + m.name() + " : " + m.email().orElse("par courrier")),
                () -> System.out.println("REFUS : membre inconnu " + memberId));
    }

    // TODO 10 : map(Deque::size) sur une file absente reste vide -> orElse(0).
    String info(String query) {
        return find(query)
                .map(b -> "INFO " + b.isbn() + " " + b.title() + " (" + b.author() + ") : " + available(b) + " disponible(s), "
                        + Optional.ofNullable(waitlists.get(b.isbn())).map(Deque::size).orElse(0) + " en attente")
                .orElse("REFUS : aucun livre pour " + query);
    }

    // TODO 11
    void report() {
        List<Integer> fees = returns.stream().map(Return::feeCents).filter(c -> c > 0).toList();
        System.out.println("BILAN : " + fees.size() + " penalite(s), total " + money(fees.stream().mapToInt(Integer::intValue).sum()));

        // OptionalInt n'a pas de map : il donne le nombre, le max sur les Return donne le membre.
        OptionalInt maxDays = returns.stream().mapToInt(Return::days).max();
        Optional<Return> worst = returns.stream().max(Comparator.comparingInt(Return::days));
        OptionalDouble average = returns.stream().mapToInt(Return::days).average();
        if (worst.isEmpty()) {
            System.out.println("BILAN : aucun retour");
        } else {
            // orElseThrow() sans argument = get() au nom honnete (NoSuchElementException si vide).
            // Math.round(x * 10) / 10.0 : une decimale ; Double.toString ecrit toujours un point.
            System.out.println("BILAN : retard max " + maxDays.getAsInt() + " jour(s) (" + worst.orElseThrow().member().name()
                    + "), moyen " + Math.round(average.orElse(0) * 10) / 10.0 + " jour(s)");
        }

        // Optional::stream : 0 ou 1 element -> flatMap ne garde que les emails presents.
        List<String> reachable = members.values().stream().map(Member::email).flatMap(Optional::stream).toList();
        System.out.println("BILAN : joignables par email " + reachable);
        System.out.println("BILAN : " + loans.size() + " emprunt(s) en cours");
    }

    // TODO 12 : chaque commande produit sa reponse ; un refus n'arrete pas les suivantes.
    void execute(String command) {
        String[] p = command.split(" ");
        switch (p[0]) {
            case "EMPRUNT" -> System.out.println(withMemberAndBook(p[1], p[2], this::borrow));
            case "RETOUR" -> System.out.println(withMemberAndBook(p[1], p[2], (m, b) -> giveBack(m, b, Integer.parseInt(p[3]))));
            case "CONTACT" -> contact(p[1]);
            // Le titre peut contenir des espaces : on prend tout apres "INFO ", pas p[1].
            case "INFO" -> System.out.println(info(command.substring("INFO ".length())));
            case "BILAN" -> report();
            default -> System.out.println("REFUS : commande inconnue " + p[0]);
        }
    }

    public static void main(String[] args) {
        LoanDesk desk = new LoanDesk(Data.BOOKS, Data.MEMBERS);
        Data.COMMANDS.forEach(desk::execute);
    }
}
