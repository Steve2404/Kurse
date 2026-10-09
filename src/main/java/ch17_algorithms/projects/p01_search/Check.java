package ch17_algorithms.projects.p01_search;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Search et TES tests, ou avec l'argument "solution".
 * Les tests de reference contiennent des tests de VITESSE : un algorithme trop lent echoue.
 */
public class Check {

    /** Les bugs que tes tests doivent attraper (INDICES.md dit, replie, ce que change chacun). */
    static final List<Mutant> MUTANTS = List.of(
            new Mutant("Search.java", "while (lo <= hi) {\n            int mid = lo + (hi - lo) / 2;\n            if (sorted[mid] < key) {",
                    "while (lo < hi) {\n            int mid = lo + (hi - lo) / 2;\n            if (sorted[mid] < key) {"),
            new Mutant("Search.java", "return -(lo + 1);", "return -1;"),
            new Mutant("Search.java", "if (sorted[mid] < key) {\n                lo = mid + 1;\n            } else {\n                hi = mid;",
                    "if (sorted[mid] <= key) {\n                lo = mid + 1;\n            } else {\n                hi = mid;"),
            new Mutant("Search.java", "int hi = sorted.length;\n        while (lo < hi) {\n            int mid = lo + (hi - lo) / 2;\n            if (sorted[mid] <= key) {",
                    "int hi = sorted.length - 1;\n        while (lo < hi) {\n            int mid = lo + (hi - lo) / 2;\n            if (sorted[mid] <= key) {"),
            new Mutant("Search.java", "int lo = 1;\n        int hi = n;\n        int found = -1;\n        while (lo <= hi) {\n            int mid = lo + (hi - lo) / 2;",
                    "int lo = 1;\n        int hi = n;\n        int found = -1;\n        while (lo <= hi) {\n            int mid = (lo + hi) / 2;"),
            new Mutant("Search.java", "if (mid == 0 || mid <= n / mid) {", "if (mid * mid <= n) {"),
            new Mutant("Search.java", "lo = Math.max(lo, w);", "lo = Math.min(lo, w);"),
            new Mutant("Search.java", "if (load + w > capacity) {", "if (load + w >= capacity) {"),
            new Mutant("Search.java", "if (n < 0) {", "if (n < -1) {"));

    static final List<String> API_CODE = List.of(
            "final class Search", "static int linear(int[] a, int key)", "static int binary(int[] sorted, int key)",
            "static int lowerBound(int[] sorted, int key)", "static int upperBound(int[] sorted, int key)",
            "static int count(int[] sorted, int key)", "static int firstBad(int n, IntPredicate isBad)",
            "static long isqrt(long n)", "static int minCapacity(int[] weights, int days)",
            "lo + (hi - lo) / 2",
            // Tu ECRIS la dichotomie : pas de recherche toute faite.
            "!Arrays.binarySearch", "!Collections.binarySearch", "!Math.sqrt");

    static final List<String> API_TESTS = List.of(
            "@Test", "@ParameterizedTest", "assertEquals(", "assertThrows(", "Integer.MAX_VALUE",
            "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 15, MUTANTS, API_CODE, API_TESTS);
    }
}
