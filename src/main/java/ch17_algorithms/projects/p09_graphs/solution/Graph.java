package ch17_algorithms.projects.p09_graphs.solution;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Un graphe ORIENTE et pondere, en listes d'adjacence : pour chaque sommet, la liste de ses aretes sortantes.
 * Les sommets sont numerotes de 0 a n - 1. Memoire O(V + E), contre O(V^2) pour une matrice.
 */
public final class Graph {

    private record Edge(int to, int weight) {
    }

    private final List<List<Edge>> adjacency = new ArrayList<>();

    public Graph(int vertices) {
        for (int i = 0; i < vertices; i++) {
            adjacency.add(new ArrayList<>());
        }
    }

    public int size() {
        return adjacency.size();
    }

    public void addEdge(int from, int to, int weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("poids negatif : " + weight);
        }
        adjacency.get(from).add(new Edge(to, weight));
    }

    // Une ligne de metro va dans les deux sens : deux aretes orientees.
    public void addUndirected(int a, int b, int weight) {
        addEdge(a, b, weight);
        addEdge(b, a, weight);
    }

    // Le parcours en LARGEUR : par cercles de distance croissante, avec une FILE.
    // Piege : marquer un sommet VU au moment ou on l'ajoute a la file, pas quand on le sort (sinon doublons).
    public List<Integer> bfsOrder(int start) {
        List<Integer> order = new ArrayList<>();
        boolean[] seen = new boolean[size()];
        Deque<Integer> queue = new ArrayDeque<>();
        queue.add(start);
        seen[start] = true;
        while (!queue.isEmpty()) {
            int u = queue.poll();
            order.add(u);
            for (Edge e : adjacency.get(u)) {
                if (!seen[e.to()]) {
                    seen[e.to()] = true;
                    queue.add(e.to());
                }
            }
        }
        return order;
    }

    // Le nombre minimal d'ARETES depuis start (-1 si inaccessible). Le BFS les donne : chaque cercle = +1.
    public int[] hops(int start) {
        int[] dist = new int[size()];
        Arrays.fill(dist, -1);
        Deque<Integer> queue = new ArrayDeque<>();
        dist[start] = 0;
        queue.add(start);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (Edge e : adjacency.get(u)) {
                if (dist[e.to()] == -1) {
                    dist[e.to()] = dist[u] + 1;
                    queue.add(e.to());
                }
            }
        }
        return dist;
    }

    // Le chemin avec le moins d'aretes : on retient d'ou l'on vient (previous), puis on remonte depuis l'arrivee.
    public List<Integer> fewestStops(int from, int to) {
        int[] previous = new int[size()];
        Arrays.fill(previous, -2);
        Deque<Integer> queue = new ArrayDeque<>();
        previous[from] = -1;
        queue.add(from);
        while (!queue.isEmpty() && previous[to] == -2) {
            int u = queue.poll();
            for (Edge e : adjacency.get(u)) {
                if (previous[e.to()] == -2) {
                    previous[e.to()] = u;
                    queue.add(e.to());
                }
            }
        }
        if (previous[to] == -2) {
            return List.of();
        }
        List<Integer> path = new ArrayList<>();
        for (int v = to; v != -1; v = previous[v]) {
            path.add(v);
        }
        Collections.reverse(path);
        return path;
    }

    // Les composantes connexes (le graphe est suppose non oriente). Un parcours en PROFONDEUR avec une pile
    // explicite : pas de recursion, donc pas de debordement de pile sur une ligne d'un million de stations.
    public int countComponents() {
        boolean[] seen = new boolean[size()];
        int components = 0;
        for (int s = 0; s < size(); s++) {
            if (seen[s]) {
                continue;
            }
            components++;
            Deque<Integer> stack = new ArrayDeque<>();
            stack.push(s);
            seen[s] = true;
            while (!stack.isEmpty()) {
                int u = stack.pop();
                for (Edge e : adjacency.get(u)) {
                    if (!seen[e.to()]) {
                        seen[e.to()] = true;
                        stack.push(e.to());
                    }
                }
            }
        }
        return components;
    }

    // Le tri topologique (Kahn) : on retire sans cesse un sommet sans arete ENTRANTE. Le plus petit numero d'abord.
    // S'il reste des sommets qu'on ne peut jamais retirer, c'est qu'ils forment un cycle.
    public List<Integer> topologicalOrder() {
        int[] inDegree = new int[size()];
        for (List<Edge> edges : adjacency) {
            for (Edge e : edges) {
                inDegree[e.to()]++;
            }
        }
        PriorityQueue<Integer> ready = new PriorityQueue<>();
        for (int v = 0; v < size(); v++) {
            if (inDegree[v] == 0) {
                ready.add(v);
            }
        }
        List<Integer> order = new ArrayList<>();
        while (!ready.isEmpty()) {
            int u = ready.poll();
            order.add(u);
            for (Edge e : adjacency.get(u)) {
                if (--inDegree[e.to()] == 0) {
                    ready.add(e.to());
                }
            }
        }
        if (order.size() < size()) {
            throw new IllegalStateException("cycle");
        }
        return order;
    }

    public boolean hasCycle() {
        try {
            topologicalOrder();
            return false;
        } catch (IllegalStateException e) {
            return true;
        }
    }

    // Dijkstra : on fixe toujours le sommet le plus PROCHE non encore fixe (un tas), puis on relache ses aretes.
    // Piege : un sommet peut etre dans le tas plusieurs fois ; on ignore les entrees perimees.
    // Pourquoi pas de poids negatifs : un sommet fixe ne pourrait plus etre ameliore, et c'est faux avec un poids < 0.
    public long[] dijkstra(int start) {
        long[] dist = new long[size()];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[start] = 0;
        PriorityQueue<long[]> heap = new PriorityQueue<>((x, y) -> Long.compare(x[1], y[1]));
        heap.add(new long[]{start, 0});
        while (!heap.isEmpty()) {
            long[] top = heap.poll();
            int u = (int) top[0];
            if (top[1] > dist[u]) {
                continue;
            }
            for (Edge e : adjacency.get(u)) {
                long candidate = dist[u] + e.weight();
                if (candidate < dist[e.to()]) {
                    dist[e.to()] = candidate;
                    heap.add(new long[]{e.to(), candidate});
                }
            }
        }
        return dist;
    }
}
