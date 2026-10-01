package ch10_streams.drills.r03_intermediate;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du drill de rappel 3 (ne pas modifier). Consigne : TODO.md.
 * Lance-le tel quel pour verifier TON Recall03, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : [Dune, Hyperion, Neuromancien]",
            "D02 : [collector, filter, java, lambda, map, optional, stream]",
            "D03 : [stream, optional, map]",
            "D04 : [Germinal, L'Assommoir]",
            "D05 : Asimov:1951, Gibson:1984, Herbert:1965, Simmons:1989, Tolkien:1977, Tolkien:1937, Zola:1885, Zola:1877",
            "D06 : [Germinal, L'Assommoir, Le Hobbit]",
            "D07 : [null, a, b] [b, a, null]",
            "D08 : 10 mots, 9 distincts",
            "D09 : [Hyperion, L'Assommoir, Le Hobbit]",
            "D10 : [5, 3, 8, 1] [9, 2, 8, 7]",
            "D11 : count 9 -> peek 0 fois ; avec filter count 9 -> peek 9 fois",
            "D12 : sorted() sans Comparable -> ClassCastException",
            "D13 : map [2, 0, 1], flatMap [a, b, c]");
            // EXPECTED-END

    static final List<String> API = List.of(
            ".filter(", ".map(", ".flatMap(", ".distinct()", ".sorted()",
            "Comparator.comparing(", "Comparator.comparingInt(", "Comparator.comparingDouble(", ".thenComparing(", ".reversed()",
            "Comparator.reverseOrder()", "Comparator.naturalOrder()", "Comparator.nullsFirst(", "Comparator.nullsLast(", ".skip(",
            ".limit(", ".takeWhile(", ".dropWhile(", ".peek(", "ClassCastException");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall03", args, EXPECTED, API);
    }
}
