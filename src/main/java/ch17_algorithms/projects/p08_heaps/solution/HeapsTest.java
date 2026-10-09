package ch17_algorithms.projects.p08_heaps.solution;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/** Les tests de reference des urgences, des k plus grands, de la fusion et de la mediane. */
class HeapsTest {

    @Test
    void theMostSevereFirstThenTheFirstArrived() {
        Emergency e = new Emergency();
        e.arrive(new Patient("Ana", 2, 1));
        e.arrive(new Patient("Bob", 5, 2));
        e.arrive(new Patient("Cid", 3, 3));
        e.arrive(new Patient("Dan", 5, 4));
        e.arrive(new Patient("Eve", 2, 0));
        assertAll(
                () -> assertEquals(5, e.waitingCount()),
                () -> assertEquals("Bob", e.next().name()),
                () -> assertEquals("Dan", e.next().name()),
                () -> assertEquals("Cid", e.next().name()),
                () -> assertEquals("Eve", e.next().name()),
                () -> assertEquals("Ana", e.next().name()),
                () -> assertEquals(0, e.waitingCount()));
    }

    @Test
    void nobodyWaiting() {
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> new Emergency().next());
        assertEquals("personne en attente", ex.getMessage());
    }

    @Test
    void topKLargest() {
        assertAll(
                () -> assertArrayEquals(new int[]{9, 8, 7}, Heaps.topK(new int[]{5, 9, 1, 7, 3, 8, 2}, 3)),
                () -> assertArrayEquals(new int[]{5, 5}, Heaps.topK(new int[]{5, 1, 5, 2}, 2)),
                () -> assertArrayEquals(new int[]{3, 1}, Heaps.topK(new int[]{1, 3}, 5)),
                () -> assertArrayEquals(new int[0], Heaps.topK(new int[]{1, 3}, 0)));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> Heaps.topK(new int[]{1}, -1));
        assertEquals("k negatif : -1", ex.getMessage());
    }

    @Test
    void mergeKSortedLists() {
        assertAll(
                () -> assertEquals(List.of(1, 1, 2, 3, 4, 4, 5, 6),
                        Heaps.mergeSorted(List.of(List.of(1, 4, 5), List.of(1, 3, 4), List.of(2, 6)))),
                () -> assertEquals(List.of(7), Heaps.mergeSorted(List.of(List.of(), List.of(7), List.of()))),
                () -> assertEquals(List.of(), Heaps.mergeSorted(List.of())));
    }

    @Test
    void runningMedian() {
        MedianFinder m = new MedianFinder();
        m.add(5);
        assertEquals(5.0, m.median());
        m.add(15);
        assertEquals(10.0, m.median());
        m.add(1);
        assertEquals(5.0, m.median());
        m.add(3);
        assertEquals(4.0, m.median());
        m.add(8);
        assertEquals(5.0, m.median());
        m.add(2);
        assertEquals(4.0, m.median());
    }

    @Test
    void medianOfHugeValuesDoesNotOverflow() {
        MedianFinder m = new MedianFinder();
        m.add(Integer.MAX_VALUE);
        m.add(Integer.MAX_VALUE - 2);
        assertEquals(Integer.MAX_VALUE - 1.0, m.median());
    }

    @Test
    void medianOfNothing() {
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> new MedianFinder().median());
        assertEquals("aucun nombre", ex.getMessage());
    }

    @Test
    void priorityQueuesAreFast() {
        int[] a = new Random(8).ints(1_000_000).toArray();
        List<List<Integer>> lists = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            List<Integer> l = new ArrayList<>();
            for (int j = 0; j < 1000; j++) {
                l.add(i + j * 1000);
            }
            lists.add(l);
        }
        assertTimeoutPreemptively(Duration.ofSeconds(4), () -> {
            assertEquals(10, Heaps.topK(a, 10).length);
            assertEquals(1_000_000, Heaps.mergeSorted(lists).size());
            MedianFinder m = new MedianFinder();
            for (int i = 0; i < 200_000; i++) {
                m.add(i);
                m.median();
            }
            assertEquals(99_999.5, m.median());
        });
    }
}
