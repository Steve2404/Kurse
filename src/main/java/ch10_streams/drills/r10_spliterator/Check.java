package ch10_streams.drills.r10_spliterator;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du drill de rappel 10 (ne pas modifier). Consigne : TODO.md.
 * Lance-le tel quel pour verifier TON Recall10, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 9 9 true true",
            "D02 : 4 5",
            "D03 : stream [lambda, optional, java] false",
            "D04 : 5 [stream+lambda, optional+java, stream+collector, map+java, filter]",
            "D05 : 5 true",
            "D06 : -1 false");
            // EXPECTED-END

    static final List<String> API = List.of(
            "implements Spliterator<", ".spliterator()", ".estimateSize()", ".getExactSizeIfKnown()", ".hasCharacteristics(",
            ".trySplit()", ".tryAdvance(", ".forEachRemaining(", "StreamSupport.stream(", "characteristics()",
            // Crescendo : notions des chapitres 11 et 13, interdites au chapitre 10.
            "!catch (", "!extends Exception", "!extends RuntimeException", "!Locale",
            "!.parallel()", "!.parallelStream()", "!Atomic", "!Concurrent");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall10", args, EXPECTED, API);
    }
}
