package ch9_collections.drills.r01_collection;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall01, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : true 3 false true [java, map, java]",
            "D02 : true false 1",
            "D03 : true false [map, java]",
            "D04 : true [1, 3, 5] 1;3;5;",
            "D05 : [y, y, w] true true",
            "D06 : true true false");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Collection<String> c = new ArrayList<>()", "Collection<String> set = new HashSet<>()", ".add(", ".remove(",
            ".removeIf(", ".forEach(", ".addAll(", ".retainAll(",
            ".containsAll(", ".clear()", ".isEmpty()", ".contains(",
            // Crescendo : notions des chapitres 10 a 15, interdites au chapitre 9.
            "!.stream(", "!Stream.", "!Stream<", "!Collectors", "!IntStream", "!LongStream", "!DoubleStream", "!.lines()",
            "!Optional", "!.chars()", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.now()",
            "!parallel");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall01", args, EXPECTED, API);
    }
}
