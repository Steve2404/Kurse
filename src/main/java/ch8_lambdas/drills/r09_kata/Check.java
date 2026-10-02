package ch8_lambdas.drills.r09_kata;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 9 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall09, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 3 2",
            "D02 : 1024 7",
            "D03 : 15 3",
            "D04 : false [java]",
            "D05 : jour 4");
            // EXPECTED-END

    static final List<String> API = List.of(
            "IntPredicate", "IntUnaryOperator.identity()", "Function<Integer, Function<Integer, Integer>>", "a -> b -> a + b",
            "Predicate.not(String::isBlank)", ".andThen(String::toLowerCase)", "BiFunction<String, Integer, String>", "Supplier<String>",
            "((IntPredicate)",
            // Crescendo : notions des chapitres 9 a 15, interdites au chapitre 8.
            "!List", "!Map", "!Set<", "!ArrayList", "!Collections", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique declare (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall09", args, EXPECTED, API);
    }
}
