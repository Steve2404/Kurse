package ch17_algorithms.drills.r05_graphs;

import projectkit.TestKit;

import java.util.List;

/**
 * Le correcteur : drill de rappel 5 (ne pas modifier). Enonce : TODO.md.
 * Les tests de REFERENCE (dans solution/) verifient TON Recall05 ; avec l'argument "solution", le corrige.
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "d01 : 1 executions, 1 reussies",
            "d02 : 1 executions, 1 reussies",
            "d03 : 1 executions, 1 reussies",
            "d04 : 1 executions, 1 reussies",
            "d05 : 1 executions, 1 reussies");
            // EXPECTED-END

    static final List<String> API = List.of("ArrayDeque", "PriorityQueue");

    public static void main(String[] args) throws Exception {
        TestKit.checkRecall(Check.class, args, EXPECTED, API);
    }
}
