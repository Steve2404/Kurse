package ch19_final.projects.p08_atelier.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Le coeur du metier, sans base ni reseau : des microsecondes par test. */
class BoardTest {

    private final Board board = new Board(2);

    private static Task in(String column) {
        return new Task(7, "Pneu", column, 2, 0);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {"A faire | En cours", "En cours | Fini", "Fini | En cours", "En cours | A faire"})
    void oneColumnAtATimeIsAllowed(String from, String to) {
        assertDoesNotThrow(() -> board.checkMove(in(from), to, Map.of()));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "A faire  | Fini     | deplacement impossible : A faire -> Fini (une colonne a la fois)",
            "Fini     | A faire  | deplacement impossible : Fini -> A faire (une colonne a la fois)",
            "En cours | En cours | la tache 7 est deja dans En cours",
            "A faire  | Archive  | colonne inconnue : Archive",
            "A faire  | en cours | colonne inconnue : en cours"})
    void forbiddenMoves(String from, String to, String message) {
        assertEquals(message, assertThrows(IllegalArgumentException.class, () -> board.checkMove(in(from), to, Map.of())).getMessage());
    }

    @Test
    void wipLimitCountsTasksAlreadyInProgress() {
        assertDoesNotThrow(() -> board.checkMove(in("A faire"), "En cours", Map.of("En cours", 1)));
        ApiException e = assertThrows(ApiException.class, () -> board.checkMove(in("A faire"), "En cours", Map.of("En cours", 2)));
        assertEquals(409, e.status());
        assertEquals("limite atteinte : En cours (2 taches au plus)", e.getMessage());
        assertThrows(ApiException.class, () -> board.checkMove(in("Fini"), "En cours", Map.of("En cours", 3)));
    }

    @Test
    void wipLimitOnlyConcernsInProgress() {
        assertDoesNotThrow(() -> board.checkMove(in("En cours"), "Fini", Map.of("En cours", 2, "Fini", 99)));
        assertDoesNotThrow(() -> board.checkMove(in("En cours"), "A faire", Map.of("En cours", 2, "A faire", 99)));
    }

    @Test
    void newTasksStartInToDo() {
        assertDoesNotThrow(() -> board.checkNew(new NewTask("Pneu", "A faire", 1)));
        assertEquals("une nouvelle tache commence dans A faire, pas dans Fini",
                assertThrows(IllegalArgumentException.class, () -> board.checkNew(new NewTask("Pneu", "Fini", 1))).getMessage());
    }

    @Test
    void limitIsChecked() {
        assertEquals("limite En cours invalide : 0", assertThrows(IllegalArgumentException.class, () -> new Board(0)).getMessage());
        assertThrows(ApiException.class, () -> new Board(1).checkMove(in("A faire"), "En cours", Map.of("En cours", 1)));
    }
}
