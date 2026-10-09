package ch17_algorithms.drills.r02_windows_hashing.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/** Les tests de reference du drill 2. */
class Recall02Test {

    @Test
    void d01() {
        int[] s = {1, 3, 4, 6, 8, 11};
        assertArrayEquals(new int[]{1, 4}, Recall02.pairWithSum(s, 11));
        assertArrayEquals(new int[0], Recall02.pairWithSum(s, 2));
        assertArrayEquals(new int[0], Recall02.pairWithSum(new int[]{Integer.MAX_VALUE - 1, Integer.MAX_VALUE}, -3));
    }

    @ParameterizedTest
    @CsvSource({"1, 9", "3, 13", "4, 21", "7, 20"})
    void d02(int k, long expected) {
        assertEquals(expected, Recall02.maxSumOfK(new int[]{2, 9, -1, 5, 8, -6, 3}, k));
    }

    @ParameterizedTest
    @CsvSource({"abcabcbb, 3", "bbbbb, 1", "abba, 2", "'', 0"})
    void d03(String s, int expected) {
        assertEquals(expected, Recall02.longestUniqueRun(s));
    }

    @ParameterizedTest
    @CsvSource({"7, 2", "15, 6", "16, 0"})
    void d04(int target, int expected) {
        assertEquals(expected, Recall02.shortestAtLeast(new int[]{2, 3, 1, 2, 4, 3}, target));
    }

    @Test
    void d05() {
        assertArrayEquals(new int[]{1, 2}, Recall02.twoSum(new int[]{3, 2, 4}, 6));
        assertArrayEquals(new int[]{0, 2}, Recall02.twoSum(new int[]{5, 5, 1}, 6));
        assertArrayEquals(new int[0], Recall02.twoSum(new int[]{1, 2}, 9));
        int[] big = new int[1_000_000];
        for (int i = 0; i < big.length; i++) {
            big[i] = 2 * i;
        }
        assertArrayEquals(new int[0], assertTimeoutPreemptively(Duration.ofSeconds(3), () -> Recall02.twoSum(big, 1)));
    }

    @Test
    void d06() {
        assertEquals(4, Recall02.longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}));
        assertEquals(0, Recall02.longestConsecutive(new int[0]));
        int[] down = new int[1_000_000];
        for (int i = 0; i < down.length; i++) {
            down[i] = down.length - i;
        }
        assertEquals(1_000_000, assertTimeoutPreemptively(Duration.ofSeconds(3), () -> Recall02.longestConsecutive(down)));
    }

    @Test
    void d07() {
        int[][] in = {{8, 10}, {1, 3}, {2, 6}, {15, 18}, {10, 12}};
        assertArrayEquals(new int[][]{{1, 6}, {8, 12}, {15, 18}}, Recall02.mergeIntervals(in));
        assertArrayEquals(new int[][]{{8, 10}, {1, 3}, {2, 6}, {15, 18}, {10, 12}}, in);
    }
}
