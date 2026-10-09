package ch17_algorithms.drills.r07_interview.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Les tests de reference du drill 7 : les exemples, les cas pieges, et la vitesse. */
class Recall07Test {

    @Test
    void d01() {
        assertEquals(5, Recall07.maxProfit(new int[]{7, 1, 5, 3, 6, 4}));
        assertEquals(0, Recall07.maxProfit(new int[]{7, 6, 4, 3, 1}));
        assertEquals(0, Recall07.maxProfit(new int[0]));
        int[] big = new Random(1).ints(1_000_000, 1, 1000).toArray();
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> Recall07.maxProfit(big));
    }

    @Test
    void d02() {
        assertEquals(6, Recall07.maxSubarray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
        assertEquals(-1, Recall07.maxSubarray(new int[]{-3, -1, -2}));
        assertEquals(4_000_000_000L, Recall07.maxSubarray(new int[]{2_000_000_000, 2_000_000_000}));
    }

    static final int[] ROTATED = {4, 5, 6, 7, 0, 1, 2};

    @ParameterizedTest
    @ValueSource(ints = {4, 7, 0, 2, 5, 1})
    void d03(int target) {
        int got = Recall07.searchRotated(ROTATED, target);
        assertEquals(target, ROTATED[got]);
        assertEquals(-1, Recall07.searchRotated(ROTATED, 3));
    }

    @Test
    void d04() {
        char[][] grid = {
                "11000".toCharArray(),
                "11000".toCharArray(),
                "00100".toCharArray(),
                "00011".toCharArray()};
        assertEquals(3, Recall07.islands(grid));
        assertEquals(1, Recall07.islands(new char[][]{"111".toCharArray(), "101".toCharArray(), "111".toCharArray()}));
        char[][] land = new char[1000][1000];
        for (char[] row : land) {
            java.util.Arrays.fill(row, '1');
        }
        assertEquals(1, assertTimeoutPreemptively(Duration.ofSeconds(3), () -> Recall07.islands(land)));
    }

    @Test
    void d05() {
        assertTrue(Recall07.wordBreak("javalinux", List.of("java", "linux")));
        assertTrue(Recall07.wordBreak("pommepomme", List.of("pomme")));
        assertFalse(Recall07.wordBreak("catsandog", List.of("cats", "dog", "sand", "and", "cat")));
        assertFalse(assertTimeoutPreemptively(Duration.ofSeconds(2),
                () -> Recall07.wordBreak("a".repeat(500) + "b", List.of("a", "aa", "aaa", "aaaa"))));
    }

    @Test
    void d06() {
        assertArrayEquals(new long[]{24, 12, 8, 6}, Recall07.productExceptSelf(new int[]{1, 2, 3, 4}));
        assertArrayEquals(new long[]{0, 0, 9, 0, 0}, Recall07.productExceptSelf(new int[]{-1, 1, 0, -3, 3}));
    }

    @Test
    void d07() {
        int[][] times = {{1, 0, 1}, {1, 2, 1}, {2, 3, 1}};
        assertEquals(2, Recall07.networkDelay(4, times, 1));
        assertEquals(-1, Recall07.networkDelay(4, times, 0));
        assertEquals(0, Recall07.networkDelay(1, new int[0][], 0));
    }
}
