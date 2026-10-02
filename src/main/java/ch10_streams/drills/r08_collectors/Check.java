package ch10_streams.drills.r08_collectors;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du drill de rappel 8 (ne pas modifier). Consigne : TODO.md.
 * Lance-le tel quel pour verifier TON Recall08, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 8 6 Asimov..Zola",
            "D02 : 7 9",
            "D03 : ab a-b [a, b] []",
            "D04 : 8 399.875 3199 71.1",
            "D05 : 1877-1989 12.0 3199",
            "D06 : L'Assommoir Le Silmarillion",
            "D07 : Germinal+L'Assommoir 412 [Asimov, Gibson, Herbert, Simmons, Tolkien, Zola]",
            "D08 : 8.888 1945.625");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Collectors.toList()", "toSet()", "toCollection(", "toUnmodifiableList(", "toUnmodifiableSet(",
            "3xjoining(", "counting()", "averagingInt(", "averagingDouble(", "averagingLong(",
            "summingInt(", "summingDouble(", "summarizingInt(", "summarizingDouble(", "summarizingLong(", "minBy(",
            "maxBy(", "2xtoMap(",
            // Crescendo : notions des chapitres 11 et 13, interdites au chapitre 10.
            "!catch (", "!extends Exception", "!extends RuntimeException", "!Locale",
            "!.parallel()", "!.parallelStream()", "!Atomic", "!Concurrent");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall08", args, EXPECTED, API);
    }
}
