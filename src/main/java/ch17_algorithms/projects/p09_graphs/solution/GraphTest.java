package ch17_algorithms.projects.p09_graphs.solution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Les tests de reference du graphe, sur un metro NON oriente de 8 stations (temps en minutes).
 * Aretes, dans cet ordre : 0-1 (2), 1-2 (2), 2-3 (2), 0-4 (5), 4-3 (1), 1-5 (7), 5-3 (1), 6-7 (1).
 * Les stations 6 et 7 forment une ligne isolee.
 */
class GraphTest {

    private Graph metro;

    @BeforeEach
    void build() {
        metro = new Graph(8);
        metro.addUndirected(0, 1, 2);
        metro.addUndirected(1, 2, 2);
        metro.addUndirected(2, 3, 2);
        metro.addUndirected(0, 4, 5);
        metro.addUndirected(4, 3, 1);
        metro.addUndirected(1, 5, 7);
        metro.addUndirected(5, 3, 1);
        metro.addUndirected(6, 7, 1);
    }

    @Test
    void breadthFirstOrder() {
        assertAll(
                () -> assertEquals(List.of(0, 1, 4, 2, 5, 3), metro.bfsOrder(0)),
                () -> assertEquals(List.of(6, 7), metro.bfsOrder(6)));
    }

    @Test
    void fewestEdgesFromAStation() {
        assertArrayEquals(new int[]{0, 1, 2, 2, 1, 2, -1, -1}, metro.hops(0));
    }

    @Test
    void pathWithTheFewestStops() {
        assertAll(
                () -> assertEquals(List.of(0, 4, 3), metro.fewestStops(0, 3)),
                () -> assertEquals(List.of(0), metro.fewestStops(0, 0)),
                () -> assertEquals(List.of(), metro.fewestStops(0, 7)),
                () -> assertEquals(List.of(2, 1, 5), metro.fewestStops(2, 5)));
    }

    @Test
    void componentsOfTheNetwork() {
        assertEquals(2, metro.countComponents());
        assertEquals(5, new Graph(5).countComponents());
    }

    @Test
    void dijkstraFindsTheFastestTimes() {
        long[] d = metro.dijkstra(0);
        assertAll(
                () -> assertEquals(0, d[0]),
                () -> assertEquals(2, d[1]),
                () -> assertEquals(4, d[2]),
                () -> assertEquals(6, d[3]),
                () -> assertEquals(5, d[4]),
                () -> assertEquals(7, d[5]),
                () -> assertEquals(Long.MAX_VALUE, d[6]));
    }

    // Piege : le chemin le plus court en minutes n'est pas celui qui a le moins d'aretes.
    @Test
    void fastestIsNotFewestStops() {
        Graph g = new Graph(3);
        g.addEdge(0, 2, 10);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        assertAll(
                () -> assertEquals(List.of(0, 2), g.fewestStops(0, 2)),
                () -> assertEquals(2, g.dijkstra(0)[2]));
    }

    @Test
    void negativeWeightsAreRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> metro.addEdge(0, 1, -3));
        assertEquals("poids negatif : -3", e.getMessage());
    }

    @Test
    void topologicalOrderOfCourses() {
        Graph courses = new Graph(5);
        courses.addEdge(0, 2, 0);
        courses.addEdge(1, 2, 0);
        courses.addEdge(2, 3, 0);
        courses.addEdge(1, 4, 0);
        courses.addEdge(4, 3, 0);
        assertAll(
                () -> assertEquals(List.of(0, 1, 2, 4, 3), courses.topologicalOrder()),
                () -> assertFalse(courses.hasCycle()));
        courses.addEdge(3, 1, 0);
        IllegalStateException e = assertThrows(IllegalStateException.class, courses::topologicalOrder);
        assertAll(
                () -> assertEquals("cycle", e.getMessage()),
                () -> assertTrue(courses.hasCycle()));
    }

    @Test
    void aSelfLoopIsACycle() {
        Graph g = new Graph(2);
        g.addEdge(1, 1, 0);
        assertTrue(g.hasCycle());
    }

    @Test
    void graphAlgorithmsAreLinearOrNearly() {
        int n = 1_000_000;
        Graph line = new Graph(n);
        for (int i = 0; i + 1 < n; i++) {
            line.addUndirected(i, i + 1, 1);
        }
        int side = 300;
        Graph grid = new Graph(side * side);
        Random r = new Random(11);
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                int v = i * side + j;
                if (j + 1 < side) {
                    grid.addUndirected(v, v + 1, 1 + r.nextInt(9));
                }
                if (i + 1 < side) {
                    grid.addUndirected(v, v + side, 1 + r.nextInt(9));
                }
            }
        }
        assertTimeoutPreemptively(Duration.ofSeconds(4), () -> {
            assertEquals(1, line.countComponents());
            assertEquals(n - 1, line.hops(0)[n - 1]);
            assertEquals(n, line.fewestStops(0, n - 1).size());
            long[] d = grid.dijkstra(0);
            assertTrue(d[side * side - 1] > 0 && d[side * side - 1] < Long.MAX_VALUE);
        });
    }
}
