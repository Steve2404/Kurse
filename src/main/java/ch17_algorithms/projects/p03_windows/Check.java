package ch17_algorithms.projects.p03_windows;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Windows et TES tests, ou avec l'argument "solution".
 * Les tests de reference contiennent des tests de VITESSE.
 */
public class Check {

    /** Les bugs que tes tests doivent attraper (INDICES.md dit, replie, ce que change chacun). */
    static final List<Mutant> MUTANTS = List.of(
            new Mutant("Windows.java", "long sum = (long) sorted[i] + sorted[j];", "long sum = sorted[i] + sorted[j];"),
            new Mutant("Windows.java", "while (i < j) {", "while (i <= j) {"),
            new Mutant("Windows.java", "sorted[kept++] = sorted[i];", "sorted[++kept - 1] = sorted[i - 1];"),
            new Mutant("Windows.java", "if (k < 1 || k > a.length) {", "if (k < 1) {"),
            new Mutant("Windows.java", "window += a[i] - a[i - k];", "window += a[i] - a[i - k + 1];"),
            new Mutant("Windows.java", "left = Math.max(left, previous + 1);", "left = previous + 1;"),
            new Mutant("Windows.java", "while (sum >= target) {", "if (sum >= target) {"),
            new Mutant("Windows.java", "next.start() <= result.get(result.size() - 1).end()", "next.start() < result.get(result.size() - 1).end()"),
            new Mutant("Windows.java", "Math.max(last.end(), next.end())", "next.end()"),
            new Mutant("Windows.java", "sorted.sort(Comparator.comparingInt(Interval::end));", "sorted.sort(Comparator.comparingInt(Interval::start));"),
            new Mutant("Windows.java", "while (ends[e] <= starts[s]) {", "while (ends[e] < starts[s]) {"),
            new Mutant("Interval.java", "if (end <= start) {", "if (end < start) {"));

    static final List<String> API_CODE = List.of(
            "record Interval(int start, int end)", "final class Windows", "static int[] pairWithSum(int[] sorted, int target)",
            "static int removeDuplicates(int[] sorted)", "static long maxSumOfK(int[] a, int k)",
            "static int longestUniqueRun(String s)", "static int shortestAtLeast(int[] positive, int target)",
            "static List<Interval> merge(List<Interval> intervals)", "static int maxNonOverlapping(List<Interval> intervals)",
            "static int minRooms(List<Interval> meetings)");

    static final List<String> API_TESTS = List.of(
            "@Test", "@ParameterizedTest", "assertEquals(", "assertThrows(", "assertTimeoutPreemptively(",
            "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 20, MUTANTS, API_CODE, API_TESTS);
    }
}
