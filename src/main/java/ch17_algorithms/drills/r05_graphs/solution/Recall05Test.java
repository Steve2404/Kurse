package ch17_algorithms.drills.r05_graphs.solution;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/** Les tests de reference du drill 5. */
class Recall05Test {

    static final int[][] METRO = {{0, 1, 2}, {1, 2, 2}, {2, 3, 2}, {0, 4, 5}, {4, 3, 1}, {1, 5, 7}, {5, 3, 1}, {6, 7, 1}};

    @Test
    void d01() {
        assertArrayEquals(new int[]{0, 1, 2, 2, 1, 2, -1, -1}, Recall05.bfsHops(8, METRO, 0));
        int n = 1_000_000;
        int[][] line = new int[n - 1][];
        for (int i = 0; i + 1 < n; i++) {
            line[i] = new int[]{i, i + 1};
        }
        assertEquals(n - 1, assertTimeoutPreemptively(Duration.ofSeconds(3), () -> Recall05.bfsHops(n, line, 0))[n - 1]);
    }

    @Test
    void d02() {
        assertEquals(2, Recall05.components(8, METRO));
        assertEquals(4, Recall05.components(4, new int[0][]));
    }

    @Test
    void d03() {
        int[][] courses = {{0, 2}, {1, 2}, {2, 3}, {1, 4}, {4, 3}};
        assertEquals(List.of(0, 1, 2, 4, 3), Recall05.topologicalOrder(5, courses));
        assertEquals(List.of(), Recall05.topologicalOrder(3, new int[][]{{0, 1}, {1, 2}, {2, 1}}));
    }

    @Test
    void d04() {
        int[][] directed = {{0, 2, 10}, {0, 1, 1}, {1, 2, 1}, {2, 3, 4}};
        assertArrayEquals(new long[]{0, 1, 2, 6, Long.MAX_VALUE}, Recall05.dijkstra(5, directed, 0));
        assertArrayEquals(new long[]{Long.MAX_VALUE, Long.MAX_VALUE, 0, 4, Long.MAX_VALUE}, Recall05.dijkstra(5, directed, 2));
    }

    @Test
    void d05() {
        assertEquals(7, Recall05.minimumSpanningTree(4, new int[][]{{0, 1, 1}, {1, 2, 2}, {0, 2, 3}, {2, 3, 4}, {1, 3, 5}}));
        assertEquals(-1, Recall05.minimumSpanningTree(4, new int[][]{{0, 1, 1}, {2, 3, 1}}));
    }
}
