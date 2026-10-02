package ch8_lambdas.drills.r06_methodrefs;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall06, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 42 pre-fixe ABC true",
            "D02 : 4 init 3",
            "D03 : 8 8.0",
            "D04 : false true true",
            "D05 : a! parent(b) enfant(c)",
            "D06 : 78 m");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Integer::parseInt", "prefix::concat", "String::toUpperCase", "String::startsWith",
            "StringBuilder::new", "int[]::new", "Math::max", "this::exclaim",
            "super::describe", "String::valueOf", "String::charAt",
            // Crescendo : notions des chapitres 9 a 15, interdites au chapitre 8.
            "!List", "!Map", "!Set<", "!ArrayList", "!Collections", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique declare (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall06", args, EXPECTED, API);
    }
}
