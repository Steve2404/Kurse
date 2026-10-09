package ch17_algorithms.projects.p06_backtracking.solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * La recursivite et le retour arriere (backtracking).
 * Le gabarit : choisir, explorer (appel recursif), puis DEFAIRE le choix avant d'essayer le suivant.
 */
public final class Backtracking {

    private Backtracking() {
    }

    // Diviser pour regner : b^e = (b^(e/2))^2, et un b de plus si e est impair. O(log e) multiplications.
    public static long power(long base, int exp) {
        if (exp < 0) {
            throw new IllegalArgumentException("exposant negatif : " + exp);
        }
        if (exp == 0) {
            return 1;
        }
        long half = power(base, exp / 2);
        return exp % 2 == 0 ? half * half : half * half * base;
    }

    // Toutes les permutations, dans l'ordre ou on choisit les elements de gauche a droite.
    public static List<List<Integer>> permutations(List<Integer> items) {
        List<List<Integer>> result = new ArrayList<>();
        permute(items, new boolean[items.size()], new ArrayList<>(), result);
        return result;
    }

    private static void permute(List<Integer> items, boolean[] used, List<Integer> current, List<List<Integer>> result) {
        if (current.size() == items.size()) {
            // Piege : une COPIE ; current continue de changer apres.
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            if (!used[i]) {
                used[i] = true;
                current.add(items.get(i));
                permute(items, used, current, result);
                current.remove(current.size() - 1);
                used[i] = false;
            }
        }
    }

    // Tous les sous-ensembles : pour chaque element, d'abord SANS lui, puis AVEC lui.
    public static List<List<Integer>> subsets(List<Integer> items) {
        List<List<Integer>> result = new ArrayList<>();
        subsets(items, 0, new ArrayList<>(), result);
        return result;
    }

    private static void subsets(List<Integer> items, int index, List<Integer> current, List<List<Integer>> result) {
        if (index == items.size()) {
            result.add(new ArrayList<>(current));
            return;
        }
        subsets(items, index + 1, current, result);
        current.add(items.get(index));
        subsets(items, index + 1, current, result);
        current.remove(current.size() - 1);
    }

    // Les combinaisons (chaque candidat reutilisable) dont la somme vaut target, en ordre croissant.
    // Pourquoi `start` : on ne revient jamais a un candidat plus petit, donc pas de doublon [2, 3] / [3, 2].
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        int[] sorted = candidates.clone();
        Arrays.sort(sorted);
        List<List<Integer>> result = new ArrayList<>();
        combine(sorted, target, 0, new ArrayList<>(), result);
        return result;
    }

    private static void combine(int[] c, int remaining, int start, List<Integer> current, List<List<Integer>> result) {
        if (remaining == 0) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < c.length && c[i] <= remaining; i++) {
            current.add(c[i]);
            combine(c, remaining - c[i], i, current, result);
            current.remove(current.size() - 1);
        }
    }

    // Les parentheses bien formees : on ne ferme que s'il y a une ouverte en attente. Ordre : "(" avant ")".
    public static List<String> parentheses(int n) {
        List<String> result = new ArrayList<>();
        parens(n, 0, 0, new StringBuilder(), result);
        return result;
    }

    private static void parens(int n, int open, int close, StringBuilder sb, List<String> result) {
        if (sb.length() == 2 * n) {
            result.add(sb.toString());
            return;
        }
        if (open < n) {
            sb.append('(');
            parens(n, open + 1, close, sb, result);
            sb.deleteCharAt(sb.length() - 1);
        }
        if (close < open) {
            sb.append(')');
            parens(n, open, close + 1, sb, result);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    // Le nombre de facons de placer n reines qui ne se menacent pas. Elagage : on refuse une case attaquee
    // AVANT d'aller plus loin ; trois tableaux disent en O(1) si une colonne ou une diagonale est prise.
    public static int nQueens(int n) {
        return queens(n, 0, new boolean[n], new boolean[2 * n], new boolean[2 * n]);
    }

    private static int queens(int n, int row, boolean[] cols, boolean[] diag, boolean[] anti) {
        if (row == n) {
            return 1;
        }
        int count = 0;
        for (int col = 0; col < n; col++) {
            int d = row - col + n;
            int a = row + col;
            if (!cols[col] && !diag[d] && !anti[a]) {
                cols[col] = diag[d] = anti[a] = true;
                count += queens(n, row + 1, cols, diag, anti);
                cols[col] = diag[d] = anti[a] = false;
            }
        }
        return count;
    }

    // Remplit la grille (0 = case vide) et rend true, ou rend false si elle n'a pas de solution.
    // Piege : une grille de depart deja fausse (deux 5 sur une ligne) doit etre refusee, pas "resolue".
    public static boolean solveSudoku(int[][] grid) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int v = grid[r][c];
                if (v != 0) {
                    grid[r][c] = 0;
                    boolean ok = allowed(grid, r, c, v);
                    grid[r][c] = v;
                    if (!ok) {
                        return false;
                    }
                }
            }
        }
        return fill(grid, 0);
    }

    private static boolean fill(int[][] grid, int cell) {
        if (cell == 81) {
            return true;
        }
        int r = cell / 9;
        int c = cell % 9;
        if (grid[r][c] != 0) {
            return fill(grid, cell + 1);
        }
        for (int v = 1; v <= 9; v++) {
            if (allowed(grid, r, c, v)) {
                grid[r][c] = v;
                if (fill(grid, cell + 1)) {
                    return true;
                }
            }
        }
        grid[r][c] = 0;
        return false;
    }

    private static boolean allowed(int[][] grid, int r, int c, int v) {
        int br = r / 3 * 3;
        int bc = c / 3 * 3;
        for (int i = 0; i < 9; i++) {
            if (grid[r][i] == v || grid[i][c] == v || grid[br + i / 3][bc + i % 3] == v) {
                return false;
            }
        }
        return true;
    }
}
