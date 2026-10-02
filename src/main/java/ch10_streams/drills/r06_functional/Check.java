package ch10_streams.drills.r06_functional;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du drill de rappel 6 (ne pas modifier). Consigne : TODO.md.
 * Lance-le tel quel pour verifier TON Recall06, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : true false true true 3",
            "D02 : 10 16 42",
            "D03 : 6 7",
            "D04 : *** [*, **, ***]",
            "D05 : 6 10 9",
            "D06 : 8000000000 3.5",
            "D07 : -3 10",
            "D08 : 42 1790000000 3 true",
            "D09 : 538 5,3,8,1,9,2,8,7",
            "D10 : 0.25 1.5 6.0",
            "D11 : 1307674368000");
            // EXPECTED-END

    static final List<String> API = List.of(
            "IntPredicate", "LongPredicate", "DoublePredicate", "IntUnaryOperator", "IntBinaryOperator",
            "IntFunction<", "ToIntFunction<", "ToIntBiFunction<", "IntToLongFunction", "IntToDoubleFunction",
            "DoubleToIntFunction", "LongToIntFunction", "IntSupplier", "LongSupplier", "DoubleSupplier",
            "BooleanSupplier", "IntConsumer", "ObjIntConsumer<", "ToDoubleBiFunction<", "DoubleBinaryOperator",
            "DoubleUnaryOperator", "LongBinaryOperator", ".andThen(", ".compose(", ".negate()",
            ".applyAsInt(", ".applyAsLong(", ".applyAsDouble(", ".getAsBoolean()", ".test(",
            // Crescendo : notions des chapitres 11 et 13, interdites au chapitre 10.
            "!catch (", "!extends Exception", "!extends RuntimeException", "!Locale",
            "!.parallel()", "!.parallelStream()", "!Atomic", "!Concurrent");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall06", args, EXPECTED, API);
    }
}
