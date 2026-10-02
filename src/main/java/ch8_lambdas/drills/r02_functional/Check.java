package ch8_lambdas.drills.r02_functional;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall02, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : Bonjour Ana",
            "D02 : hi!! x!!!!",
            "D03 : OK abab cc",
            "D04 : [x] [x] true",
            "D05 : anonyme z 1 1");
            // EXPECTED-END

    static final List<String> API = List.of(
            "@FunctionalInterface", "default Greeter twice()", "static Greeter polite()", "boolean equals(Object o);",
            "interface Shout extends Greeter", "interface Echo extends Greeter", "(Greeter) n ->", "new Greeter() {",
            "static Counter start()",
            // Crescendo : notions des chapitres 9 a 15, interdites au chapitre 8.
            "!List", "!Map", "!Set<", "!ArrayList", "!Collections", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique declare (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall02", args, EXPECTED, API);
    }
}
