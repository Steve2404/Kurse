package ch17_algorithms.drills.r03_stacks_heaps_trees;

import projectkit.TestKit;

import java.util.List;

/**
 * Le correcteur : drill de rappel 3 (ne pas modifier). Enonce : TODO.md.
 * Les tests de REFERENCE (dans solution/) verifient TON Recall03 ; avec l'argument "solution", le corrige.
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "d01 : 3 executions, 3 reussies",
            "d02 : 4 executions, 4 reussies",
            "d03 : 1 executions, 1 reussies",
            "d04 : 1 executions, 1 reussies",
            "d05 : 1 executions, 1 reussies",
            "d06 : 1 executions, 1 reussies",
            "d07 : 1 executions, 1 reussies");
            // EXPECTED-END

    static final List<String> API = List.of(
            "ArrayDeque", "PriorityQueue", "2 * i + 1",
            "!java.util.Stack", "!Arrays.sort", "!TreeMap", "!TreeSet");

    public static void main(String[] args) throws Exception {
        TestKit.checkRecall(Check.class, args, EXPECTED, API);
    }
}
