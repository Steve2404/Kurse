package ch17_algorithms.projects.p02_sorting.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/** Les tests de reference du projet 2 : les tableaux pieges, la stabilite, puis la vitesse. */
class SortingTest {

    // Les tableaux pieges : vide, une case, deja trie, a l'envers, que des doublons, des negatifs.
    static Stream<int[]> tricky() {
        return Stream.of(new int[0], new int[]{7}, new int[]{1, 2, 3, 4}, new int[]{9, 7, 5, 3, 1},
                new int[]{4, 4, 4, 4}, new int[]{3, -1, 0, -7, 3, 2}, new int[]{5, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5});
    }

    static Stream<Arguments> sorters() {
        return Stream.of(
                Arguments.of("insertion", (Consumer<int[]>) Sorting::insertionSort),
                Arguments.of("fusion", (Consumer<int[]>) Sorting::mergeSort),
                Arguments.of("rapide", (Consumer<int[]>) Sorting::quickSort));
    }

    @ParameterizedTest(name = "tri {0}")
    @MethodSource("sorters")
    void sortsEveryTrickyArray(String name, Consumer<int[]> sorter) {
        tricky().forEach(original -> {
            int[] expected = original.clone();
            Arrays.sort(expected);
            int[] a = original.clone();
            sorter.accept(a);
            assertArrayEquals(expected, a, name + " de " + Arrays.toString(original));
        });
    }

    @Test
    void countingSortSortsSmallValues() {
        int[] a = {3, 0, 2, 3, 1, 0, 5};
        Sorting.countingSort(a, 5);
        assertArrayEquals(new int[]{0, 0, 1, 2, 3, 3, 5}, a);
    }

    @Test
    void countingSortRejectsValuesOutOfRange() {
        IllegalArgumentException high = assertThrows(IllegalArgumentException.class, () -> Sorting.countingSort(new int[]{1, 6}, 5));
        IllegalArgumentException low = assertThrows(IllegalArgumentException.class, () -> Sorting.countingSort(new int[]{-1}, 5));
        assertAll(
                () -> assertEquals("valeur hors limites : 6", high.getMessage()),
                () -> assertEquals("valeur hors limites : -1", low.getMessage()));
    }

    @Test
    void genericMergeSortIsStable() {
        List<Runner> runners = List.of(
                new Runner("Ana", 9000), new Runner("Bob", 8500), new Runner("Cid", 9000),
                new Runner("Dan", 8500), new Runner("Eve", 7200));
        List<Runner> sorted = Sorting.mergeSort(runners, Comparator.comparingInt(Runner::seconds));
        // Pourquoi Bob avant Dan, et Ana avant Cid : a temps egal, l'ordre d'arrivee dans la liste est garde.
        assertEquals(List.of("Eve", "Bob", "Dan", "Ana", "Cid"), sorted.stream().map(Runner::name).toList());
    }

    @Test
    void genericMergeSortDoesNotTouchItsInput() {
        List<Runner> runners = List.of(new Runner("Bob", 2), new Runner("Ana", 1));
        assertEquals(List.of(new Runner("Ana", 1), new Runner("Bob", 2)), Sorting.mergeSort(runners, Comparator.comparingInt(Runner::seconds)));
        assertEquals(List.of(), Sorting.mergeSort(List.<Runner>of(), Comparator.comparingInt(Runner::seconds)));
    }

    // ---- la vitesse

    static int[] randomArray(int n, int bound, long seed) {
        Random r = new Random(seed);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = r.nextInt(bound);
        }
        return a;
    }

    static void assertSortedFast(Consumer<int[]> sorter, int[] a, int seconds) {
        int[] expected = a.clone();
        Arrays.sort(expected);
        assertTimeoutPreemptively(Duration.ofSeconds(seconds), () -> sorter.accept(a));
        assertArrayEquals(expected, a);
    }

    @Test
    void mergeSortOfAMillionRandomNumbers() {
        assertSortedFast(Sorting::mergeSort, randomArray(1_000_000, Integer.MAX_VALUE, 1), 3);
    }

    @Test
    void quickSortOfAMillionRandomNumbers() {
        assertSortedFast(Sorting::quickSort, randomArray(1_000_000, Integer.MAX_VALUE, 2), 3);
    }

    // Piege : avec le 1er element comme pivot, un tableau deja trie donne O(n^2).
    @Test
    void quickSortOfAnAlreadySortedArray() {
        int[] a = new int[1_000_000];
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }
        assertSortedFast(Sorting::quickSort, a, 3);
    }

    // Piege : avec une partition en deux, un tableau plein de doublons donne O(n^2).
    @Test
    void quickSortOfAMillionDuplicates() {
        assertSortedFast(Sorting::quickSort, randomArray(1_000_000, 3, 3), 3);
    }

    // Le tri par insertion est lent en general, mais tres rapide sur un tableau presque trie.
    @Test
    void insertionSortIsFastOnANearlySortedArray() {
        int[] a = new int[1_000_000];
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }
        Random r = new Random(4);
        for (int s = 0; s < 100; s++) {
            int i = r.nextInt(a.length - 1);
            int t = a[i];
            a[i] = a[i + 1];
            a[i + 1] = t;
        }
        assertSortedFast(Sorting::insertionSort, a, 2);
    }

    @Test
    void countingSortOfTenMillionSmallValues() {
        int[] a = randomArray(10_000_000, 101, 5);
        int[] expected = a.clone();
        Arrays.sort(expected);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> Sorting.countingSort(a, 100));
        assertArrayEquals(expected, a);
    }
}
