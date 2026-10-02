package ch9_collections.drills.r09_wildcards;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 9 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall09, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 3 2 ISD",
            "D02 : 6.0 4.0",
            "D03 : [1, 2] [debut, 1, 2]",
            "D04 : 3 kiwi",
            "D05 : 1 [1, 2, 99] 1");
            // EXPECTED-END

    static final List<String> API = List.of(
            "List<?> list", "List<? extends Number>", "List<? super Integer>", "<T extends Comparable<? super T>>",
            "List<? extends T>", "List<Object> objects",
            // Crescendo : notions des chapitres 10 a 15, interdites au chapitre 9.
            "!.stream(", "!Stream.", "!Stream<", "!Collectors", "!IntStream", "!LongStream", "!DoubleStream", "!.lines()",
            "!Optional", "!.chars()", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.now()",
            "!parallel");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall09", args, EXPECTED, API);
    }
}
