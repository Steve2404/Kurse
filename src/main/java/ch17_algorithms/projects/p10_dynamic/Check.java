package ch17_algorithms.projects.p10_dynamic;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 10 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Dynamic et TES tests, ou avec l'argument "solution".
 * Les tests de reference contiennent des tests de VITESSE.
 */
public class Check {

    /** Les bugs que tes tests doivent attraper (INDICES.md dit, replie, ce que change chacun). */
    static final List<Mutant> MUTANTS = List.of(
            new Mutant("Dynamic.java", "        return previous;\n    }\n\n    // best[a]", "        return current;\n    }\n\n    // best[a]"),
            new Mutant("Dynamic.java", "if (c <= a && best[a - c] != impossible) {", "if (c <= a) {"),
            new Mutant("Dynamic.java", "return best[amount] == impossible ? -1 : best[amount];", "return best[amount] == impossible ? 0 : best[amount];"),
            new Mutant("Dynamic.java", "        for (int c : coins) {\n            for (int a = c; a <= amount; a++) {\n                ways[a] += ways[a - c];\n            }\n        }",
                    "        for (int a = 1; a <= amount; a++) {\n            for (int c : coins) {\n                if (c <= a) {\n                    ways[a] += ways[a - c];\n                }\n            }\n        }"),
            new Mutant("Dynamic.java", "? dp[i - 1][j - 1] + 1", "? dp[i - 1][j] + 1"),
            new Mutant("Dynamic.java", "} else if (dp[i - 1][j] >= dp[i][j - 1]) {", "} else if (dp[i - 1][j] > dp[i][j - 1]) {"),
            new Mutant("Dynamic.java", "        return sb.reverse().toString();", "        return sb.toString();"),
            new Mutant("Dynamic.java", "        for (int i = 0; i <= a.length(); i++) {\n            dp[i][0] = i;\n        }\n", ""),
            new Mutant("Dynamic.java", "(a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1)", "1"),
            new Mutant("Dynamic.java", "for (int w = capacity; w >= weights[k]; w--) {", "for (int w = weights[k]; w <= capacity; w++) {"),
            new Mutant("Dynamic.java", "if (tails[mid] < v) {", "if (tails[mid] <= v) {"),
            new Mutant("Dynamic.java", "                if (blocked[r][c]) {\n                    paths[c] = 0;\n                } else if (r == 0 && c == 0) {",
                    "                if (blocked[r][c] && r + c > 0) {\n                    paths[c] = 0;\n                } else if (r == 0 && c == 0) {"),
            new Mutant("Dynamic.java", "        if (n < 0) {\n            throw new IllegalArgumentException(\"n negatif", "        if (n < -1) {\n            throw new IllegalArgumentException(\"n negatif"));

    static final List<String> API_CODE = List.of(
            "final class Dynamic", "static long fibonacci(int n)", "static int minCoins(int[] coins, int amount)",
            "static long countWays(int[] coins, int amount)", "static int lcsLength(String a, String b)", "static String lcs(String a, String b)",
            "static int editDistance(String a, String b)", "static int knapsack(int[] weights, int[] values, int capacity)",
            "static int longestIncreasing(int[] a)", "static long gridPaths(boolean[][] blocked)");

    static final List<String> API_TESTS = List.of(
            "@Test", "@ParameterizedTest", "assertEquals(", "assertTimeoutPreemptively(", "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 20, MUTANTS, API_CODE, API_TESTS);
    }
}
