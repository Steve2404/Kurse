package ch17_algorithms.projects.p06_backtracking.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Les tests de reference du projet 6. */
class BacktrackingTest {

    @ParameterizedTest(name = "{0}^{1} = {2}")
    @CsvSource({"2, 10, 1024", "3, 0, 1", "-2, 5, -32", "7, 1, 7", "2, 62, 4611686018427387904", "1, 1000000000, 1"})
    void fastPower(long base, int exp, long expected) {
        assertEquals(expected, assertTimeoutPreemptively(Duration.ofSeconds(1), () -> Backtracking.power(base, exp)));
    }

    @Test
    void negativeExponentIsRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Backtracking.power(2, -1));
        assertEquals("exposant negatif : -1", e.getMessage());
    }

    @Test
    void permutationsInChoiceOrder() {
        assertAll(
                () -> assertEquals(List.of(List.of(1, 2, 3), List.of(1, 3, 2), List.of(2, 1, 3), List.of(2, 3, 1), List.of(3, 1, 2), List.of(3, 2, 1)),
                        Backtracking.permutations(List.of(1, 2, 3))),
                () -> assertEquals(List.of(List.of()), Backtracking.permutations(List.of())),
                () -> assertEquals(5040, Backtracking.permutations(List.of(1, 2, 3, 4, 5, 6, 7)).size()));
    }

    @Test
    void subsetsWithoutThenWith() {
        assertAll(
                () -> assertEquals(List.of(List.of(), List.of(3), List.of(2), List.of(2, 3), List.of(1), List.of(1, 3), List.of(1, 2), List.of(1, 2, 3)),
                        Backtracking.subsets(List.of(1, 2, 3))),
                () -> assertEquals(List.of(List.of()), Backtracking.subsets(List.of())),
                () -> assertEquals(1024, new HashSet<>(Backtracking.subsets(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))).size()));
    }

    @Test
    void combinationsReachingATarget() {
        assertAll(
                () -> assertEquals(List.of(List.of(2, 2, 3), List.of(7)), Backtracking.combinationSum(new int[]{2, 3, 6, 7}, 7)),
                () -> assertEquals(List.of(List.of(2, 2, 2, 2), List.of(2, 3, 3), List.of(3, 5)), Backtracking.combinationSum(new int[]{5, 3, 2}, 8)),
                () -> assertEquals(List.of(), Backtracking.combinationSum(new int[]{2}, 3)),
                () -> assertEquals(List.of(List.of()), Backtracking.combinationSum(new int[]{2}, 0)));
    }

    @Test
    void wellFormedParentheses() {
        assertAll(
                () -> assertEquals(List.of("((()))", "(()())", "(())()", "()(())", "()()()"), Backtracking.parentheses(3)),
                () -> assertEquals(List.of("()"), Backtracking.parentheses(1)),
                () -> assertEquals(14, Backtracking.parentheses(4).size()),
                () -> assertEquals(List.of(""), Backtracking.parentheses(0)));
    }

    @ParameterizedTest(name = "{0} reines : {1} solutions")
    @CsvSource({"1, 1", "2, 0", "3, 0", "4, 2", "6, 4", "8, 92", "10, 724"})
    void nQueens(int n, int solutions) {
        assertEquals(solutions, Backtracking.nQueens(n));
    }

    @Test
    void nQueensPrunesEarly() {
        assertEquals(73_712, assertTimeoutPreemptively(Duration.ofSeconds(3), () -> Backtracking.nQueens(13)));
    }

    static int[][] grid(String s) {
        int[][] g = new int[9][9];
        for (int i = 0; i < 81; i++) {
            g[i / 9][i % 9] = s.charAt(i) == '.' ? 0 : s.charAt(i) - '0';
        }
        return g;
    }

    static String flat(int[][] g) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : g) {
            for (int v : row) {
                sb.append(v);
            }
        }
        return sb.toString();
    }

    @Test
    void solvesAClassicSudoku() {
        int[][] g = grid("53..7....6..195....98....6.8...6...34..8.3..17...2...6.6....28....419..5....8..79");
        assertTrue(Backtracking.solveSudoku(g));
        assertEquals("534678912672195348198342567859761423426853791713924856961537284287419635345286179", flat(g));
    }

    // La grille d'Arto Inkala (2012), presentee comme "la plus difficile du monde".
    @Test
    void solvesAHardSudokuQuickly() {
        int[][] g = grid("8..........36......7..9.2...5...7.......457.....1...3...1....68..85...1..9....4..");
        assertTrue(assertTimeoutPreemptively(Duration.ofSeconds(3), () -> Backtracking.solveSudoku(g)));
        assertEquals("812753649943682175675491283154237896369845721287169534521974368438526917796318452", flat(g));
    }

    @Test
    void rejectsAnInvalidStartingGrid() {
        int[][] twoFivesOnARow = grid("55..7....6..195....98....6.8...6...34..8.3..17...2...6.6....28....419..5....8..79");
        int[][] twoFivesInABox = grid("53..7....6..195....98....6.8...6...34..8.3..17...2...6.6....28....419..5....8..79");
        twoFivesInABox[1][1] = 5;
        // Piege : presque vide, cette grille se "complete" tres bien si l'on oublie de verifier les chiffres de depart.
        int[][] almostEmpty = new int[9][9];
        almostEmpty[0][0] = 5;
        almostEmpty[0][8] = 5;
        assertAll(
                () -> assertFalse(Backtracking.solveSudoku(twoFivesOnARow)),
                () -> assertFalse(Backtracking.solveSudoku(twoFivesInABox)),
                () -> assertFalse(assertTimeoutPreemptively(Duration.ofSeconds(2), () -> Backtracking.solveSudoku(almostEmpty))));
    }
}
