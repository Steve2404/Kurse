package ch17_algorithms.drills.r04_backtracking;

import projectkit.TestKit;

import java.util.List;

/**
 * Le correcteur : drill de rappel 4 (ne pas modifier). Enonce : TODO.md.
 * Les tests de REFERENCE (dans solution/) verifient TON Recall04 ; avec l'argument "solution", le corrige.
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "d01 : 4 executions, 4 reussies",
            "d02 : 1 executions, 1 reussies",
            "d03 : 1 executions, 1 reussies",
            "d04 : 1 executions, 1 reussies",
            "d05 : 1 executions, 1 reussies",
            "d06 : 3 executions, 3 reussies");
            // EXPECTED-END

    static final List<String> API = List.of("new ArrayList<>(", "!Math.pow");

    public static void main(String[] args) throws Exception {
        TestKit.checkRecall(Check.class, args, EXPECTED, API);
    }
}
