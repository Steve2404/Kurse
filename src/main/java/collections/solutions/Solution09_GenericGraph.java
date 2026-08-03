package collections.solutions;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Corrige de l'exercice 9 (capstone).
 */
public class Solution09_GenericGraph {

    static class Graph<T> {
        private final Map<T, Set<T>> adjacency = new LinkedHashMap<>();

        void addEdge(T from, T to) {
            adjacency.computeIfAbsent(from, k -> new LinkedHashSet<>()).add(to);
            adjacency.computeIfAbsent(to, k -> new LinkedHashSet<>());
        }

        Set<T> neighborsOf(T node) {
            return adjacency.getOrDefault(node, Set.of());
        }

        List<T> bfs(T start) {
            List<T> result = new ArrayList<>();
            Set<T> visited = new LinkedHashSet<>();
            Deque<T> queue = new ArrayDeque<>();
            queue.offer(start);
            visited.add(start);

            while (!queue.isEmpty()) {
                T node = queue.poll();
                result.add(node);
                for (T neighbor : neighborsOf(node)) {
                    if (visited.add(neighbor)) {
                        queue.offer(neighbor);
                    }
                }
            }
            return result;
        }

        List<T> dfs(T start) {
            List<T> result = new ArrayList<>();
            Set<T> visited = new LinkedHashSet<>();
            Deque<T> stack = new ArrayDeque<>();
            stack.push(start);

            while (!stack.isEmpty()) {
                T node = stack.pop();
                if (!visited.add(node)) {
                    continue;
                }
                result.add(node);
                List<T> neighbors = new ArrayList<>(neighborsOf(node));
                for (int i = neighbors.size() - 1; i >= 0; i--) {
                    stack.push(neighbors.get(i));
                }
            }
            return result;
        }

        boolean hasCycle() {
            Map<T, Integer> state = new LinkedHashMap<>();
            for (T node : adjacency.keySet()) {
                state.put(node, 0);
            }
            for (T node : adjacency.keySet()) {
                if (state.get(node) == 0 && dfsHasCycle(node, state)) {
                    return true;
                }
            }
            return false;
        }

        private boolean dfsHasCycle(T node, Map<T, Integer> state) {
            state.put(node, 1); // gris : en cours d'exploration
            for (T neighbor : neighborsOf(node)) {
                int neighborState = state.get(neighbor);
                if (neighborState == 1) {
                    return true; // arete de retour vers un noeud gris => cycle
                }
                if (neighborState == 0 && dfsHasCycle(neighbor, state)) {
                    return true;
                }
            }
            state.put(node, 2); // noir : totalement explore
            return false;
        }
    }
}