package ch17_algorithms.drills.r06_dynamic.solution;

import java.util.Arrays;

/** Le corrige du drill 6 : les tables de programmation dynamique. */
public final class Recall06 {

    private Recall06() {
    }

    // D01
    public static long fibonacci(int n) {
        long previous = 0;
        long current = 1;
        for (int i = 0; i < n; i++) {
            long next = previous + current;
            previous = current;
            current = next;
        }
        return previous;
    }

    // D02
    public static int minCoins(int[] coins, int amount) {
        int[] best = new int[amount + 1];
        Arrays.fill(best, Integer.MAX_VALUE);
        best[0] = 0;
        for (int a = 1; a <= amount; a++) {
            for (int c : coins) {
                if (c <= a && best[a - c] != Integer.MAX_VALUE) {
                    best[a] = Math.min(best[a], best[a - c] + 1);
                }
            }
        }
        return best[amount] == Integer.MAX_VALUE ? -1 : best[amount];
    }

    // D03
    public static long countWays(int[] coins, int amount) {
        long[] ways = new long[amount + 1];
        ways[0] = 1;
        for (int c : coins) {
            for (int a = c; a <= amount; a++) {
                ways[a] += ways[a - c];
            }
        }
        return ways[amount];
    }

    // D04
    public static int lcsLength(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                dp[i][j] = a.charAt(i - 1) == b.charAt(j - 1) ? dp[i - 1][j - 1] + 1 : Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }
        return dp[a.length()][b.length()];
    }

    // D05
    public static int editDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= b.length(); j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int replace = dp[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1);
                dp[i][j] = Math.min(replace, Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
            }
        }
        return dp[a.length()][b.length()];
    }

    // D06
    public static int knapsack(int[] weights, int[] values, int capacity) {
        int[] best = new int[capacity + 1];
        for (int k = 0; k < weights.length; k++) {
            for (int w = capacity; w >= weights[k]; w--) {
                best[w] = Math.max(best[w], best[w - weights[k]] + values[k]);
            }
        }
        return best[capacity];
    }

    // D07
    public static int longestIncreasing(int[] a) {
        int[] tails = new int[a.length];
        int length = 0;
        for (int v : a) {
            int lo = 0;
            int hi = length;
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (tails[mid] < v) {
                    lo = mid + 1;
                } else {
                    hi = mid;
                }
            }
            tails[lo] = v;
            if (lo == length) {
                length++;
            }
        }
        return length;
    }
}
