package ch9_collections.drills.r10_kata;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 10 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall10, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : [8, 16, 23]",
            "D02 : {j=2, l=2, m=1}",
            "D03 : 14 true",
            "D04 : [Bob, Dan, eve, alice] [9, 7] [eve, alice]",
            "D05 : FDCA 2 70");
            // EXPECTED-END

    static final List<String> API = List.of(
            "<T extends Comparable<? super T>> List<T> topTwo(", "Integer.valueOf(15)", ".removeIf(", ".merge(",
            "Deque<Integer> stack", ".push(", ".pop()", "String.CASE_INSENSITIVE_ORDER",
            "TreeMap<Integer, String>", ".floorEntry(", ".headMap(", ".ceilingKey(",
            // Crescendo : notions des chapitres 10 a 15, interdites au chapitre 9.
            "!.stream(", "!Stream.", "!Stream<", "!Collectors", "!IntStream", "!LongStream", "!DoubleStream", "!.lines()",
            "!Optional", "!.chars()", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.now()",
            "!parallel");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall10", args, EXPECTED, API);
    }
}
