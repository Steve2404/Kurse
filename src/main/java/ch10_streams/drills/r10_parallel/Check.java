package ch10_streams.drills.r10_parallel;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du drill de rappel 10 (ne pas modifier). Consigne : TODO.md.
 * Lance-le tel quel pour verifier TON Recall10, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : false true true false",
            "D02 : 5 3 8 1 9 2 8 7",
            "D03 : sequentiel -5050, parallele different : true",
            "D04 : java true",
            "D05 : 7",
            "D06 : true",
            "D07 : 4 2",
            "D08 : 52 true",
            "D09 : 2");
            // EXPECTED-END

    static final List<String> API = List.of(
            ".isParallel()", ".parallelStream()", ".parallel()", ".sequential()", ".forEachOrdered(",
            ".findAny()", ".unordered()", "groupingByConcurrent(", "toConcurrentMap(", "ConcurrentMap<");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall10", args, EXPECTED, API);
    }
}
