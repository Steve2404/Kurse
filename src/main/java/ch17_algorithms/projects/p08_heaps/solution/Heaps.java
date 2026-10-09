package ch17_algorithms.projects.p08_heaps.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/** Les classiques des files de priorite. */
public final class Heaps {

    private Heaps() {
    }

    // Les k plus grands, du plus grand au plus petit. Un tas MIN de taille k garde les k meilleurs vus :
    // son sommet est le plus FAIBLE des k, celui qu'on chasse quand un meilleur arrive. O(n log k).
    public static int[] topK(int[] a, int k) {
        if (k < 0) {
            throw new IllegalArgumentException("k negatif : " + k);
        }
        PriorityQueue<Integer> best = new PriorityQueue<>();
        for (int v : a) {
            best.add(v);
            if (best.size() > k) {
                best.poll();
            }
        }
        int[] out = new int[best.size()];
        for (int i = out.length - 1; i >= 0; i--) {
            out[i] = best.poll();
        }
        return out;
    }

    // Fusionner k listes triees : un tas des tetes de liste. O(N log k) pour N elements en tout.
    public static List<Integer> mergeSorted(List<List<Integer>> lists) {
        // Chaque entree du tas : {valeur, numero de la liste, indice dans la liste}.
        PriorityQueue<int[]> heads = new PriorityQueue<>((x, y) -> Integer.compare(x[0], y[0]));
        for (int i = 0; i < lists.size(); i++) {
            if (!lists.get(i).isEmpty()) {
                heads.add(new int[]{lists.get(i).get(0), i, 0});
            }
        }
        List<Integer> out = new ArrayList<>();
        while (!heads.isEmpty()) {
            int[] h = heads.poll();
            out.add(h[0]);
            int next = h[2] + 1;
            if (next < lists.get(h[1]).size()) {
                heads.add(new int[]{lists.get(h[1]).get(next), h[1], next});
            }
        }
        return out;
    }
}
