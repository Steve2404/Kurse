package ch17_algorithms.projects.p01_search.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Les tests de reference du projet 1 : les cas limites de chaque dichotomie, puis la vitesse. */
class SearchTest {

    static final int[] SORTED = {2, 4, 4, 4, 7, 9, 12};

    @Test
    void linearFindsTheFirstOccurrenceOrMinusOne() {
        assertAll(
                () -> assertEquals(1, Search.linear(SORTED, 4)),
                () -> assertEquals(6, Search.linear(SORTED, 12)),
                () -> assertEquals(-1, Search.linear(SORTED, 5)),
                () -> assertEquals(-1, Search.linear(new int[0], 5)));
    }

    // Pourquoi comparer a Arrays.binarySearch : c'est le contrat a respecter, y compris le point d'insertion.
    @ParameterizedTest(name = "binary({0})")
    @CsvSource({"2", "7", "9", "12", "1", "3", "5", "13", "4"})
    void binaryHasTheContractOfArraysBinarySearch(int key) {
        int got = Search.binary(SORTED, key);
        if (key == 4) {
            assertTrue(got >= 1 && got <= 3, "un des 4 : " + got);
        } else {
            assertEquals(Arrays.binarySearch(SORTED, key), got);
        }
    }

    @Test
    void binaryOnEmptyAndSingleArrays() {
        assertAll(
                () -> assertEquals(-1, Search.binary(new int[0], 5)),
                () -> assertEquals(0, Search.binary(new int[]{5}, 5)),
                () -> assertEquals(-2, Search.binary(new int[]{5}, 8)));
    }

    @ParameterizedTest(name = "bornes de {0} : [{1}, {2}[")
    @CsvSource({"4, 1, 4", "1, 0, 0", "2, 0, 1", "5, 4, 4", "12, 6, 7", "13, 7, 7"})
    void lowerAndUpperBounds(int key, int lower, int upper) {
        assertAll(
                () -> assertEquals(lower, Search.lowerBound(SORTED, key)),
                () -> assertEquals(upper, Search.upperBound(SORTED, key)),
                () -> assertEquals(upper - lower, Search.count(SORTED, key)));
    }

    @Test
    void firstBadVersionUsesFewCallsEvenForHugeN() {
        AtomicInteger calls = new AtomicInteger();
        // Pourquoi un delai : avec (lo + hi) / 2, le milieu deborde et la boucle ne s'arrete jamais.
        int first = assertTimeoutPreemptively(Duration.ofSeconds(2), () -> Search.firstBad(Integer.MAX_VALUE, v -> {
            calls.incrementAndGet();
            return v >= 2_000_000_000;
        }));
        assertAll(
                () -> assertEquals(2_000_000_000, first),
                () -> assertTrue(calls.get() <= 32, calls.get() + " appels"));
    }

    @Test
    void firstBadEdgeCases() {
        assertAll(
                () -> assertEquals(1, Search.firstBad(10, v -> true)),
                () -> assertEquals(10, Search.firstBad(10, v -> v == 10)),
                () -> assertEquals(-1, Search.firstBad(10, v -> false)));
    }

    @ParameterizedTest(name = "isqrt({0}) = {1}")
    @CsvSource({"0, 0", "1, 1", "3, 1", "4, 2", "15, 3", "16, 4", "1000000, 1000", "9223372036854775807, 3037000499"})
    void integerSquareRoot(long n, long root) {
        assertEquals(root, assertTimeoutPreemptively(Duration.ofSeconds(2), () -> Search.isqrt(n)));
    }

    @Test
    void negativeSquareRootIsRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Search.isqrt(-1));
        assertEquals("nombre negatif : -1", e.getMessage());
    }

    @ParameterizedTest(name = "en {0} jour(s) : {1}")
    @CsvSource({"1, 55", "2, 28", "5, 15", "10, 10", "20, 10"})
    void minimalTruckCapacity(int days, int capacity) {
        int[] weights = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        assertEquals(capacity, Search.minCapacity(weights, days));
    }

    @Test
    void impossibleDeliveriesAreRejected() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> Search.minCapacity(new int[0], 3)),
                () -> assertThrows(IllegalArgumentException.class, () -> Search.minCapacity(new int[]{4}, 0)));
    }

    // La vitesse : un million de recherches dans un million de nombres. O(log n) : quelques dixiemes de seconde.
    // Une recherche lineaire ferait mille milliards de comparaisons.
    @Test
    void binarySearchIsLogarithmic() {
        int n = 1_000_000;
        int[] big = new int[n];
        for (int i = 0; i < n; i++) {
            big[i] = 2 * i;
        }
        Random random = new Random(17);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            for (int q = 0; q < n; q++) {
                int key = random.nextInt(2 * n);
                int got = Search.binary(big, key);
                if (key % 2 == 0 ? got != key / 2 : got >= 0) {
                    throw new AssertionError("binary(" + key + ") = " + got);
                }
            }
            assertEquals(1, Search.count(big, 0));
        });
    }

    @Test
    void minCapacityIsFastOnBigInputs() {
        int[] weights = new int[100_000];
        Arrays.fill(weights, 500);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(50_000, Search.minCapacity(weights, 1000)));
    }
}
