package ch17_algorithms.projects.p09_graphs.solution;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Les tests de reference d'union-find et de Kruskal. */
class UnionFindTest {

    @Test
    void unionsMergeGroups() {
        UnionFind uf = new UnionFind(6);
        assertAll(
                () -> assertEquals(6, uf.count()),
                () -> assertTrue(uf.union(0, 1)),
                () -> assertTrue(uf.union(2, 3)),
                () -> assertTrue(uf.union(1, 3)),
                () -> assertFalse(uf.union(0, 2)),
                () -> assertEquals(3, uf.count()),
                () -> assertTrue(uf.connected(0, 3)),
                () -> assertFalse(uf.connected(0, 4)),
                () -> assertEquals(uf.find(0), uf.find(2)));
    }

    @Test
    void kruskalKeepsTheCheapestEdges() {
        int[][] edges = {{0, 1, 1}, {1, 2, 2}, {0, 2, 3}, {2, 3, 4}, {1, 3, 5}};
        assertEquals(7, UnionFind.minimumSpanningTreeCost(4, edges));
        assertEquals(0, UnionFind.minimumSpanningTreeCost(1, new int[0][]));
    }

    @Test
    void kruskalDoesNotChangeItsInput() {
        int[][] edges = {{0, 1, 9}, {1, 2, 1}};
        UnionFind.minimumSpanningTreeCost(3, edges);
        assertEquals(9, edges[0][2]);
    }

    @Test
    void aDisconnectedNetworkCannotBeSpanned() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> UnionFind.minimumSpanningTreeCost(4, new int[][]{{0, 1, 1}, {2, 3, 1}}));
        assertEquals("graphe non connexe", e.getMessage());
    }

    @Test
    void aMillionUnionsAreNearlyConstant() {
        int n = 1_000_000;
        UnionFind uf = new UnionFind(n);
        Random r = new Random(12);
        // Une ligne de fond (cheres) relie tout le monde, puis 200 000 aretes au hasard (moins cheres).
        int[][] edges = new int[300_000][];
        for (int i = 0; i < 99_999; i++) {
            edges[i] = new int[]{i, i + 1, 1000 + r.nextInt(1000)};
        }
        for (int i = 99_999; i < edges.length; i++) {
            int a = r.nextInt(100_000);
            edges[i] = new int[]{a, (a + 1 + r.nextInt(99_999)) % 100_000, r.nextInt(1000)};
        }
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            for (int i = 0; i + 1 < n; i++) {
                uf.union(i, i + 1);
            }
            assertEquals(1, uf.count());
            assertTrue(uf.connected(0, n - 1));
            assertTrue(UnionFind.minimumSpanningTreeCost(100_000, edges) > 0);
        });
    }
}
