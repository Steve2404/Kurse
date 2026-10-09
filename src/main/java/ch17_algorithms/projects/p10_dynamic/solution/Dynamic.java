package ch17_algorithms.projects.p10_dynamic.solution;

import java.util.Arrays;

/**
 * La programmation dynamique : un probleme qui se decoupe en sous-problemes QUI SE REPETENT.
 * On calcule chaque sous-probleme UNE fois, et on le range dans un tableau (du plus petit au plus grand).
 */
public final class Dynamic {

    private Dynamic() {
    }

    // O(n) au lieu de O(2^n) : on garde seulement les deux derniers termes.
    public static long fibonacci(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n negatif : " + n);
        }
        long previous = 0;
        long current = 1;
        for (int i = 0; i < n; i++) {
            long next = previous + current;
            previous = current;
            current = next;
        }
        return previous;
    }

    // best[a] = le moins de pieces pour faire a ; best[a] = 1 + min(best[a - piece]).
    // Piege : le glouton (la plus grosse piece d'abord) se trompe avec des pieces {1, 3, 4} pour 6.
    public static int minCoins(int[] coins, int amount) {
        int impossible = Integer.MAX_VALUE;
        int[] best = new int[amount + 1];
        Arrays.fill(best, impossible);
        best[0] = 0;
        for (int a = 1; a <= amount; a++) {
            for (int c : coins) {
                if (c <= a && best[a - c] != impossible) {
                    best[a] = Math.min(best[a], best[a - c] + 1);
                }
            }
        }
        return best[amount] == impossible ? -1 : best[amount];
    }

    // Le nombre de COMBINAISONS (l'ordre des pieces ne compte pas).
    // Pourquoi la boucle des pieces a l'EXTERIEUR : chaque combinaison n'est comptee qu'une fois, dans l'ordre des pieces.
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

    // dp[i][j] = la longueur de la plus longue sous-suite commune a a[0, i[ et b[0, j[.
    private static int[][] lcsTable(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                dp[i][j] = a.charAt(i - 1) == b.charAt(j - 1)
                        ? dp[i - 1][j - 1] + 1
                        : Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }
        return dp;
    }

    public static int lcsLength(String a, String b) {
        return lcsTable(a, b)[a.length()][b.length()];
    }

    // On REMONTE la table depuis la fin. A egalite, on remonte d'abord vers le haut (i - 1) : un choix fixe, donc un resultat fixe.
    public static String lcs(String a, String b) {
        int[][] dp = lcsTable(a, b);
        StringBuilder sb = new StringBuilder();
        int i = a.length();
        int j = b.length();
        while (i > 0 && j > 0) {
            if (a.charAt(i - 1) == b.charAt(j - 1)) {
                sb.append(a.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        return sb.reverse().toString();
    }

    // Levenshtein : le moins d'insertions, suppressions ou remplacements pour passer de a a b.
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
                int delete = dp[i - 1][j] + 1;
                int insert = dp[i][j - 1] + 1;
                dp[i][j] = Math.min(replace, Math.min(delete, insert));
            }
        }
        return dp[a.length()][b.length()];
    }

    // Le sac a dos 0/1 : best[w] = la meilleure valeur avec un poids <= w.
    // Piege : parcourir w a l'ENVERS, sinon le meme objet serait pris plusieurs fois.
    public static int knapsack(int[] weights, int[] values, int capacity) {
        int[] best = new int[capacity + 1];
        for (int k = 0; k < weights.length; k++) {
            for (int w = capacity; w >= weights[k]; w--) {
                best[w] = Math.max(best[w], best[w - weights[k]] + values[k]);
            }
        }
        return best[capacity];
    }

    // La plus longue sous-suite STRICTEMENT croissante, en O(n log n) : tails[k] = la plus petite fin
    // possible d'une sous-suite de longueur k + 1. Chaque nombre remplace la premiere fin >= lui (dichotomie).
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

    // Le nombre de chemins (a droite ou en bas) du coin haut-gauche au coin bas-droit, en evitant les cases bloquees.
    public static long gridPaths(boolean[][] blocked) {
        int rows = blocked.length;
        int cols = blocked[0].length;
        long[] paths = new long[cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (blocked[r][c]) {
                    paths[c] = 0;
                } else if (r == 0 && c == 0) {
                    paths[c] = 1;
                } else if (c > 0) {
                    paths[c] += paths[c - 1];
                }
            }
        }
        return paths[cols - 1];
    }
}
