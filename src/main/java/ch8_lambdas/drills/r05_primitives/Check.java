package ch8_lambdas.drills.r05_primitives;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall05, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 42 1099511627776 true",
            "D02 : false true 81 10 9",
            "D03 : 6 5 ****",
            "D04 : 3.5 -3 3.5",
            "D05 : 1,4,9,",
            "D06 : i1+ l2 d3.5 ol4 od5.5",
            "D07 : true 12 ff 6 1.5 3 5 0.25");
            // EXPECTED-END

    static final List<String> API = List.of(
            "IntSupplier", "LongSupplier", "BooleanSupplier", "IntPredicate",
            "IntUnaryOperator", "IntBinaryOperator", "ToIntFunction<String>", "ToIntBiFunction<String, String>",
            "IntFunction<String>", "IntToDoubleFunction", "DoubleToIntFunction", "DoubleBinaryOperator",
            "ObjIntConsumer<StringBuilder>", ".getAsInt()", ".getAsLong()", ".getAsBoolean()",
            ".applyAsInt(", ".applyAsDouble(", "IntConsumer", "LongConsumer",
            "DoubleConsumer", "ObjLongConsumer<StringBuilder>", "ObjDoubleConsumer<StringBuilder>", "DoublePredicate",
            "LongBinaryOperator", "LongFunction<String>", "LongToIntFunction", "LongToDoubleFunction",
            "DoubleToLongFunction", "ToLongBiFunction<String, String>", "ToDoubleBiFunction<Integer, Integer>", ".applyAsLong(",
            // Crescendo : notions des chapitres 9 a 15, interdites au chapitre 8.
            "!List", "!Map", "!Set<", "!ArrayList", "!Collections", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique declare (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall05", args, EXPECTED, API);
    }
}
