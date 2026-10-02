package ch7_beyondclasses.drills.r06_records;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall06, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : Point[x=3, y=4] 3 4 5.0",
            "D02 : true false true false",
            "D03 : Point[x=0, y=2] Range[lo=2, hi=9] Point[x=7, y=7]",
            "D04 : Point[x=0, y=0] Point[x=4, y=5] 1",
            "D05 : false true true",
            "D06 : Pair[key=k, value=1] k true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "record Point(int x, int y)", "Point {", "this(both, both)", "static final Point ORIGIN",
            "record Range(", "Range {", "record Person(String name, int[] scores)", "record Pair(",
            // Crescendo : notions des chapitres 8 a 15, interdites au chapitre 7.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:\\(\\s*\\)\\s*->|\\(\\w+(?:\\s*,\\s*\\w+)*\\)\\s*->|[=(]\\s*\\w+\\s*->##lambda (chapitre 8)", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall06", args, EXPECTED, API);
    }
}
