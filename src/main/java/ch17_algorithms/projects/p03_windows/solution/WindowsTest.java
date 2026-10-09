package ch17_algorithms.projects.p03_windows.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/** Les tests de reference du projet 3 : les cas limites de chaque fenetre, les intervalles, puis la vitesse. */
class WindowsTest {

    static Interval iv(int s, int e) {
        return new Interval(s, e);
    }

    @Test
    void pairWithSumUsesBothEnds() {
        int[] sorted = {1, 3, 4, 6, 8, 11};
        assertAll(
                () -> assertArrayEquals(new int[]{1, 4}, Windows.pairWithSum(sorted, 11)),
                () -> assertArrayEquals(new int[]{0, 5}, Windows.pairWithSum(sorted, 12)),
                () -> assertArrayEquals(new int[0], Windows.pairWithSum(sorted, 2)),
                () -> assertArrayEquals(new int[0], Windows.pairWithSum(new int[]{5}, 10)),
                () -> assertArrayEquals(new int[]{0, 1}, Windows.pairWithSum(new int[]{5, 5}, 10)));
    }

    @Test
    void pairWithSumDoesNotOverflow() {
        assertArrayEquals(new int[0], Windows.pairWithSum(new int[]{Integer.MAX_VALUE - 1, Integer.MAX_VALUE}, -3));
    }

    @Test
    void removeDuplicatesKeepsOneOfEach() {
        int[] a = {1, 1, 2, 3, 3, 3, 7};
        int n = Windows.removeDuplicates(a);
        assertAll(
                () -> assertEquals(4, n),
                () -> assertArrayEquals(new int[]{1, 2, 3, 7}, Arrays.copyOf(a, n)),
                () -> assertEquals(0, Windows.removeDuplicates(new int[0])),
                () -> assertEquals(1, Windows.removeDuplicates(new int[]{4, 4, 4})));
    }

    @ParameterizedTest(name = "fenetre de {0} : {1}")
    @CsvSource({"1, 9", "2, 13", "3, 13", "4, 21", "7, 20"})
    void maxSumOfAFixedWindow(int k, long expected) {
        assertEquals(expected, Windows.maxSumOfK(new int[]{2, 9, -1, 5, 8, -6, 3}, k));
    }

    @Test
    void invalidWindowIsRejected() {
        IllegalArgumentException zero = assertThrows(IllegalArgumentException.class, () -> Windows.maxSumOfK(new int[]{1, 2}, 0));
        IllegalArgumentException big = assertThrows(IllegalArgumentException.class, () -> Windows.maxSumOfK(new int[]{1, 2}, 3));
        assertAll(
                () -> assertEquals("fenetre invalide : 0", zero.getMessage()),
                () -> assertEquals("fenetre invalide : 3", big.getMessage()));
    }

    @ParameterizedTest(name = "\"{0}\" : {1}")
    @CsvSource({"abcabcbb, 3", "bbbbb, 1", "pwwkew, 3", "abba, 2", "dvdf, 3", "'', 0", "abcdef, 6"})
    void longestRunWithoutRepetition(String s, int expected) {
        assertEquals(expected, Windows.longestUniqueRun(s));
    }

    @ParameterizedTest(name = "somme >= {0} : {1}")
    @CsvSource({"7, 2", "4, 1", "15, 6", "16, 0", "11, 5"})
    void shortestWindowReachingATarget(int target, int expected) {
        assertEquals(expected, Windows.shortestAtLeast(new int[]{2, 3, 1, 2, 4, 3}, target));
    }

    @Test
    void mergeOverlappingAndTouchingIntervals() {
        List<Interval> in = List.of(iv(8, 10), iv(1, 3), iv(2, 6), iv(15, 18), iv(10, 12), iv(16, 17));
        assertEquals(List.of(iv(1, 6), iv(8, 12), iv(15, 18)), Windows.merge(in));
        assertEquals(List.of(), Windows.merge(List.of()));
    }

    @Test
    void greedyKeepsTheMostMeetings() {
        List<Interval> in = List.of(iv(1, 4), iv(3, 5), iv(0, 6), iv(5, 7), iv(3, 9), iv(5, 9), iv(6, 10), iv(8, 11), iv(8, 12), iv(2, 14), iv(12, 16));
        assertEquals(4, Windows.maxNonOverlapping(in));
        assertEquals(2, Windows.maxNonOverlapping(List.of(iv(1, 2), iv(2, 3))));
    }

    @Test
    void roomsNeededAtTheBusiestMoment() {
        assertAll(
                () -> assertEquals(2, Windows.minRooms(List.of(iv(0, 30), iv(5, 10), iv(15, 20)))),
                () -> assertEquals(1, Windows.minRooms(List.of(iv(9, 10), iv(10, 11), iv(11, 12)))),
                () -> assertEquals(3, Windows.minRooms(List.of(iv(1, 5), iv(2, 6), iv(3, 7), iv(6, 8)))),
                () -> assertEquals(0, Windows.minRooms(List.of())));
    }

    @Test
    void anEmptyIntervalCannotExist() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Interval(5, 5));
        assertEquals("intervalle vide : [5, 5[", e.getMessage());
    }

    // ---- la vitesse : un million d'elements, O(n) ou O(n log n) ; O(n^2) echoue.

    @Test
    void windowsAreLinear() {
        int n = 1_000_000;
        Random r = new Random(9);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = 1 + r.nextInt(100);
        }
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < n; i++) {
            text.append((char) ('a' + r.nextInt(26)));
        }
        int[] sorted = new int[n];
        for (int i = 0; i < n; i++) {
            sorted[i] = 2 * i;
        }
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            Windows.maxSumOfK(a, n / 2);
            Windows.shortestAtLeast(a, 40_000_000);
            Windows.longestUniqueRun(text.toString());
            assertEquals(0, Windows.pairWithSum(sorted, 1).length);
        });
    }

    @Test
    void intervalsAreFast() {
        Random r = new Random(10);
        List<Interval> many = new ArrayList<>();
        for (int i = 0; i < 200_000; i++) {
            int s = r.nextInt(1_000_000);
            many.add(new Interval(s, s + 1 + r.nextInt(50)));
        }
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            Windows.merge(many);
            Windows.maxNonOverlapping(many);
            Windows.minRooms(many);
        });
    }
}
