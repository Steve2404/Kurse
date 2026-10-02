package ch9_collections.drills.r03_set;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall03, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 4 [delta, alpha, charlie, bravo] [alpha, bravo, charlie, delta]",
            "D02 : 5 30 10 15 5 null",
            "D03 : [5, 10] [15, 20, 30] [5, 10, 15] [5, 10, 15] [30, 20, 15, 10, 5]",
            "D04 : 5 30 [10, 15, 20]",
            "D05 : [1, 2, 3, 4, 5] [3, 4] [1, 2]",
            "D06 : [h, ab, efg] 3 true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "new HashSet<>(", "new LinkedHashSet<>(", "new TreeSet<>(", "NavigableSet<Integer>",
            ".first()", ".last()", ".floor(", ".ceiling(",
            ".lower(", ".higher(", ".headSet(", ".tailSet(",
            ".subSet(", ".descendingSet()", ".pollFirst()", ".pollLast()",
            ".retainAll(", ".removeAll(", "Set.of(",
            // Crescendo : notions des chapitres 10 a 15, interdites au chapitre 9.
            "!.stream(", "!Stream.", "!Stream<", "!Collectors", "!IntStream", "!LongStream", "!DoubleStream", "!.lines()",
            "!Optional", "!.chars()", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.now()",
            "!parallel");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall03", args, EXPECTED, API);
    }
}
