package ch18_design.drills.r01_refactor.solution;

import ch18_design.drills.r01_refactor.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Les tests de reference du drill 1 : l'ancien et le nouveau prix, sur TOUTES les entrees possibles. */
class Recall01Test {

    // Le domaine est petit : on n'a pas besoin du hasard, on essaie TOUT (101 x 2 x 7 x 24 = 33 936 cas).
    @Test
    void d01() {
        for (int age = 0; age <= 100; age++) {
            for (boolean student : new boolean[]{false, true}) {
                for (String day : List.of("LUN", "MAR", "MER", "JEU", "VEN", "SAM", "DIM")) {
                    for (int hour = 0; hour <= 23; hour++) {
                        assertEquals(Data.legacyTicket(age, student, day, hour), Recall01.ticket(age, student, day, hour),
                                age + " " + student + " " + day + " " + hour);
                    }
                }
            }
        }
    }

    @ParameterizedTest
    @CsvSource({"-1, 10", "20, -1", "20, 24"})
    void d02(int age, int hour) {
        assertEquals("entree invalide",
                assertThrows(IllegalArgumentException.class, () -> Recall01.ticket(age, false, "LUN", hour)).getMessage());
    }
}
