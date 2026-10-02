package ch8_lambdas.drills.r04_compose;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall04, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 9 5 5 id",
            "D02 : true false true false true",
            "D03 : 1:abc 2:3",
            "D04 : =42",
            "D05 : kiwi pomme ab",
            "D06 : 9");
            // EXPECTED-END

    static final List<String> API = List.of(
            ".andThen(", ".compose(", "Function.<Integer>identity()", "UnaryOperator.<String>identity()",
            ".and(", ".or(", ".negate()", "Predicate.not(",
            "Predicate.isEqual(", "BinaryOperator.minBy(", "BinaryOperator.maxBy(",
            // Crescendo : notions des chapitres 9 a 15, interdites au chapitre 8.
            "!List", "!Map", "!Set<", "!ArrayList", "!Collections", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique declare (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall04", args, EXPECTED, API);
    }
}
