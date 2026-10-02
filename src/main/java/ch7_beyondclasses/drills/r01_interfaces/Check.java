package ch7_beyondclasses.drills.r01_interfaces;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall01, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 1 1 9.0 4 polygone a 4 cotes",
            "D02 : polygone a 4 cotes=9.0 cercle=3.14",
            "D03 : true true false false",
            "D04 : rouge 9.0 polygone a 4 cotes",
            "D05 : cercle 12");
            // EXPECTED-END

    static final List<String> API = List.of(
            "interface Shape", "interface Polygon extends Shape", "abstract class Base implements Polygon, Named", "implements Colored",
            "int UNIT = 1", "public double area()", "((Shape) c)", "((Named) c)",
            // Crescendo : notions des chapitres 8 a 15, interdites au chapitre 7.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:\\(\\s*\\)\\s*->|\\(\\w+(?:\\s*,\\s*\\w+)*\\)\\s*->|[=(]\\s*\\w+\\s*->##lambda (chapitre 8)", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall01", args, EXPECTED, API);
    }
}
