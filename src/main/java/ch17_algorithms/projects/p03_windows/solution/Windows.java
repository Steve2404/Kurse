package ch17_algorithms.projects.p03_windows.solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Deux pointeurs, fenetre glissante, intervalles.
 * L'idee commune : au lieu de recommencer pour chaque position (O(n^2)), on fait AVANCER deux indices (O(n)).
 */
public final class Windows {

    private Windows() {
    }

    // Deux pointeurs aux deux bouts d'un tableau TRIE : trop petit, on avance la gauche ; trop grand, on recule la droite.
    public static int[] pairWithSum(int[] sorted, int target) {
        int i = 0;
        int j = sorted.length - 1;
        while (i < j) {
            long sum = (long) sorted[i] + sorted[j];
            if (sum == target) {
                return new int[]{i, j};
            }
            if (sum < target) {
                i++;
            } else {
                j--;
            }
        }
        return new int[0];
    }

    // Deux pointeurs dans le MEME sens : `kept` ecrit, `i` lit.
    public static int removeDuplicates(int[] sorted) {
        if (sorted.length == 0) {
            return 0;
        }
        int kept = 1;
        for (int i = 1; i < sorted.length; i++) {
            if (sorted[i] != sorted[kept - 1]) {
                sorted[kept++] = sorted[i];
            }
        }
        return kept;
    }

    // Fenetre de taille FIXE : on ajoute l'element qui entre, on retire celui qui sort. O(n), pas O(n * k).
    public static long maxSumOfK(int[] a, int k) {
        if (k < 1 || k > a.length) {
            throw new IllegalArgumentException("fenetre invalide : " + k);
        }
        long window = 0;
        for (int i = 0; i < k; i++) {
            window += a[i];
        }
        long best = window;
        for (int i = k; i < a.length; i++) {
            window += a[i] - a[i - k];
            best = Math.max(best, window);
        }
        return best;
    }

    // Fenetre de taille VARIABLE : la droite avance toujours ; la gauche avance quand la fenetre devient invalide.
    // Piege : la gauche ne RECULE jamais (Math.max), sinon on reviendrait sur une repetition deja depassee.
    public static int longestUniqueRun(String s) {
        Map<Character, Integer> lastSeen = new HashMap<>();
        int best = 0;
        int left = 0;
        for (int right = 0; right < s.length(); right++) {
            Integer previous = lastSeen.put(s.charAt(right), right);
            if (previous != null) {
                left = Math.max(left, previous + 1);
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    // La plus courte fenetre de somme >= target (valeurs positives) ; 0 s'il n'y en a pas.
    public static int shortestAtLeast(int[] positive, int target) {
        int best = Integer.MAX_VALUE;
        long sum = 0;
        int left = 0;
        for (int right = 0; right < positive.length; right++) {
            sum += positive[right];
            while (sum >= target) {
                best = Math.min(best, right - left + 1);
                sum -= positive[left++];
            }
        }
        return best == Integer.MAX_VALUE ? 0 : best;
    }

    // Trier par debut, puis fusionner tant que le suivant commence avant (ou pile a) la fin du courant.
    public static List<Interval> merge(List<Interval> intervals) {
        List<Interval> sorted = new ArrayList<>(intervals);
        sorted.sort(Comparator.comparingInt(Interval::start));
        List<Interval> result = new ArrayList<>();
        for (Interval next : sorted) {
            if (!result.isEmpty() && next.start() <= result.get(result.size() - 1).end()) {
                Interval last = result.remove(result.size() - 1);
                result.add(new Interval(last.start(), Math.max(last.end(), next.end())));
            } else {
                result.add(next);
            }
        }
        return result;
    }

    // Glouton : garder a chaque fois le creneau qui FINIT le plus tot laisse le plus de place aux suivants.
    public static int maxNonOverlapping(List<Interval> intervals) {
        List<Interval> sorted = new ArrayList<>(intervals);
        sorted.sort(Comparator.comparingInt(Interval::end));
        int count = 0;
        int freeFrom = Integer.MIN_VALUE;
        for (Interval in : sorted) {
            if (in.start() >= freeFrom) {
                count++;
                freeFrom = in.end();
            }
        }
        return count;
    }

    // Balayage : debuts tries et fins triees, deux pointeurs. Une fin a 10 libere la salle AVANT un debut a 10.
    public static int minRooms(List<Interval> meetings) {
        int n = meetings.size();
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = meetings.get(i).start();
            ends[i] = meetings.get(i).end();
        }
        Arrays.sort(starts);
        Arrays.sort(ends);
        int rooms = 0;
        int best = 0;
        int e = 0;
        for (int s = 0; s < n; s++) {
            while (ends[e] <= starts[s]) {
                e++;
                rooms--;
            }
            rooms++;
            best = Math.max(best, rooms);
        }
        return best;
    }
}
