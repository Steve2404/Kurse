package ch9_collections.drills.r07_comparator;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 7 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall07, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : [Ace9, Bob2, Max5, Rex5]",
            "D02 : [Bob2, Max5, Rex5, Ace9]",
            "D03 : [Ace9, Max5, Rex5, Bob2]",
            "D04 : [Rex5, Max5, Bob2, Ace9]",
            "D05 : [A, B, b, c] [A, b, B, c] [c, b, B, A]",
            "D06 : [null, a, b] [b, a, null] 17",
            "D07 : [fig, kiwi, plum, banana] 2 -2");
            // EXPECTED-END

    static final List<String> API = List.of(
            "implements Comparable<Dog>", "public int compareTo(Dog", "Collections.sort(", "Comparator.comparingInt(",
            ".reversed()", ".thenComparing(", "Comparator.comparing(", "Comparator.reverseOrder()",
            "Comparator.naturalOrder()", "String.CASE_INSENSITIVE_ORDER", "Collections.reverseOrder()", "Comparator.nullsFirst(",
            "Comparator.nullsLast(", "Comparator.comparingDouble(", ".thenComparingInt(", "Collections.sort(fruits, byWeight)",
            "Collections.binarySearch(",
            // Crescendo : notions des chapitres 10 a 15, interdites au chapitre 9.
            "!.stream(", "!Stream.", "!Stream<", "!Collectors", "!IntStream", "!LongStream", "!DoubleStream", "!.lines()",
            "!Optional", "!.chars()", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.now()",
            "!parallel");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall07", args, EXPECTED, API);
    }
}
