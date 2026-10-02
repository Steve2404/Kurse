package ch10_streams.drills.r07_reduce;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du drill de rappel 7 (ne pas modifier). Consigne : TODO.md.
 * Lance-le tel quel pour verifier TON Recall07, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 52 slojscmjf",
            "D02 : collector Optional.empty",
            "D03 : 52",
            "D04 : un passage 20, deux moities 30",
            "D05 : STREAM 9 collector 7",
            "D06 : slojscmjf",
            "D07 : map map -",
            "D08 : true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "4x.reduce(", ".collect(", "ArrayList::new", "TreeSet::new", "StringBuilder::new",
            "Collector.of(", ".combiner()",
            // Crescendo : notions des chapitres 11 et 13, interdites au chapitre 10.
            "!catch (", "!extends Exception", "!extends RuntimeException", "!Locale",
            "!.parallel()", "!.parallelStream()", "!Atomic", "!Concurrent");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall07", args, EXPECTED, API);
    }
}
