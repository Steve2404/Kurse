package ch17_algorithms.projects.p08_heaps.solution;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/** Les tests de reference du tas ecrit a la main. */
class MinHeapTest {

    @Test
    void pollAlwaysGivesTheSmallest() {
        MinHeap h = new MinHeap();
        for (int v : new int[]{5, 3, 8, 1, 9, 2, 7}) {
            h.add(v);
        }
        assertEquals(1, h.peek());
        assertEquals(7, h.size());
        int[] out = new int[7];
        for (int i = 0; i < 7; i++) {
            out[i] = h.poll();
        }
        assertArrayEquals(new int[]{1, 2, 3, 5, 7, 8, 9}, out);
    }

    @Test
    void duplicatesAndNegatives() {
        MinHeap h = new MinHeap();
        for (int v : new int[]{4, -1, 4, 0, -1}) {
            h.add(v);
        }
        assertAll(
                () -> assertEquals(-1, h.poll()),
                () -> assertEquals(-1, h.poll()),
                () -> assertEquals(0, h.poll()),
                () -> assertEquals(4, h.poll()),
                () -> assertEquals(4, h.poll()),
                () -> assertEquals(0, h.size()));
    }

    // Plus de 16 elements : le tableau doit grandir.
    @Test
    void theArrayGrows() {
        MinHeap h = new MinHeap();
        for (int v = 100; v > 0; v--) {
            h.add(v);
        }
        assertEquals(1, h.poll());
        assertEquals(99, h.size());
    }

    @Test
    void anEmptyHeapComplains() {
        MinHeap h = new MinHeap();
        NoSuchElementException e = assertThrows(NoSuchElementException.class, h::peek);
        assertEquals("tas vide", e.getMessage());
        assertThrows(NoSuchElementException.class, h::poll);
    }

    @Test
    void heapSortSorts() {
        assertAll(
                () -> assertArrayEquals(new int[]{-3, 0, 2, 2, 7}, MinHeap.heapSort(new int[]{2, 7, -3, 2, 0})),
                () -> assertArrayEquals(new int[0], MinHeap.heapSort(new int[0])));
    }

    @Test
    void aMillionOperationsInNLogN() {
        int[] a = new Random(5).ints(1_000_000).toArray();
        int[] expected = a.clone();
        Arrays.sort(expected);
        assertArrayEquals(expected, assertTimeoutPreemptively(Duration.ofSeconds(3), () -> MinHeap.heapSort(a)));
    }
}
