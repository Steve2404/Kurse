package ch17_algorithms.projects.p09_graphs;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 9 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 * Les tests de reference contiennent des tests de VITESSE.
 */
public class Check {

    /** Les bugs que tes tests doivent attraper (INDICES.md dit, replie, ce que change chacun). */
    static final List<Mutant> MUTANTS = List.of(
            new Mutant("Graph.java", "            int u = queue.poll();\n            order.add(u);", "            int u = queue.pollLast();\n            order.add(u);"),
            new Mutant("Graph.java", "        dist[start] = 0;\n        queue.add(start);", "        dist[start] = 1;\n        queue.add(start);"),
            new Mutant("Graph.java", "        Collections.reverse(path);\n", ""),
            new Mutant("Graph.java", "            components++;\n", "            components = 1;\n"),
            new Mutant("Graph.java", "PriorityQueue<Integer> ready = new PriorityQueue<>();", "PriorityQueue<Integer> ready = new PriorityQueue<>(java.util.Collections.reverseOrder());"),
            new Mutant("Graph.java", "        if (order.size() < size()) {\n            throw new IllegalStateException(\"cycle\");", "        if (order.size() < size() - 1) {\n            throw new IllegalStateException(\"cycle\");"),
            new Mutant("Graph.java", "if (candidate < dist[e.to()]) {", "if (dist[e.to()] == Long.MAX_VALUE) {"),
            new Mutant("Graph.java", "long candidate = dist[u] + e.weight();", "long candidate = e.weight();"),
            new Mutant("Graph.java", "            if (top[1] > dist[u]) {", "            if (top[1] >= dist[u] && u != start) {"),
            new Mutant("Graph.java", "        if (weight < 0) {\n            throw", "        if (weight < -1000) {\n            throw"),
            new Mutant("UnionFind.java", "        if (ra == rb) {\n            return false;\n        }", "        if (ra == rb) {\n            count--;\n            return false;\n        }"),
            new Mutant("UnionFind.java", "Arrays.sort(sorted, Comparator.comparingInt(e -> e[2]));", "Arrays.sort(sorted, Comparator.comparingInt(e -> e[0]));"),
            new Mutant("UnionFind.java", "        if (uf.count() > 1) {\n", "        if (uf.count() > 2) {\n"),
            new Mutant("UnionFind.java", "int[][] sorted = edges.clone();", "int[][] sorted = edges;"));

    static final List<String> API_CODE = List.of(
            "final class Graph", "Graph(int vertices)", "void addEdge(int from, int to, int weight)",
            "void addUndirected(int a, int b, int weight)", "List<Integer> bfsOrder(int start)", "int[] hops(int start)",
            "List<Integer> fewestStops(int from, int to)", "int countComponents()", "List<Integer> topologicalOrder()",
            "boolean hasCycle()", "long[] dijkstra(int start)", "final class UnionFind", "int find(int x)",
            "boolean union(int a, int b)", "boolean connected(int a, int b)", "int count()",
            "static long minimumSpanningTreeCost(int n, int[][] edges)", "ArrayDeque", "PriorityQueue");

    static final List<String> API_TESTS = List.of(
            "@BeforeEach", "@Test", "assertEquals(", "assertThrows(", "assertTimeoutPreemptively(",
            "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 12, MUTANTS, API_CODE, API_TESTS);
    }
}
