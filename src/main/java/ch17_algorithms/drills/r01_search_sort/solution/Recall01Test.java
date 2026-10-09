package ch17_algorithms.drills.r01_search_sort.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/** Les tests de reference du drill 1 : un test par defi, avec ses cas pieges. */
class Recall01Test {

    static final int[] SORTED = {2, 4, 4, 4, 7, 9, 12};

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 7, 12, 13})
    void d01(int key) {
        assertEquals(Arrays.binarySearch(SORTED, key), Recall01.binary(SORTED, key));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 4, 5, 13})
    void d02(int key) {
        int expected = 0;
        while (expected < SORTED.length && SORTED[expected] < key) {
            expected++;
        }
        assertEquals(expected, Recall01.lowerBound(SORTED, key));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 4, 12, 13})
    void d03(int key) {
        int expected = 0;
        while (expected < SORTED.length && SORTED[expected] <= key) {
            expected++;
        }
        assertEquals(expected, Recall01.upperBound(SORTED, key));
    }

    static void checkSort(java.util.function.Consumer<int[]> sorter) {
        for (int[] original : new int[][]{{}, {5}, {3, 1, 2}, {4, 4, 4}, {9, -1, 0, 9, -7, 3}, {1, 2, 3, 4}}) {
            int[] expected = original.clone();
            Arrays.sort(expected);
            int[] a = original.clone();
            sorter.accept(a);
            assertArrayEquals(expected, a);
        }
    }

    @Test
    void d04() {
        checkSort(Recall01::insertionSort);
    }

    @Test
    void d05() {
        checkSort(Recall01::mergeSort);
        int[] big = new Random(1).ints(500_000).toArray();
        int[] expected = big.clone();
        Arrays.sort(expected);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> Recall01.mergeSort(big));
        assertArrayEquals(expected, big);
    }

    @Test
    void d06() {
        checkSort(Recall01::quickSort);
        int[] dup = new Random(2).ints(500_000, 0, 3).toArray();
        int[] expected = dup.clone();
        Arrays.sort(expected);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> Recall01.quickSort(dup));
        assertArrayEquals(expected, dup);
    }

    @Test
    void d07() {
        int[] w = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        assertEquals(15, Recall01.minCapacity(w, 5));
        assertEquals(55, Recall01.minCapacity(w, 1));
        assertEquals(10, Recall01.minCapacity(w, 20));
    }
}
