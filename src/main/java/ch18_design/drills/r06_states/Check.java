package ch18_design.drills.r06_states;

import projectkit.TestKit;

import java.util.List;

/**
 * Le correcteur : drill de rappel 6 (ne pas modifier). Enonce : TODO.md.
 * Les tests de REFERENCE (dans solution/) verifient TON code ; avec l'argument "solution", le corrige.
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "d01 : 12 executions, 12 reussies",
            "d02 : 1 executions, 1 reussies",
            "d03 : 1 executions, 1 reussies",
            "d04 : 1 executions, 1 reussies");
            // EXPECTED-END

    static final List<String> API = List.of(
            "interface GateState", "implements GateState", "abstract class Report", "public final String render(",
            "extends Report", "!switch", "!instanceof",
            "in:Turnstile.java!if (##le tourniquet delegue a son etat, sans tester",
            "in:Turnstile.java!equals(##le tourniquet delegue a son etat, sans comparer");

    public static void main(String[] args) throws Exception {
        TestKit.checkRecall(Check.class, args, EXPECTED, API);
    }
}
