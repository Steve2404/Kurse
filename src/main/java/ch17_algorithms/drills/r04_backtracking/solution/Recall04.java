package ch17_algorithms.drills.r04_backtracking.solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Le corrige du drill 4 : le gabarit choisir / explorer / defaire. */
public final class Recall04 {

    private Recall04() {
    }

    // D01
    public static long power(long base, int exp) {
        if (exp == 0) {
            return 1;
        }
        long half = power(base, exp / 2);
        return exp % 2 == 0 ? half * half : half * half * base;
    }

    // D02
    public static List<List<Integer>> permutations(List<Integer> items) {
        List<List<Integer>> out = new ArrayList<>();
        permute(items, new boolean[items.size()], new ArrayList<>(), out);
        return out;
    }

    private static void permute(List<Integer> items, boolean[] used, List<Integer> cur, List<List<Integer>> out) {
        if (cur.size() == items.size()) {
            out.add(new ArrayList<>(cur));
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            if (!used[i]) {
                used[i] = true;
                cur.add(items.get(i));
                permute(items, used, cur, out);
                cur.remove(cur.size() - 1);
                used[i] = false;
            }
        }
    }

    // D03
    public static List<List<Integer>> subsets(List<Integer> items) {
        List<List<Integer>> out = new ArrayList<>();
        subsets(items, 0, new ArrayList<>(), out);
        return out;
    }

    private static void subsets(List<Integer> items, int index, List<Integer> cur, List<List<Integer>> out) {
        if (index == items.size()) {
            out.add(new ArrayList<>(cur));
            return;
        }
        subsets(items, index + 1, cur, out);
        cur.add(items.get(index));
        subsets(items, index + 1, cur, out);
        cur.remove(cur.size() - 1);
    }

    // D04
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        int[] c = candidates.clone();
        Arrays.sort(c);
        List<List<Integer>> out = new ArrayList<>();
        combine(c, target, 0, new ArrayList<>(), out);
        return out;
    }

    private static void combine(int[] c, int remaining, int start, List<Integer> cur, List<List<Integer>> out) {
        if (remaining == 0) {
            out.add(new ArrayList<>(cur));
            return;
        }
        for (int i = start; i < c.length && c[i] <= remaining; i++) {
            cur.add(c[i]);
            combine(c, remaining - c[i], i, cur, out);
            cur.remove(cur.size() - 1);
        }
    }

    // D05
    public static List<String> parentheses(int n) {
        List<String> out = new ArrayList<>();
        parens(n, 0, 0, new StringBuilder(), out);
        return out;
    }

    private static void parens(int n, int open, int close, StringBuilder sb, List<String> out) {
        if (sb.length() == 2 * n) {
            out.add(sb.toString());
            return;
        }
        if (open < n) {
            sb.append('(');
            parens(n, open + 1, close, sb, out);
            sb.deleteCharAt(sb.length() - 1);
        }
        if (close < open) {
            sb.append(')');
            parens(n, open, close + 1, sb, out);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    // D06
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
}
