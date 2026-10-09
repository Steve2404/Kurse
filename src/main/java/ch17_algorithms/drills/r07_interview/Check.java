package ch17_algorithms.drills.r07_interview;

import projectkit.TestKit;

import java.util.List;

/**
 * Le correcteur : drill de rappel 7, l'entretien (ne pas modifier). Enonce : TODO.md.
 * Les tests de REFERENCE (dans solution/) verifient TON Recall07 ; avec l'argument "solution", le corrige.
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "d01 : 1 executions, 1 reussies",
            "d02 : 1 executions, 1 reussies",
            "d03 : 6 executions, 6 reussies",
            "d04 : 1 executions, 1 reussies",
            "d05 : 1 executions, 1 reussies",
            "d06 : 1 executions, 1 reussies",
            "d07 : 1 executions, 1 reussies");
            // EXPECTED-END

    // D06 se fait sans division.
    static final List<String> API = List.of("!/ a[", "!/= a[");

    public static void main(String[] args) throws Exception {
        TestKit.checkRecall(Check.class, args, EXPECTED, API);
    }
}
