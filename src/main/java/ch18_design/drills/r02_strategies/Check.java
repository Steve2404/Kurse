package ch18_design.drills.r02_strategies;

import projectkit.TestKit;

import java.util.List;

/**
 * Le correcteur : drill de rappel 2 (ne pas modifier). Enonce : TODO.md.
 * Les tests de REFERENCE (dans solution/) verifient TON code ; avec l'argument "solution", le corrige.
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "d01 : 1 executions, 1 reussies",
            "d02 : 3 executions, 3 reussies",
            "d03 : 3 executions, 3 reussies",
            "d04 : 1 executions, 1 reussies",
            "d05 : 1 executions, 1 reussies",
            "d06 : 1 executions, 1 reussies");
            // EXPECTED-END

    static final List<String> API = List.of(
            "@FunctionalInterface", "implements Promotion", "LocalDate.now(clock)", "!LocalDate.now()", "!switch", "!instanceof",
            "in:Checkout.java!ThreeForTwo##la caisse ne nomme aucune promotion",
            "in:Checkout.java!HappyTuesday##la caisse ne nomme aucune promotion",
            "in:Checkout.java!Threshold##la caisse ne nomme aucune promotion");

    public static void main(String[] args) throws Exception {
        TestKit.checkRecall(Check.class, args, EXPECTED, API);
    }
}
