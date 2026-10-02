package ch10_streams.drills.r02_sources;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du drill de rappel 2 (ne pas modifier). Consigne : TODO.md.
 * Lance-le tel quel pour verifier TON Recall02, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 3 0 0",
            "D02 : [1, 2, 4, 8, 16, 32] [1, 3, 9, 27, 81]",
            "D03 : ababab",
            "D04 : [1, 2, 3]",
            "D05 : [8, 1, 9]",
            "D06 : j-a-v-a",
            "D07 : optional apres 3 elements examines",
            "D08 : sans operation terminale : 0 appel",
            "D09 : 9 7",
            "D10 : stream lambda optional",
            "D11 : 1 8",
            "D12 : [1, 2, 3, 4, 5, 6, 7]",
            "D13 : [stream, lambda, optional] [stream, lambda, optional, stream]");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Stream.of(", "Stream.empty()", "Stream.ofNullable(", "Stream.iterate(", "Stream.generate(",
            "Stream.concat(", "Arrays.stream(", ".chars()", ".peek(", ".findFirst()",
            "Supplier<Stream<", ".iterator()", ".takeWhile(", ".limit(",
            // Crescendo : notions des chapitres 11 et 13, interdites au chapitre 10.
            "!catch (", "!extends Exception", "!extends RuntimeException", "!Locale",
            "!.parallel()", "!.parallelStream()", "!Atomic", "!Concurrent");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall02", args, EXPECTED, API);
    }
}
