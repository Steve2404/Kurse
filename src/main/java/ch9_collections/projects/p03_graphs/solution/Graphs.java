package ch9_collections.projects.p03_graphs.solution;

import ch9_collections.projects.p03_graphs.Data;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * SOLUTION du projet 3 - les graphes : Map de listes, Queue, Deque, PriorityQueue.
 */
public class Graphs {

    // Un pas de Dijkstra : un record, compare par distance.
    record Step(String city, int km) {
    }

    // Tri topologique de Kahn ; a egalite, l'ordre alphabetique (PriorityQueue de String = ordre naturel).
    static List<String> topoSort(String[] edges) {
        Map<String, List<String>> next = new TreeMap<>();
        Map<String, Integer> inDegree = new TreeMap<>();
        for (String e : edges) {
            String[] p = e.split(">");
            next.computeIfAbsent(p[0], k -> new ArrayList<>()).add(p[1]);
            inDegree.putIfAbsent(p[0], 0);
            inDegree.merge(p[1], 1, Integer::sum);
        }
        Queue<String> ready = new PriorityQueue<>();
        for (Map.Entry<String, Integer> e : inDegree.entrySet()) {
            if (e.getValue() == 0) {
                ready.offer(e.getKey());
            }
        }
        List<String> order = new ArrayList<>();
        while (!ready.isEmpty()) {
            String c = ready.poll();
            order.add(c);
            for (String n : next.getOrDefault(c, List.of())) {
                if (inDegree.merge(n, -1, Integer::sum) == 0) {
                    ready.offer(n);
                }
            }
        }
        return order;   // plus court que le nombre de sommets : il y a un cycle
    }

    static Map<String, Map<String, Integer>> roads() {
        Map<String, Map<String, Integer>> g = new TreeMap<>();
        for (String r : Data.ROADS) {
            String[] p = r.split(" ");
            int km = Integer.parseInt(p[2]);
            g.computeIfAbsent(p[0], k -> new TreeMap<>()).put(p[1], km);
            g.computeIfAbsent(p[1], k -> new TreeMap<>()).put(p[0], km);
        }
        return g;
    }

    // Parcours en profondeur ITERATIF : une Deque utilisee comme PILE (push / pop).
    static List<String> dfs(Map<String, Map<String, Integer>> g, String start) {
        List<String> seen = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        stack.push(start);
        while (!stack.isEmpty()) {
            String c = stack.pop();
            if (seen.contains(c)) {
                continue;
            }
            seen.add(c);
            List<String> neighbours = new ArrayList<>(g.get(c).keySet());
            for (int i = neighbours.size() - 1; i >= 0; i--) {   // empiler a l'envers pour visiter dans l'ordre alphabetique
                if (!seen.contains(neighbours.get(i))) {
                    stack.push(neighbours.get(i));
                }
            }
        }
        return seen;
    }

    // Parcours en largeur : une Queue (offer / poll) ; parent permet de reconstruire le chemin le moins long en ETAPES.
    static List<String> bfs(Map<String, Map<String, Integer>> g, String from, String to) {
        Map<String, String> parent = new HashMap<>();
        Queue<String> queue = new ArrayDeque<>();
        queue.offer(from);
        parent.put(from, null);
        while (!queue.isEmpty()) {
            String c = queue.poll();
            if (c.equals(to)) {
                break;
            }
            for (String n : g.get(c).keySet()) {
                if (!parent.containsKey(n)) {
                    parent.put(n, c);
                    queue.offer(n);
                }
            }
        }
        LinkedList<String> path = new LinkedList<>();
        for (String c = to; c != null; c = parent.get(c)) {
            path.addFirst(c);
        }
        return path;
    }

    // Dijkstra : la PriorityQueue donne toujours la ville la plus proche non encore fixee.
    static String dijkstra(Map<String, Map<String, Integer>> g, String from, String to) {
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        PriorityQueue<Step> pq = new PriorityQueue<>(Comparator.comparingInt(Step::km).thenComparing(Step::city));
        dist.put(from, 0);
        pq.add(new Step(from, 0));
        int settled = 0;
        while (!pq.isEmpty()) {
            Step s = pq.remove();
            if (s.km() > dist.get(s.city())) {
                continue;                                         // entree perimee
            }
            settled++;
            if (s.city().equals(to)) {
                break;
            }
            for (Map.Entry<String, Integer> e : g.get(s.city()).entrySet()) {
                int d = s.km() + e.getValue();
                if (d < dist.getOrDefault(e.getKey(), Integer.MAX_VALUE)) {
                    dist.put(e.getKey(), d);
                    parent.put(e.getKey(), s.city());
                    pq.add(new Step(e.getKey(), d));
                }
            }
        }
        LinkedList<String> path = new LinkedList<>();
        for (String c = to; c != null; c = parent.get(c)) {
            path.addFirst(c);
        }
        return String.join(" > ", path) + " = " + dist.get(to) + " km (" + settled + " villes fixees)";
    }

    static List<Set<String>> components(Map<String, Map<String, Integer>> g) {
        List<Set<String>> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        for (String start : g.keySet()) {
            if (visited.add(start)) {                              // add rend false si deja present
                Set<String> comp = new TreeSet<>();
                Deque<String> todo = new ArrayDeque<>(List.of(start));
                while (!todo.isEmpty()) {
                    String c = todo.pollFirst();
                    comp.add(c);
                    for (String n : g.get(c).keySet()) {
                        if (visited.add(n)) {
                            todo.offerLast(n);
                        }
                    }
                }
                result.add(comp);
            }
        }
        return result;
    }

    // Maximum sur fenetre glissante : une Deque d'INDICES aux valeurs decroissantes. O(n) au total.
    static List<Integer> windowMax(int[] a, int k) {
        Deque<Integer> dq = new ArrayDeque<>();
        List<Integer> out = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            while (!dq.isEmpty() && dq.peekFirst() <= i - k) {
                dq.pollFirst();                                    // sorti de la fenetre
            }
            while (!dq.isEmpty() && a[dq.peekLast()] <= a[i]) {
                dq.pollLast();                                     // domine par a[i] : ne sera jamais le max
            }
            dq.offerLast(i);
            if (i >= k - 1) {
                out.add(a[dq.peekFirst()]);
            }
        }
        return out;
    }

    public static void main(String[] args) {
        List<String> order = topoSort(Data.COURSES);
        System.out.println("ordre des cours : " + order);
        List<String> cyclic = topoSort(Data.CYCLIC);
        System.out.println("avec un cycle : " + cyclic + " -> " + (cyclic.size() < 4 ? "cycle detecte" : "pas de cycle"));

        Map<String, Map<String, Integer>> g = roads();
        System.out.println("voisins de Lyon : " + g.get("Lyon") + " ; profondeur depuis Paris : " + dfs(g, "Paris"));
        System.out.println("moins d'etapes " + Data.FROM + "-" + Data.TO + " : " + bfs(g, Data.FROM, Data.TO));
        System.out.println("moins de km : " + dijkstra(g, Data.FROM, Data.TO));
        System.out.println("composantes : " + components(g));
        System.out.println("max glissant (" + Data.WINDOW + ") : " + windowMax(Data.MEASURES, Data.WINDOW));

        // L'API Deque : deux familles de methodes. Exceptions (add/remove/element) ou valeurs speciales (offer/poll/peek).
        Deque<String> d = new ArrayDeque<>();
        d.offerFirst("b");
        d.offerFirst("a");
        d.offerLast("c");
        d.push("z");                                               // push = addFirst
        String top = d.peek();                                     // peek = peekFirst
        String popped = d.pop();                                   // pop = removeFirst
        String last = d.pollLast();
        System.out.println("deque : sommet " + top + ", pop " + popped + ", pollLast " + last + ", reste " + d + ", peekLast " + d.peekLast()
                + " ; vide : poll " + new ArrayDeque<String>().poll() + ", peek " + new ArrayDeque<String>().peek());
    }
}
