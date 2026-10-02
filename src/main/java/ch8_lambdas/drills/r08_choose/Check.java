package ch8_lambdas.drills.r08_choose;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 8 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall08, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : t 2.5 false",
            "D02 : x2y",
            "D03 : true true false",
            "D04 : z bda quoi? concat",
            "D05 : 1.5 1 2");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Supplier<String>", "DoubleSupplier", "BooleanSupplier", "Consumer<String>",
            "BiConsumer<String, Integer>", "Predicate<String>", "BiPredicate<String, Character>", "IntPredicate",
            "Function<String, Character>", "BiFunction<String, Integer, String>", "UnaryOperator<String>", "BinaryOperator<String>",
            "ToDoubleFunction<String>", "ToIntBiFunction<String, String>", "IntBinaryOperator",
            // Crescendo : notions des chapitres 9 a 15, interdites au chapitre 8.
            "!List", "!Map", "!Set<", "!ArrayList", "!Collections", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique declare (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall08", args, EXPECTED, API);
    }
}
