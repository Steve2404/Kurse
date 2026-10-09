package ch17_algorithms.drills.r03_stacks_heaps_trees.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Les tests de reference du drill 3. */
class Recall03Test {

    @ParameterizedTest
    @ValueSource(strings = {"", "([]{})", "f(a[i])"})
    void d01(String s) {
        assertTrue(Recall03.balanced(s));
        assertFalse(Recall03.balanced(s + "("));
        assertFalse(Recall03.balanced("([)]" + s));
    }

    @ParameterizedTest
    @CsvSource({"3 4 +, 7", "8 2 -, 6", "2 3 4 * +, 14", "7 2 /, 3"})
    void d02(String expr, long expected) {
        assertEquals(expected, Recall03.evalRpn(expr));
    }

    @Test
    void d03() {
        assertArrayEquals(new int[]{1, 1, 4, 2, 1, 1, 0, 0}, Recall03.daysUntilWarmer(new int[]{73, 74, 75, 71, 69, 72, 76, 73}));
        assertArrayEquals(new int[]{0, 0}, Recall03.daysUntilWarmer(new int[]{5, 5}));
    }

    @Test
    void d04() {
        for (int[] original : new int[][]{{}, {1}, {3, 1, 2}, {5, 5, 1}, {9, -3, 7, 0, 9, 2}}) {
            int[] expected = original.clone();
            Arrays.sort(expected);
            int[] a = original.clone();
            Recall03.heapSort(a);
            assertArrayEquals(expected, a);
        }
        int[] big = new Random(3).ints(1_000_000).toArray();
        int[] expected = big.clone();
        Arrays.sort(expected);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> Recall03.heapSort(big));
        assertArrayEquals(expected, big);
    }

    @Test
    void d05() {
        assertArrayEquals(new int[]{9, 8, 7}, Recall03.topK(new int[]{5, 9, 1, 7, 3, 8, 2}, 3));
        assertArrayEquals(new int[]{3, 1}, Recall03.topK(new int[]{1, 3}, 5));
    }

    static final int[] KEYS = {50, 30, 70, 20, 40, 60, 80, 35, 45, 65};

    @Test
    void d06() {
        assertEquals(List.of(50, 30, 20, 40, 35, 45, 70, 60, 65, 80), Recall03.preOrder(KEYS));
        assertEquals(List.of(), Recall03.preOrder(new int[0]));
    }

    @Test
    void d07() {
        assertEquals(List.of(50, 30, 70, 20, 40, 60, 80, 35, 45, 65), Recall03.levelOrder(KEYS));
        assertEquals(List.of(1, 2, 3), Recall03.levelOrder(new int[]{1, 2, 2, 3}));
    }
}
