package ch17_algorithms.projects.p10_dynamic.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/** Les tests de reference du projet 10. */
class DynamicTest {

    @ParameterizedTest(name = "fibonacci({0}) = {1}")
    @CsvSource({"0, 0", "1, 1", "2, 1", "10, 55", "50, 12586269025", "92, 7540113804746346429"})
    void fibonacci(int n, long expected) {
        assertEquals(expected, assertTimeoutPreemptively(Duration.ofSeconds(1), () -> Dynamic.fibonacci(n)));
    }

    @Test
    void negativeFibonacciIsRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Dynamic.fibonacci(-1));
        assertEquals("n negatif : -1", e.getMessage());
    }

    // Piege : le glouton (4 + 1 + 1) donne 3 pieces ; la bonne reponse est 3 + 3.
    @Test
    void fewestCoinsBeatsGreedy() {
        assertAll(
                () -> assertEquals(2, Dynamic.minCoins(new int[]{1, 3, 4}, 6)),
                () -> assertEquals(3, Dynamic.minCoins(new int[]{1, 2, 5}, 11)),
                () -> assertEquals(-1, Dynamic.minCoins(new int[]{2}, 3)),
                () -> assertEquals(0, Dynamic.minCoins(new int[]{5}, 0)));
    }

    @Test
    void waysToPayCountCombinationsNotOrders() {
        assertAll(
                () -> assertEquals(4, Dynamic.countWays(new int[]{1, 2, 5}, 5)),
                () -> assertEquals(0, Dynamic.countWays(new int[]{2}, 3)),
                () -> assertEquals(1, Dynamic.countWays(new int[]{2}, 0)),
                () -> assertEquals(73_682, Dynamic.countWays(new int[]{1, 2, 5, 10, 20, 50, 100, 200}, 200)));
    }

    @Test
    void longestCommonSubsequence() {
        assertAll(
                () -> assertEquals(4, Dynamic.lcsLength("ABCBDAB", "BDCABA")),
                () -> assertEquals("BCBA", Dynamic.lcs("ABCBDAB", "BDCABA")),
                () -> assertEquals("che", Dynamic.lcs("chien", "niche")),
                () -> assertEquals("", Dynamic.lcs("abc", "xyz")),
                () -> assertEquals(0, Dynamic.lcsLength("", "abc")));
    }

    @ParameterizedTest(name = "distance(\"{0}\", \"{1}\") = {2}")
    @CsvSource({"chien, chine, 2", "kitten, sitting, 3", "'', abc, 3", "abc, '', 3", "java, java, 0", "sunday, saturday, 3"})
    void editDistance(String a, String b, int expected) {
        assertEquals(expected, Dynamic.editDistance(a, b));
    }

    @Test
    void knapsackTakesEachItemAtMostOnce() {
        assertAll(
                () -> assertEquals(9, Dynamic.knapsack(new int[]{1, 3, 4, 5}, new int[]{1, 4, 5, 7}, 7)),
                () -> assertEquals(0, Dynamic.knapsack(new int[]{5}, new int[]{10}, 4)),
                () -> assertEquals(3, Dynamic.knapsack(new int[]{2, 2}, new int[]{3, 3}, 3)),
                () -> assertEquals(10, Dynamic.knapsack(new int[]{5}, new int[]{10}, 5)),
                () -> assertEquals(3, Dynamic.knapsack(new int[]{2}, new int[]{3}, 4)));
    }

    @Test
    void longestStrictlyIncreasingSubsequence() {
        assertAll(
                () -> assertEquals(4, Dynamic.longestIncreasing(new int[]{10, 9, 2, 5, 3, 7, 101, 18})),
                () -> assertEquals(1, Dynamic.longestIncreasing(new int[]{7, 7, 7})),
                () -> assertEquals(0, Dynamic.longestIncreasing(new int[0])),
                () -> assertEquals(4, Dynamic.longestIncreasing(new int[]{1, 2, 3, 4})),
                () -> assertEquals(1, Dynamic.longestIncreasing(new int[]{4, 3, 2, 1})));
    }

    @Test
    void pathsThroughAGrid() {
        boolean[][] center = new boolean[3][3];
        center[1][1] = true;
        boolean[][] start = new boolean[2][2];
        start[0][0] = true;
        assertAll(
                () -> assertEquals(6, Dynamic.gridPaths(new boolean[3][3])),
                () -> assertEquals(2, Dynamic.gridPaths(center)),
                () -> assertEquals(1, Dynamic.gridPaths(new boolean[1][1])),
                () -> assertEquals(0, Dynamic.gridPaths(start)),
                () -> assertEquals(2_333_606_220L, Dynamic.gridPaths(new boolean[18][18])));
    }

    @Test
    void dynamicProgrammingIsPolynomial() {
        Random r = new Random(13);
        StringBuilder a = new StringBuilder();
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < 3000; i++) {
            a.append((char) ('a' + r.nextInt(4)));
            b.append((char) ('a' + r.nextInt(4)));
        }
        int[] seq = r.ints(1_000_000).toArray();
        int[] weights = r.ints(200, 1, 1000).toArray();
        int[] values = r.ints(200, 1, 1000).toArray();
        assertTimeoutPreemptively(Duration.ofSeconds(4), () -> {
            assertEquals(1000, Dynamic.minCoins(new int[]{1, 5, 10, 25, 50, 100, 200}, 200_000));
            Dynamic.editDistance(a.toString(), b.toString());
            Dynamic.lcsLength(a.toString(), b.toString());
            Dynamic.longestIncreasing(seq);
            Dynamic.knapsack(weights, values, 50_000);
        });
    }
}
