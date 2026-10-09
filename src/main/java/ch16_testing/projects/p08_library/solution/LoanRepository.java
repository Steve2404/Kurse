package ch16_testing.projects.p08_library.solution;

import java.time.LocalDate;
import java.util.List;

/** Les prets EN COURS (un pret clos n'apparait plus dans les listes). */
public interface LoanRepository {

    List<Loan> activeLoansOfMember(String memberId);

    List<Loan> activeLoansOfBook(String isbn);

    void save(Loan loan);

    void close(Loan loan, LocalDate returned);
}
