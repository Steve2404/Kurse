package ch17_algorithms.projects.p06_backtracking;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Backtracking et TES tests, ou avec l'argument "solution".
 * Les tests de reference contiennent des tests de VITESSE.
 */
public class Check {

    /** Les bugs que tes tests doivent attraper (INDICES.md dit, replie, ce que change chacun). */
    static final List<Mutant> MUTANTS = List.of(
            new Mutant("Backtracking.java", "long half = power(base, exp / 2);\n        return exp % 2 == 0 ? half * half : half * half * base;",
                    "return base * power(base, exp - 1);"),
            new Mutant("Backtracking.java", "return exp % 2 == 0 ? half * half : half * half * base;", "return exp % 2 == 0 ? half * half : half * base;"),
            new Mutant("Backtracking.java", "            result.add(new ArrayList<>(current));\n            return;\n        }\n        for (int i = 0; i < items.size(); i++) {",
                    "            result.add(current);\n            return;\n        }\n        for (int i = 0; i < items.size(); i++) {"),
            new Mutant("Backtracking.java", "                used[i] = false;\n", ""),
            new Mutant("Backtracking.java", "        subsets(items, index + 1, current, result);\n        current.add(items.get(index));\n        subsets(items, index + 1, current, result);",
                    "        current.add(items.get(index));\n        subsets(items, index + 1, current, result);\n        current.remove(current.size() - 1);\n        subsets(items, index + 1, current, result);\n        current.add(0);"),
            new Mutant("Backtracking.java", "combine(c, remaining - c[i], i, current, result);", "combine(c, remaining - c[i], i + 1, current, result);"),
            new Mutant("Backtracking.java", "combine(c, remaining - c[i], i, current, result);", "combine(c, remaining - c[i], 0, current, result);"),
            new Mutant("Backtracking.java", "if (close < open) {", "if (close < n) {"),
            new Mutant("Backtracking.java", "if (!cols[col] && !diag[d] && !anti[a]) {", "if (!cols[col] && !diag[d]) {"),
            new Mutant("Backtracking.java", "                cols[col] = diag[d] = anti[a] = false;\n", "                cols[col] = diag[d] = false;\n"),
            new Mutant("Backtracking.java", "        grid[r][c] = 0;\n        return false;", "        return false;"),
            new Mutant("Backtracking.java", "grid[br + i / 3][bc + i % 3] == v", "grid[br + i % 3][bc + i % 3] == v"),
            new Mutant("Backtracking.java", "                    if (!ok) {\n                        return false;\n                    }\n", ""));

    static final List<String> API_CODE = List.of(
            "final class Backtracking", "static long power(long base, int exp)",
            "static List<List<Integer>> permutations(List<Integer> items)", "static List<List<Integer>> subsets(List<Integer> items)",
            "static List<List<Integer>> combinationSum(int[] candidates, int target)", "static List<String> parentheses(int n)",
            "static int nQueens(int n)", "static boolean solveSudoku(int[][] grid)", "new ArrayList<>(current)",
            "!Math.pow");

    static final List<String> API_TESTS = List.of(
            "@Test", "@ParameterizedTest", "assertEquals(", "assertTimeoutPreemptively(", "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 20, MUTANTS, API_CODE, API_TESTS);
    }
}
