package ch17_algorithms.projects.p11_delivery.solution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/**
 * Les tests de reference du capstone. La ville : 6 carrefours, rues a double sens (minutes) :
 * 0-1 (4), 0-2 (1), 2-1 (2), 1-3 (5), 2-3 (8), 3-4 (3), 4-0 (10), 4-5 (2) ; le carrefour 6 est isole.
 */
class DeliveryTest {

    private Delivery city;

    @BeforeEach
    void build() {
        city = new Delivery(7, new int[][]{
                {0, 1, 4}, {0, 2, 1}, {2, 1, 2}, {1, 3, 5}, {2, 3, 8}, {3, 4, 3}, {4, 0, 10}, {4, 5, 2}});
    }

    @Test
    void travelTimesUseTheFastestRoads() {
        long[][] t = city.travelTimes(new int[]{0, 1, 3});
        assertAll(
                () -> assertArrayEquals(new long[]{0, 3, 8}, t[0]),
                () -> assertArrayEquals(new long[]{3, 0, 5}, t[1]),
                () -> assertArrayEquals(new long[]{8, 5, 0}, t[2]),
                () -> assertEquals(Long.MAX_VALUE, city.travelTimes(new int[]{0, 6})[0][1]));
    }

    @Test
    void bestTourAndItsOrder() {
        assertAll(
                () -> assertEquals(6, city.bestTour(new int[]{1})),
                () -> assertEquals(List.of(1), city.bestOrder(new int[]{1})),
                () -> assertEquals(25, city.bestTour(new int[]{5, 1})),
                () -> assertEquals(List.of(1, 5), city.bestOrder(new int[]{5, 1})),
                () -> assertEquals(25, city.bestTour(new int[]{3, 5, 1, 4})),
                () -> assertEquals(List.of(1, 3, 4, 5), city.bestOrder(new int[]{3, 5, 1, 4})),
                () -> assertEquals(0, city.bestTour(new int[0])),
                () -> assertEquals(List.of(), city.bestOrder(new int[0])));
    }

    @Test
    void invalidStopsAreRejected() {
        IllegalArgumentException depot = assertThrows(IllegalArgumentException.class, () -> city.bestTour(new int[]{0}));
        IllegalArgumentException twice = assertThrows(IllegalArgumentException.class, () -> city.bestTour(new int[]{1, 1}));
        IllegalArgumentException outside = assertThrows(IllegalArgumentException.class, () -> city.bestTour(new int[]{9}));
        IllegalArgumentException tooMany = assertThrows(IllegalArgumentException.class,
                () -> new Delivery(20, new int[0][]).bestTour(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13}));
        assertAll(
                () -> assertEquals("arret invalide : 0", depot.getMessage()),
                () -> assertEquals("arret invalide : 1", twice.getMessage()),
                () -> assertEquals("arret invalide : 9", outside.getMessage()),
                () -> assertEquals("trop d'arrets : 13", tooMany.getMessage()));
    }

    @Test
    void unreachableStopMakesTheDeliveryImpossible() {
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> city.bestTour(new int[]{1, 6}));
        assertEquals("livraison impossible", e.getMessage());
        assertThrows(IllegalStateException.class, () -> city.bestOrder(new int[]{6}));
    }

    @Test
    void negativeTimesAreRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Delivery(2, new int[][]{{0, 1, -1}}));
        assertEquals("temps negatif : -1", e.getMessage());
    }

    // L'ORACLE : sur de petites villes au hasard, Held-Karp doit trouver le meme temps que l'essai de TOUS les ordres.
    @RepeatedTest(20)
    void heldKarpMatchesBruteForce(RepetitionInfo info) {
        Random r = new Random(info.getCurrentRepetition());
        int n = 9;
        List<int[]> streets = new ArrayList<>();
        for (int a = 0; a < n; a++) {
            for (int b = a + 1; b < n; b++) {
                if (r.nextInt(3) > 0) {
                    streets.add(new int[]{a, b, 1 + r.nextInt(20)});
                }
            }
        }
        for (int a = 0; a + 1 < n; a++) {
            streets.add(new int[]{a, a + 1, 50});
        }
        Delivery d = new Delivery(n, streets.toArray(new int[0][]));
        int[] stops = {1, 2, 3, 4, 5, 6};
        long[][] t = d.travelTimes(new int[]{0, 1, 2, 3, 4, 5, 6});
        long brute = bruteForce(t, new boolean[7], 0, 0, 0);
        assertEquals(brute, d.bestTour(stops));
        List<Integer> order = d.bestOrder(stops);
        long cost = t[0][order.get(0)];
        for (int i = 0; i + 1 < order.size(); i++) {
            cost += t[order.get(i)][order.get(i + 1)];
        }
        assertEquals(brute, cost + t[order.get(order.size() - 1)][0]);
    }

    private static long bruteForce(long[][] t, boolean[] used, int at, int visited, long cost) {
        if (visited == t.length - 1) {
            return cost + t[at][0];
        }
        long best = Long.MAX_VALUE;
        for (int next = 1; next < t.length; next++) {
            if (!used[next]) {
                used[next] = true;
                best = Math.min(best, bruteForce(t, used, next, visited + 1, cost + t[at][next]));
                used[next] = false;
            }
        }
        return best;
    }

    @Test
    void mostDeliveriesOnTime() {
        assertAll(
                () -> assertEquals(3, Delivery.maxOnTime(new int[]{100, 200, 1000, 2000}, new int[]{200, 1300, 1250, 3200})),
                () -> assertEquals(2, Delivery.maxOnTime(new int[]{1, 2}, new int[]{3, 2})),
                () -> assertEquals(0, Delivery.maxOnTime(new int[]{5}, new int[]{4})),
                () -> assertEquals(2, Delivery.maxOnTime(new int[]{3, 3, 3}, new int[]{3, 6, 6})),
                () -> assertEquals(3, Delivery.maxOnTime(new int[]{5, 1, 1, 3}, new int[]{5, 6, 6, 8})),
                () -> assertEquals(0, Delivery.maxOnTime(new int[0], new int[0])));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Delivery.maxOnTime(new int[1], new int[2]));
        assertEquals("tailles differentes", e.getMessage());
    }

    @Test
    void twelveStopsInABigCityAreFast() {
        int side = 100;
        List<int[]> streets = new ArrayList<>();
        Random r = new Random(21);
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                int v = i * side + j;
                if (j + 1 < side) {
                    streets.add(new int[]{v, v + 1, 1 + r.nextInt(9)});
                }
                if (i + 1 < side) {
                    streets.add(new int[]{v, v + side, 1 + r.nextInt(9)});
                }
            }
        }
        Delivery big = new Delivery(side * side, streets.toArray(new int[0][]));
        int[] stops = new int[12];
        for (int i = 0; i < 12; i++) {
            stops[i] = 1 + r.nextInt(side * side - 1);
        }
        stops = java.util.Arrays.stream(stops).distinct().toArray();
        int[] finalStops = stops;
        int[] durations = r.ints(200_000, 1, 100).toArray();
        int[] deadlines = r.ints(200_000, 1, 5_000_000).toArray();
        assertTimeoutPreemptively(Duration.ofSeconds(4), () -> {
            big.bestTour(finalStops);
            big.bestOrder(finalStops);
            Delivery.maxOnTime(durations, deadlines);
        });
    }
}
