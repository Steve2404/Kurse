package ch17_algorithms.projects.p11_delivery.solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Le GPS du livreur. Le depot est le carrefour 0. Trois algorithmes du chapitre, assembles :
 * Dijkstra (les temps entre les arrets), la programmation dynamique sur les sous-ensembles (la tournee),
 * et un glouton avec un tas (le plus de livraisons a l'heure).
 */
public final class Delivery {

    static final int MAX_STOPS = 12;

    private final List<List<int[]>> roads = new ArrayList<>();

    // roads[i] = {a, b, minutes} : une rue a double sens.
    public Delivery(int intersections, int[][] streets) {
        for (int i = 0; i < intersections; i++) {
            roads.add(new ArrayList<>());
        }
        for (int[] s : streets) {
            if (s[2] < 0) {
                throw new IllegalArgumentException("temps negatif : " + s[2]);
            }
            roads.get(s[0]).add(new int[]{s[1], s[2]});
            roads.get(s[1]).add(new int[]{s[0], s[2]});
        }
    }

    // Dijkstra depuis un carrefour (projet 9).
    private long[] dijkstra(int start) {
        long[] dist = new long[roads.size()];
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
            for (int[] e : roads.get(u)) {
                long c = dist[u] + e[1];
                if (c < dist[e[0]]) {
                    dist[e[0]] = c;
                    heap.add(new long[]{e[0], c});
                }
            }
        }
        return dist;
    }

    // Le tableau des temps entre les lieux donnes : UN Dijkstra par lieu, pas un par paire.
    public long[][] travelTimes(int[] places) {
        long[][] t = new long[places.length][];
        for (int i = 0; i < places.length; i++) {
            long[] d = dijkstra(places[i]);
            t[i] = new long[places.length];
            for (int j = 0; j < places.length; j++) {
                t[i][j] = d[places[j]];
            }
        }
        return t;
    }

    private void checkStops(int[] stops) {
        if (stops.length > MAX_STOPS) {
            throw new IllegalArgumentException("trop d'arrets : " + stops.length);
        }
        Set<Integer> seen = new HashSet<>();
        for (int s : stops) {
            if (s <= 0 || s >= roads.size() || !seen.add(s)) {
                throw new IllegalArgumentException("arret invalide : " + s);
            }
        }
    }

    /** Le resultat de Held-Karp : pour chaque dernier arret i, le cout total (retour au depot compris) et les parents. */
    private record Tour(long[] totalEndingAt, int[][] parent) {

        int bestEnd() {
            int end = -1;
            for (int i = 0; i < totalEndingAt.length; i++) {
                if (totalEndingAt[i] != Long.MAX_VALUE && (end < 0 || totalEndingAt[i] < totalEndingAt[end])) {
                    end = i;
                }
            }
            if (end < 0) {
                throw new IllegalStateException("livraison impossible");
            }
            return end;
        }
    }

    // Held-Karp : best[mask][i] = le temps minimal pour partir du depot, visiter EXACTEMENT les arrets de mask,
    // et finir a l'arret i. O(2^k * k^2) au lieu de O(k!) : 12 arrets = 600 000 cases, contre 479 millions d'ordres.
    private Tour tour(int[] stops) {
        int k = stops.length;
        int[] places = new int[k + 1];
        System.arraycopy(stops, 0, places, 1, k);
        long[][] t = travelTimes(places);
        long none = Long.MAX_VALUE;
        long[][] best = new long[1 << k][k];
        int[][] parent = new int[1 << k][k];
        for (long[] row : best) {
            Arrays.fill(row, none);
        }
        for (int i = 0; i < k; i++) {
            best[1 << i][i] = t[0][i + 1];
        }
        for (int mask = 1; mask < (1 << k); mask++) {
            for (int i = 0; i < k; i++) {
                if ((mask & (1 << i)) == 0 || best[mask][i] == none) {
                    continue;
                }
                for (int j = 0; j < k; j++) {
                    if ((mask & (1 << j)) != 0 || t[i + 1][j + 1] == none) {
                        continue;
                    }
                    int next = mask | (1 << j);
                    long candidate = best[mask][i] + t[i + 1][j + 1];
                    // Pourquoi strictement < : a egalite, on garde le premier trouve (le plus petit i) : resultat unique.
                    if (candidate < best[next][j]) {
                        best[next][j] = candidate;
                        parent[next][j] = i;
                    }
                }
            }
        }
        long[] total = new long[k];
        int full = (1 << k) - 1;
        for (int i = 0; i < k; i++) {
            total[i] = best[full][i] == none || t[i + 1][0] == none ? none : best[full][i] + t[i + 1][0];
        }
        return new Tour(total, parent);
    }

    public long bestTour(int[] stops) {
        checkStops(stops);
        if (stops.length == 0) {
            return 0;
        }
        Tour tour = tour(stops);
        return tour.totalEndingAt()[tour.bestEnd()];
    }

    // L'ordre des arrets de la meilleure tournee : on remonte les parents depuis la meilleure fin.
    public List<Integer> bestOrder(int[] stops) {
        checkStops(stops);
        if (stops.length == 0) {
            return List.of();
        }
        Tour tour = tour(stops);
        List<Integer> order = new ArrayList<>();
        int mask = (1 << stops.length) - 1;
        int i = tour.bestEnd();
        while (mask != 0) {
            order.add(stops[i]);
            int previous = tour.parent()[mask][i];
            mask &= ~(1 << i);
            i = previous;
        }
        Collections.reverse(order);
        return order;
    }

    // Le plus de livraisons a l'heure, une a la fois, a partir de l'instant 0. Glouton : par echeance croissante ;
    // si on depasse l'echeance, on abandonne la livraison la PLUS LONGUE gardee (un tas max). O(n log n).
    public static int maxOnTime(int[] durations, int[] deadlines) {
        if (durations.length != deadlines.length) {
            throw new IllegalArgumentException("tailles differentes");
        }
        Integer[] order = new Integer[durations.length];
        for (int i = 0; i < order.length; i++) {
            order[i] = i;
        }
        Arrays.sort(order, Comparator.comparingInt(i -> deadlines[i]));
        PriorityQueue<Integer> kept = new PriorityQueue<>(Collections.reverseOrder());
        long time = 0;
        for (int i : order) {
            kept.add(durations[i]);
            time += durations[i];
            if (time > deadlines[i]) {
                time -= kept.poll();
            }
        }
        return kept.size();
    }
}
