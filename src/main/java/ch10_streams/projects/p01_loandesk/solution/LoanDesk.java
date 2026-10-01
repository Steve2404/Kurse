package ch10_streams.projects.p01_loandesk.solution;

import ch10_streams.projects.p01_loandesk.Data;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

/**
 * SOLUTION du projet 1 - une conception possible parmi d'autres : le
 * correcteur ne juge que la sortie et l'API utilisee, pas les noms.
 */
public class LoanDesk implements Catalog {

    static final int GRACE_DAYS = 3;
    static final double FEE_PER_DAY = 0.50;
    static final double FEE_CAP = 10.00;

    // TODO 4 : verifiee pour que le compilateur force le catch dans la boucle des commandes.
    static class Refusal extends Exception {
        private static final long serialVersionUID = 1L;

        Refusal(String reason) {
            super(reason);
        }
    }

    record Loan(String memberId, String isbn) {
    }

    record Return(Member member, Book book, int days, double fee) {
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

    // orElseThrow(Supplier) : l'exception n'est construite que si l'Optional est vide.
    Member member(String id) throws Refusal {
        return findMember(id).orElseThrow(() -> new Refusal("membre inconnu " + id));
    }

    Book book(String isbn) throws Refusal {
        return byIsbn(isbn).orElseThrow(() -> new Refusal("livre inconnu " + isbn));
    }

    // TODO 6
    void borrow(String memberId, String isbn) throws Refusal {
        Member m = member(memberId);
        Book b = book(isbn);
        // isPresent : on ne lit pas l'emprunt, on teste seulement son existence.
        if (findLoan(m, b).isPresent()) {
            throw new Refusal(m.name() + " a deja " + b.title());
        }
        if (stock.get(isbn) == 0) {
            Deque<String> queue = waitlists.computeIfAbsent(isbn, k -> new ArrayDeque<>());
            queue.addLast(m.id());
            System.out.println("ATTENTE : " + m.name() + " en position " + queue.size() + " pour " + b.title());
            return;
        }
        lend(m, b);
    }

    private void lend(Member m, Book b) {
        int left = stock.merge(b.isbn(), -1, Integer::sum);
        loans.add(new Loan(m.id(), b.isbn()));
        System.out.println("OK : " + m.name() + " emprunte " + b.title() + " (reste " + left + ")");
    }

    // TODO 7 : l'absence de penalite est une vraie absence (pas 0.0 magique) -> Optional vide.
    static Optional<Double> fee(int daysLate) {
        if (daysLate <= GRACE_DAYS) {
            return Optional.empty();
        }
        return Optional.of(Math.min((daysLate - GRACE_DAYS) * FEE_PER_DAY, FEE_CAP));
    }

    // TODO 8
    void giveBack(String memberId, String isbn, int daysLate) throws Refusal {
        Member m = member(memberId);
        Book b = book(isbn);
        Loan loan = findLoan(m, b).orElseThrow(() -> new Refusal(m.name() + " n'a pas " + b.title()));
        loans.remove(loan);
        stock.merge(isbn, 1, Integer::sum);
        Optional<Double> fee = fee(daysLate);
        returns.add(new Return(m, b, daysLate, fee.orElse(0.0)));
        System.out.println("RETOUR : " + m.name() + " rend " + b.title() + ", "
                + fee.map(f -> String.format(Locale.US, "penalite %.2f", f)).orElse("sans penalite"));

        // flatMap et pas map : findMember rend deja un Optional -> map donnerait Optional<Optional<Member>>.
        Optional.ofNullable(waitlists.get(isbn))
                .map(Deque::pollFirst)
                .flatMap(this::findMember)
                .ifPresent(next -> {
                    lend(next, b);
                    notifyMember(next, b.title() + " vous attend");
                });
    }

    // orElseGet : le texte "par courrier" n'est construit que si l'email manque (orElse le construirait toujours).
    private void notifyMember(Member m, String text) {
        System.out.println(m.email().map(e -> "AVIS " + e + " : " + text)
                .orElseGet(() -> "AVIS par courrier a " + m.name() + " : " + text));
    }

    // TODO 9 : les deux branches en un appel, sans exception ni if.
    void contact(String memberId) {
        findMember(memberId).ifPresentOrElse(
                m -> System.out.println("CONTACT " + m.name() + " : " + m.email().orElse("par courrier")),
                () -> System.out.println("REFUS : membre inconnu " + memberId));
    }

    // TODO 10 : map(Deque::size) sur une file absente reste vide -> orElse(0).
    void info(String query) throws Refusal {
        Book b = find(query).orElseThrow(() -> new Refusal("aucun livre pour " + query));
        int waiting = Optional.ofNullable(waitlists.get(b.isbn())).map(Deque::size).orElse(0);
        System.out.println("INFO " + b.isbn() + " " + b.title() + " (" + b.author() + ") : "
                + stock.get(b.isbn()) + " disponible(s), " + waiting + " en attente");
    }

    // TODO 11
    void report() {
        List<Double> fees = returns.stream().map(Return::fee).filter(f -> f > 0).toList();
        System.out.println(String.format(Locale.US, "BILAN : %d penalite(s), total %.2f",
                fees.size(), fees.stream().mapToDouble(Double::doubleValue).sum()));

        // OptionalInt n'a pas de map : il donne le nombre, le max sur les Return donne le membre.
        OptionalInt maxDays = returns.stream().mapToInt(Return::days).max();
        Optional<Return> worst = returns.stream().max(Comparator.comparingInt(Return::days));
        OptionalDouble average = returns.stream().mapToInt(Return::days).average();
        if (worst.isEmpty()) {
            System.out.println("BILAN : aucun retour");
        } else {
            // orElseThrow() sans argument = get() au nom honnete : il lance NoSuchElementException si vide.
            System.out.println(String.format(Locale.US, "BILAN : retard max %d jour(s) (%s), moyen %.1f jour(s)",
                    maxDays.getAsInt(), worst.orElseThrow().member().name(), average.orElse(0)));
        }

        // Optional::stream : 0 ou 1 element -> flatMap ne garde que les emails presents.
        List<String> reachable = members.values().stream().map(Member::email).flatMap(Optional::stream).toList();
        System.out.println("BILAN : joignables par email " + reachable);
        System.out.println("BILAN : " + loans.size() + " emprunt(s) en cours");
    }

    // TODO 12 : un catch par commande -> un refus n'arrete pas la suite.
    void execute(String command) {
        String[] p = command.split(" ");
        try {
            switch (p[0]) {
                case "EMPRUNT" -> borrow(p[1], p[2]);
                case "RETOUR" -> giveBack(p[1], p[2], Integer.parseInt(p[3]));
                case "CONTACT" -> contact(p[1]);
                // Le titre peut contenir des espaces : on prend tout apres "INFO ", pas p[1].
                case "INFO" -> info(command.substring("INFO ".length()));
                case "BILAN" -> report();
                default -> throw new Refusal("commande inconnue " + p[0]);
            }
        } catch (Refusal e) {
            System.out.println("REFUS : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        LoanDesk desk = new LoanDesk(Data.BOOKS, Data.MEMBERS);
        Data.COMMANDS.forEach(desk::execute);
    }
}
