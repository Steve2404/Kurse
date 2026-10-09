package ch17_algorithms.drills.r02_windows_hashing;

import projectkit.TestKit;

import java.util.List;

/**
 * Le correcteur : drill de rappel 2 (ne pas modifier). Enonce : TODO.md.
 * Les tests de REFERENCE (dans solution/) verifient TON Recall02 ; avec l'argument "solution", le corrige.
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "d01 : 1 executions, 1 reussies",
            "d02 : 4 executions, 4 reussies",
            "d03 : 4 executions, 4 reussies",
            "d04 : 3 executions, 3 reussies",
            "d05 : 1 executions, 1 reussies",
            "d06 : 1 executions, 1 reussies",
            "d07 : 1 executions, 1 reussies");
            // EXPECTED-END

    static final List<String> API = List.of("(long)", "HashMap", "HashSet", "Math.max(left");

    public static void main(String[] args) throws Exception {
        TestKit.checkRecall(Check.class, args, EXPECTED, API);
    }
}
