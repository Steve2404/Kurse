package ch17_algorithms.drills.r04_backtracking.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/** Les tests de reference du drill 4. */
class Recall04Test {

    @ParameterizedTest
    @CsvSource({"2, 10, 1024", "3, 0, 1", "-2, 5, -32", "1, 1000000000, 1"})
    void d01(long base, int exp, long expected) {
        assertEquals(expected, assertTimeoutPreemptively(Duration.ofSeconds(1), () -> Recall04.power(base, exp)));
    }

    @Test
    void d02() {
        assertEquals(List.of(List.of(1, 2, 3), List.of(1, 3, 2), List.of(2, 1, 3), List.of(2, 3, 1), List.of(3, 1, 2), List.of(3, 2, 1)),
                Recall04.permutations(List.of(1, 2, 3)));
        assertEquals(List.of(List.of()), Recall04.permutations(List.of()));
    }

    @Test
    void d03() {
        assertEquals(List.of(List.of(), List.of(3), List.of(2), List.of(2, 3), List.of(1), List.of(1, 3), List.of(1, 2), List.of(1, 2, 3)),
                Recall04.subsets(List.of(1, 2, 3)));
    }

    @Test
    void d04() {
        assertEquals(List.of(List.of(2, 2, 3), List.of(7)), Recall04.combinationSum(new int[]{7, 6, 3, 2}, 7));
        assertEquals(List.of(), Recall04.combinationSum(new int[]{2}, 3));
    }

    @Test
    void d05() {
        assertEquals(List.of("((()))", "(()())", "(())()", "()(())", "()()()"), Recall04.parentheses(3));
        assertEquals(List.of(""), Recall04.parentheses(0));
    }

    @ParameterizedTest
    @CsvSource({"4, 2", "8, 92", "12, 14200"})
    void d06(int n, int expected) {
        assertEquals(expected, assertTimeoutPreemptively(Duration.ofSeconds(3), () -> Recall04.nQueens(n)));
    }
}
