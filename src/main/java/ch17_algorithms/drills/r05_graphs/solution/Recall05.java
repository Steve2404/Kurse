package ch17_algorithms.drills.r05_graphs.solution;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;

/** Le corrige du drill 5 : les gabarits de graphes, sur des listes d'aretes. */
public final class Recall05 {

    private Recall05() {
    }

    // Les listes d'adjacence d'un graphe ; chaque arete {a, b, poids} ; directed = false : dans les deux sens.
    private static List<List<int[]>> adjacency(int n, int[][] edges, boolean directed) {
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            int w = e.length > 2 ? e[2] : 1;
            adj.get(e[0]).add(new int[]{e[1], w});
            if (!directed) {
                adj.get(e[1]).add(new int[]{e[0], w});
            }
        }
        return adj;
    }

    // D01 : graphe non oriente ; le nombre minimal d'aretes depuis start, -1 si inaccessible.
    public static int[] bfsHops(int n, int[][] edges, int start) {
        List<List<int[]>> adj = adjacency(n, edges, false);
        int[] dist = new int[n];
        Arrays.fill(dist, -1);
        Deque<Integer> queue = new ArrayDeque<>();
        dist[start] = 0;
        queue.add(start);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int[] e : adj.get(u)) {
                if (dist[e[0]] == -1) {
                    dist[e[0]] = dist[u] + 1;
                    queue.add(e[0]);
                }
            }
        }
        return dist;
    }

    // D02 : graphe non oriente ; le nombre de composantes, avec union-find.
    public static int components(int n, int[][] edges) {
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
        int count = n;
        for (int[] e : edges) {
            int a = find(parent, e[0]);
            int b = find(parent, e[1]);
            if (a != b) {
                parent[a] = b;
                count--;
            }
        }
        return count;
    }

    private static int find(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    // D03 : graphe oriente ; Kahn, plus petit sommet pret d'abord ; une liste vide s'il y a un cycle.
    public static List<Integer> topologicalOrder(int n, int[][] edges) {
        List<List<int[]>> adj = adjacency(n, edges, true);
        int[] in = new int[n];
        for (int[] e : edges) {
            in[e[1]]++;
        }
        PriorityQueue<Integer> ready = new PriorityQueue<>();
        for (int v = 0; v < n; v++) {
            if (in[v] == 0) {
                ready.add(v);
            }
        }
        List<Integer> order = new ArrayList<>();
        while (!ready.isEmpty()) {
            int u = ready.poll();
            order.add(u);
            for (int[] e : adj.get(u)) {
                if (--in[e[0]] == 0) {
                    ready.add(e[0]);
                }
            }
        }
        return order.size() == n ? order : List.of();
    }

    // D04 : graphe oriente pondere ; Dijkstra ; Long.MAX_VALUE si inaccessible.
    public static long[] dijkstra(int n, int[][] edges, int start) {
        List<List<int[]>> adj = adjacency(n, edges, true);
        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[start] = 0;
        PriorityQueue<long[]> heap = new PriorityQueue<>(Comparator.comparingLong(x -> x[1]));
        heap.add(new long[]{start, 0});
        while (!heap.isEmpty()) {
            long[] top = heap.poll();
            int u = (int) top[0];
            if (top[1] > dist[u]) {
                continue;
            }
            for (int[] e : adj.get(u)) {
                long c = dist[u] + e[1];
                if (c < dist[e[0]]) {
                    dist[e[0]] = c;
                    heap.add(new long[]{e[0], c});
                }
            }
        }
        return dist;
    }

    // D05 : graphe non oriente pondere ; Kruskal ; -1 si non connexe.
    public static long minimumSpanningTree(int n, int[][] edges) {
        int[][] sorted = edges.clone();
        Arrays.sort(sorted, Comparator.comparingInt(e -> e[2]));
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
        long cost = 0;
        int joined = 0;
        for (int[] e : sorted) {
            int a = find(parent, e[0]);
            int b = find(parent, e[1]);
            if (a != b) {
                parent[a] = b;
                cost += e[2];
                joined++;
            }
        }
        return joined == n - 1 ? cost : -1;
    }
}
