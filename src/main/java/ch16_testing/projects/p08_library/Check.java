package ch16_testing.projects.p08_library;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 8, le capstone (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON LibraryService et TES tests, ou avec l'argument "solution".
 */
public class Check {

    /** Les bugs que tes tests doivent attraper (INDICES.md dit, replie, ce que change chacun). */
    static final List<Mutant> MUTANTS = List.of(
            new Mutant("LibraryService.java", "if (charged <= 0) {", "if (charged <= 1) {"),
            new Mutant("LibraryService.java", "premium ? daysLate - FREE_DAYS_PREMIUM : daysLate", "premium ? daysLate - 2 : daysLate"),
            new Mutant("LibraryService.java", "Math.min(charged * FINE_PER_DAY, FINE_CAP)", "charged * FINE_PER_DAY"),
            new Mutant("LibraryService.java", "static final long FINE_CAP = 1000;", "static final long FINE_CAP = 1020;"),
            new Mutant("LibraryService.java", "l.due().isBefore(today)", "!l.due().isAfter(today)"),
            new Mutant("LibraryService.java", "current.size() >= (", "current.size() > ("),
            new Mutant("LibraryService.java", "member.premium() ? MAX_LOANS_PREMIUM : MAX_LOANS", "MAX_LOANS"),
            new Mutant("LibraryService.java", "loans.activeLoansOfBook(isbn).size() >= copies", "loans.activeLoansOfBook(isbn).size() > copies"),
            new Mutant("LibraryService.java", "member.premium() ? LOAN_DAYS_PREMIUM : LOAN_DAYS", "LOAN_DAYS"),
            new Mutant("LibraryService.java", "loans.save(loan);", ""),
            new Mutant("LibraryService.java", "if (copies == 0) {", "if (copies < 0) {"),
            new Mutant("LibraryService.java", "loans.close(loan, today);", "loans.close(loan, loan.due());"),
            new Mutant("LibraryService.java", "if (amount > 0) {", "if (amount >= 0) {"),
            new Mutant("LibraryService.java", ".filter(l -> l.isbn().equals(isbn))", ""),
            new Mutant("LibraryService.java", "ChronoUnit.DAYS.between(loan.due(), today)", "ChronoUnit.DAYS.between(today, loan.due())"),
            new Mutant("LibraryService.java",
                    "if (current.stream().anyMatch(l -> l.due().isBefore(today))) {\n            throw new IllegalStateException(\"retard en cours : \" + memberId);\n        }\n        if (current.size() >= (member.premium() ? MAX_LOANS_PREMIUM : MAX_LOANS)) {\n            throw new IllegalStateException(\"limite atteinte : \" + memberId);\n        }",
                    "if (current.size() >= (member.premium() ? MAX_LOANS_PREMIUM : MAX_LOANS)) {\n            throw new IllegalStateException(\"limite atteinte : \" + memberId);\n        }\n        if (current.stream().anyMatch(l -> l.due().isBefore(today))) {\n            throw new IllegalStateException(\"retard en cours : \" + memberId);\n        }"));

    static final List<String> API_CODE = List.of(
            "record Member(String id, String name, boolean premium)", "record Loan(String isbn, String memberId, LocalDate borrowed, LocalDate due)",
            "interface Catalog", "interface LoanRepository", "interface MemberDirectory", "interface Mailer",
            "final class LibraryService",
            "LibraryService(Catalog catalog, LoanRepository loans, MemberDirectory members, Mailer mailer, Clock clock)",
            "static long fine(long daysLate, boolean premium)", "Loan borrow(String memberId, String isbn)",
            "long giveBack(String memberId, String isbn)", "LocalDate.now(clock)", "ChronoUnit.DAYS.between(",
            "!LocalDate.now()", "!LocalDateTime.now()", "!Instant.now()", "!double", "!float");

    static final List<String> API_TESTS = List.of(
            "@ExtendWith(MockitoExtension.class)", "@Mock", "Clock.fixed(", "@BeforeEach", "@Nested", "@DisplayName(",
            "@ParameterizedTest", "@CsvSource(", "when(", "verify(", "never()", "verifyNoInteractions(", "ArgumentCaptor",
            "assertThrows(", "assertAll(", "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 22, MUTANTS, API_CODE, API_TESTS);
    }
}
