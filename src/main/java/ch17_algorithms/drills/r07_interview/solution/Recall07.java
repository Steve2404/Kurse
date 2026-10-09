package ch17_algorithms.drills.r07_interview.solution;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/** Le corrige du drill 7 : sept questions d'entretien, et la famille d'algorithmes de chacune. */
public final class Recall07 {

    private Recall07() {
    }

    // D01 : un seul passage, en retenant le plus bas prix vu (glouton). O(n).
    public static int maxProfit(int[] prices) {
        int lowest = Integer.MAX_VALUE;
        int best = 0;
        for (int p : prices) {
            lowest = Math.min(lowest, p);
            best = Math.max(best, p - lowest);
        }
        return best;
    }

    // D02 : Kadane, une programmation dynamique a une variable : la meilleure somme qui FINIT ici. O(n).
    public static long maxSubarray(int[] a) {
        long endingHere = a[0];
        long best = a[0];
        for (int i = 1; i < a.length; i++) {
            endingHere = Math.max(a[i], endingHere + a[i]);
            best = Math.max(best, endingHere);
        }
        return best;
    }

    // D03 : une dichotomie ; a chaque milieu, une des deux moities est triee : on regarde si la cible y est.
    public static int searchRotated(int[] a, int target) {
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] == target) {
                return mid;
            }
            if (a[lo] <= a[mid]) {
                if (a[lo] <= target && target < a[mid]) {
                    hi = mid - 1;
                } else {
                    lo = mid + 1;
                }
            } else {
                if (a[mid] < target && target <= a[hi]) {
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            }
        }
        return -1;
    }

    // D04 : un graphe cache dans une grille ; un parcours (ici en largeur) par ile non encore vue. O(lignes x colonnes).
    public static int islands(char[][] grid) {
        int rows = grid.length;
        int cols = rows == 0 ? 0 : grid[0].length;
        boolean[][] seen = new boolean[rows][cols];
        int count = 0;
        int[][] moves = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] != '1' || seen[r][c]) {
                    continue;
                }
                count++;
                Deque<int[]> queue = new ArrayDeque<>();
                queue.add(new int[]{r, c});
                seen[r][c] = true;
                while (!queue.isEmpty()) {
                    int[] cell = queue.poll();
                    for (int[] m : moves) {
                        int nr = cell[0] + m[0];
                        int nc = cell[1] + m[1];
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == '1' && !seen[nr][nc]) {
                            seen[nr][nc] = true;
                            queue.add(new int[]{nr, nc});
                        }
                    }
                }
            }
        }
        return count;
    }

    // D05 : programmation dynamique : ok[i] = le debut s[0, i[ se decoupe en mots du dictionnaire. O(n^2).
    public static boolean wordBreak(String s, List<String> dictionary) {
        Set<String> words = new HashSet<>(dictionary);
        boolean[] ok = new boolean[s.length() + 1];
        ok[0] = true;
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i && !ok[i]; j++) {
                ok[i] = ok[j] && words.contains(s.substring(j, i));
            }
        }
        return ok[s.length()];
    }

    // D06 : produits a gauche puis produits a droite (prefixes et suffixes), sans division. O(n).
    public static long[] productExceptSelf(int[] a) {
        long[] out = new long[a.length];
        long left = 1;
        for (int i = 0; i < a.length; i++) {
            out[i] = left;
            left *= a[i];
        }
        long right = 1;
        for (int i = a.length - 1; i >= 0; i--) {
            out[i] *= right;
            right *= a[i];
        }
        return out;
    }

    // D07 : Dijkstra depuis la source, puis le PLUS LONG des plus courts chemins ; -1 si un sommet n'est jamais atteint.
    public static long networkDelay(int n, int[][] times, int source) {
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] t : times) {
            adj.get(t[0]).add(new int[]{t[1], t[2]});
        }
        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[source] = 0;
        PriorityQueue<long[]> heap = new PriorityQueue<>(Comparator.comparingLong(x -> x[1]));
        heap.add(new long[]{source, 0});
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
        long worst = 0;
        for (long d : dist) {
            if (d == Long.MAX_VALUE) {
                return -1;
            }
            worst = Math.max(worst, d);
        }
        return worst;
    }
}
