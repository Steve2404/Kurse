package ch17_algorithms.drills.r06_dynamic.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/** Les tests de reference du drill 6. */
class Recall06Test {

    @ParameterizedTest
    @CsvSource({"0, 0", "1, 1", "10, 55", "92, 7540113804746346429"})
    void d01(int n, long expected) {
        assertEquals(expected, assertTimeoutPreemptively(Duration.ofSeconds(1), () -> Recall06.fibonacci(n)));
    }

    @Test
    void d02() {
        assertEquals(2, Recall06.minCoins(new int[]{1, 3, 4}, 6));
        assertEquals(-1, Recall06.minCoins(new int[]{2}, 3));
        assertEquals(0, Recall06.minCoins(new int[]{5}, 0));
    }

    @Test
    void d03() {
        assertEquals(4, Recall06.countWays(new int[]{1, 2, 5}, 5));
        assertEquals(73_682, Recall06.countWays(new int[]{1, 2, 5, 10, 20, 50, 100, 200}, 200));
    }

    @Test
    void d04() {
        assertEquals(4, Recall06.lcsLength("ABCBDAB", "BDCABA"));
        assertEquals(0, Recall06.lcsLength("", "abc"));
    }

    @ParameterizedTest
    @CsvSource({"kitten, sitting, 3", "'', abc, 3", "java, java, 0"})
    void d05(String a, String b, int expected) {
        assertEquals(expected, Recall06.editDistance(a, b));
    }

    @Test
    void d06() {
        assertEquals(9, Recall06.knapsack(new int[]{1, 3, 4, 5}, new int[]{1, 4, 5, 7}, 7));
        assertEquals(3, Recall06.knapsack(new int[]{2}, new int[]{3}, 4));
    }

    @Test
    void d07() {
        assertEquals(4, Recall06.longestIncreasing(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));
        assertEquals(1, Recall06.longestIncreasing(new int[]{7, 7, 7}));
        int[] big = new Random(4).ints(1_000_000).toArray();
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> Recall06.longestIncreasing(big));
    }
}
