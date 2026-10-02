package ch8_lambdas.drills.r01_syntax;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall01, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 7 12 4 3",
            "D02 : true false true",
            "D03 : bonjour bloc",
            "D04 : 10 30 5",
            "D05 : true false true",
            "D06 : runrun");
            // EXPECTED-END

    static final List<String> API = List.of(
            "(a, b) -> a + b", "(int a, int b) ->", "(var a, var b) ->", "(final int a, final int b) ->",
            "s -> s.isEmpty()", "(s) ->", "(String s) -> {", "() -> \"bonjour\"",
            "return s -> s.startsWith(", "Runnable r = () ->",
            // Crescendo : notions des chapitres 9 a 15, interdites au chapitre 8.
            "!List", "!Map", "!Set<", "!ArrayList", "!Collections", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique declare (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall01", args, EXPECTED, API);
    }
}
