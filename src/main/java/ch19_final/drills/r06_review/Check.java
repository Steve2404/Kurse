package ch19_final.drills.r06_review;

import projectkit.TestKit;

import java.util.List;

/**
 * Le correcteur : drill de rappel 6 (ne pas modifier). Enonce : TODO.md.
 * Les tests de REFERENCE (dans solution/) verifient TON code ; avec l'argument "solution", le corrige.
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            "d01 : 1 executions, 1 reussies",
            "d02 : 1 executions, 1 reussies",
            "d03 : 1 executions, 1 reussies",
            "d04 : 1 executions, 1 reussies",
            "d05 : 1 executions, 1 reussies",
            "d06 : 1 executions, 1 reussies");

    static final List<String> API = List.of(
            "final class Fixes", "final class SafeCounter", "record Customer(", "BigDecimal", "\"DOUBLE\".equals(",
            "plusYears(1)", "try (", "!double", "!== \"", "!plusDays(", "!Data.", "max:method=12",
            "in:Fixes.java!.contains(##aucune recherche lineaire dans une liste",
            "in:Fixes.java!out = out +##pas de concatenation dans une boucle",
            "in:SafeCounter.java!value++##value++ n'est pas atomique");

    public static void main(String[] args) throws Exception {
        TestKit.checkRecall(Check.class, args, EXPECTED, API);
    }
}
