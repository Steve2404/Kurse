package ch17_algorithms.drills.r02_windows_hashing.solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Le corrige du drill 2 : deux pointeurs, fenetres glissantes, hachage. */
public final class Recall02 {

    private Recall02() {
    }

    // D01
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

    // D02
    public static long maxSumOfK(int[] a, int k) {
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

    // D03
    public static int longestUniqueRun(String s) {
        Map<Character, Integer> last = new HashMap<>();
        int best = 0;
        int left = 0;
        for (int right = 0; right < s.length(); right++) {
            Integer previous = last.put(s.charAt(right), right);
            if (previous != null) {
                left = Math.max(left, previous + 1);
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    // D04
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

    // D05
    public static int[] twoSum(int[] a, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int j = 0; j < a.length; j++) {
            Integer i = seen.get(target - a[j]);
            if (i != null) {
                return new int[]{i, j};
            }
            seen.putIfAbsent(a[j], j);
        }
        return new int[0];
    }

    // D06
    public static int longestConsecutive(int[] a) {
        Set<Integer> values = new HashSet<>();
        for (int v : a) {
            values.add(v);
        }
        int best = 0;
        for (int v : values) {
            if (!values.contains(v - 1)) {
                int length = 1;
                while (values.contains(v + length)) {
                    length++;
                }
                best = Math.max(best, length);
            }
        }
        return best;
    }

    // D07 : chaque intervalle est {debut, fin} ; ceux qui se touchent fusionnent aussi.
    public static int[][] mergeIntervals(int[][] intervals) {
        int[][] sorted = intervals.clone();
        Arrays.sort(sorted, Comparator.comparingInt(x -> x[0]));
        List<int[]> result = new ArrayList<>();
        for (int[] next : sorted) {
            if (!result.isEmpty() && next[0] <= result.get(result.size() - 1)[1]) {
                int[] last = result.get(result.size() - 1);
                last[1] = Math.max(last[1], next[1]);
            } else {
                result.add(new int[]{next[0], next[1]});
            }
        }
        return result.toArray(new int[0][]);
    }
}
