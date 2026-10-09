package ch17_algorithms.projects.p11_delivery;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 11, le capstone (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Delivery et TES tests, ou avec l'argument "solution".
 * Les tests de reference contiennent des tests de VITESSE, et comparent ta tournee a un essai de tous les ordres.
 */
public class Check {

    /** Les bugs que tes tests doivent attraper (INDICES.md dit, replie, ce que change chacun). */
    static final List<Mutant> MUTANTS = List.of(
            new Mutant("Delivery.java", "            roads.get(s[1]).add(new int[]{s[0], s[2]});\n", ""),
            new Mutant("Delivery.java", "            if (s[2] < 0) {", "            if (s[2] < -100) {"),
            new Mutant("Delivery.java", "                long c = dist[u] + e[1];", "                long c = e[1];"),
            new Mutant("Delivery.java", "if (s <= 0 || s >= roads.size() || !seen.add(s)) {", "if (s < 0 || s >= roads.size() || !seen.add(s)) {"),
            new Mutant("Delivery.java", "if (s <= 0 || s >= roads.size() || !seen.add(s)) {", "if (s <= 0 || s >= roads.size()) {"),
            new Mutant("Delivery.java", "if (stops.length > MAX_STOPS) {", "if (stops.length > MAX_STOPS + 1) {"),
            new Mutant("Delivery.java", "(end < 0 || totalEndingAt[i] < totalEndingAt[end])", "(end < 0 || totalEndingAt[i] <= totalEndingAt[end])"),
            new Mutant("Delivery.java", "total[i] = best[full][i] == none || t[i + 1][0] == none ? none : best[full][i] + t[i + 1][0];",
                    "total[i] = best[full][i] == none || t[i + 1][0] == none ? none : best[full][i];"),
            new Mutant("Delivery.java", "            best[1 << i][i] = t[0][i + 1];", "            best[1 << i][i] = 0;"),
            new Mutant("Delivery.java", "        Collections.reverse(order);\n        return order;", "        return order;"),
            new Mutant("Delivery.java", "Arrays.sort(order, Comparator.comparingInt(i -> deadlines[i]));", "Arrays.sort(order, Comparator.comparingInt(i -> durations[i]));"),
            new Mutant("Delivery.java", "PriorityQueue<Integer> kept = new PriorityQueue<>(Collections.reverseOrder());", "PriorityQueue<Integer> kept = new PriorityQueue<>();"),
            new Mutant("Delivery.java", "            if (time > deadlines[i]) {", "            if (time >= deadlines[i]) {"));

    static final List<String> API_CODE = List.of(
            "final class Delivery", "Delivery(int intersections, int[][] streets)", "long[][] travelTimes(int[] places)",
            "long bestTour(int[] stops)", "List<Integer> bestOrder(int[] stops)", "static int maxOnTime(int[] durations, int[] deadlines)",
            "PriorityQueue", "1 << ");

    static final List<String> API_TESTS = List.of(
            "@BeforeEach", "@Test", "@RepeatedTest(", "assertEquals(", "assertThrows(", "assertTimeoutPreemptively(",
            "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 15, MUTANTS, API_CODE, API_TESTS);
    }
}
