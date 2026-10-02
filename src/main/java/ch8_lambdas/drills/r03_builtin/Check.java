package ch8_lambdas.drills.r03_builtin;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall03, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : salut 1 true",
            "D02 : a;bbb;",
            "D03 : true false true",
            "D04 : 8 a-b",
            "D05 : OK 42",
            "D06 : A 3");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Supplier<String>", "Consumer<String>", "BiConsumer<String, Integer>", "Predicate<String>",
            "BiPredicate<String, Integer>", "Function<String, Integer>", "BiFunction<String, String, String>", "UnaryOperator<String>",
            "BinaryOperator<Integer>", ".get()", ".accept(", ".test(",
            ".apply(",
            // Crescendo : notions des chapitres 9 a 15, interdites au chapitre 8.
            "!List", "!Map", "!Set<", "!ArrayList", "!Collections", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique declare (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall03", args, EXPECTED, API);
    }
}
