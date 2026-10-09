package ch16_testing.projects.p08_library.solution;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;

/**
 * Les prets de la mediatheque. Toutes les regles sont ici ; tout ce qui touche au monde exterieur
 * (catalogue, prets enregistres, adherents, courriels, date du jour) arrive par le constructeur.
 */
public final class LibraryService {

    static final int MAX_LOANS = 3;
    static final int MAX_LOANS_PREMIUM = 5;
    static final int LOAN_DAYS = 21;
    static final int LOAN_DAYS_PREMIUM = 28;
    static final long FINE_PER_DAY = 20;
    static final long FINE_CAP = 1000;
    static final int FREE_DAYS_PREMIUM = 3;

    private final Catalog catalog;
    private final LoanRepository loans;
    private final MemberDirectory members;
    private final Mailer mailer;
    private final Clock clock;

    public LibraryService(Catalog catalog, LoanRepository loans, MemberDirectory members, Mailer mailer, Clock clock) {
        this.catalog = catalog;
        this.loans = loans;
        this.members = members;
        this.mailer = mailer;
        this.clock = clock;
    }

    // Une fonction PURE (sans dependance) : la plus facile a tester, avec un tableau de cas.
    // Piege : le plafond s'applique APRES les jours offerts ; un retard de 0 jour ou negatif ne coute rien.
    public static long fine(long daysLate, boolean premium) {
        long charged = premium ? daysLate - FREE_DAYS_PREMIUM : daysLate;
        if (charged <= 0) {
            return 0;
        }
        return Math.min(charged * FINE_PER_DAY, FINE_CAP);
    }

    // L'ordre des refus compte : il dit a l'adherent la PREMIERE chose a regler.
    public Loan borrow(String memberId, String isbn) {
        Member member = members.find(memberId).orElseThrow(() -> new NoSuchElementException("membre inconnu : " + memberId));
        int copies = catalog.copies(isbn);
        if (copies == 0) {
            throw new NoSuchElementException("livre inconnu : " + isbn);
        }
        LocalDate today = LocalDate.now(clock);
        var current = loans.activeLoansOfMember(memberId);
        // Piege : un pret qui expire AUJOURD'HUI n'est pas encore en retard.
        if (current.stream().anyMatch(l -> l.due().isBefore(today))) {
            throw new IllegalStateException("retard en cours : " + memberId);
        }
        if (current.size() >= (member.premium() ? MAX_LOANS_PREMIUM : MAX_LOANS)) {
            throw new IllegalStateException("limite atteinte : " + memberId);
        }
        if (loans.activeLoansOfBook(isbn).size() >= copies) {
            throw new IllegalStateException("plus d'exemplaire : " + isbn);
        }
        Loan loan = new Loan(isbn, memberId, today, today.plusDays(member.premium() ? LOAN_DAYS_PREMIUM : LOAN_DAYS));
        loans.save(loan);
        return loan;
    }

    public long giveBack(String memberId, String isbn) {
        Member member = members.find(memberId).orElseThrow(() -> new NoSuchElementException("membre inconnu : " + memberId));
        Loan loan = loans.activeLoansOfMember(memberId).stream()
                .filter(l -> l.isbn().equals(isbn))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("pret introuvable : " + memberId + " / " + isbn));
        LocalDate today = LocalDate.now(clock);
        loans.close(loan, today);
        long amount = fine(ChronoUnit.DAYS.between(loan.due(), today), member.premium());
        if (amount > 0) {
            mailer.send(memberId, "Amende : " + amount + " centimes pour " + isbn);
        }
        return amount;
    }
}
