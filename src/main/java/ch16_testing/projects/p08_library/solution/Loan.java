package ch16_testing.projects.p08_library.solution;

import java.time.LocalDate;

/** Un pret en cours : le livre doit revenir au plus tard le jour due (inclus). */
public record Loan(String isbn, String memberId, LocalDate borrowed, LocalDate due) {
}
