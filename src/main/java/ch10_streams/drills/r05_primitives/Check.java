package ch10_streams.drills.r05_primitives;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du drill de rappel 5 (ne pas modifier). Consigne : TODO.md.
 * Lance-le tel quel pour verifier TON Recall05, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 10 15 2432902008176640000",
            "D02 : 43 5.375 OptionalDouble.empty",
            "D03 : 9 4 2.5",
            "D04 : 1-9 30 1.0",
            "D05 : [######, ######]",
            "D06 : [2, 4, 6, 8, 10]",
            "D07 : 2147483648 -2147483648 0.75",
            "D08 : 6 4.0 3",
            "D09 : 6",
            "D10 : [1, 3, 9, 27, 81] 21",
            "D11 : PDQ",
            "D12 : 9 1.667");
            // EXPECTED-END

    static final List<String> API = List.of(
            "IntStream.range(", "IntStream.rangeClosed(", "LongStream.rangeClosed(", "IntStream.of(", "LongStream.of(",
            "DoubleStream.of(", "IntStream.iterate(", "IntStream.generate(", ".average()", ".sum()",
            ".getAsInt()", ".getAsLong()", ".getAsDouble()", "IntSummaryStatistics", "LongSummaryStatistics",
            "DoubleSummaryStatistics", ".mapToInt(", ".mapToLong(", ".mapToDouble(", ".mapToObj(",
            ".boxed()", ".asLongStream()", ".asDoubleStream()", ".flatMapToInt(", ".chars()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall05", args, EXPECTED, API);
    }
}
