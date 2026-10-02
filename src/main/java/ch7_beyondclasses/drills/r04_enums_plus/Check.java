package ch7_beyondclasses.drills.r04_enums_plus;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall04, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 25 0 10 quarter",
            "D02 : 2xquarter 1xten 1xfive 3xcent | 1xquarter 1xfive",
            "D03 : RED GREEN YELLOW RED 90",
            "D04 : [] Light true",
            "D05 : GREEN 25s five");
            // EXPECTED-END

    static final List<String> API = List.of(
            "enum Coin", "enum Light implements Timed", "abstract Light next()", "3xLight next()",
            "Coin(int value)", "getDeclaringClass()", "public String toString()", "values()",
            // Crescendo : notions des chapitres 8 a 15, interdites au chapitre 7.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:\\(\\s*\\)\\s*->|\\(\\w+(?:\\s*,\\s*\\w+)*\\)\\s*->|[=(]\\s*\\w+\\s*->##lambda (chapitre 8)", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall04", args, EXPECTED, API);
    }
}
