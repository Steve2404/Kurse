package ch7_beyondclasses.drills.r08_casting;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 8 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall08, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : ouaf rapporte impossible",
            "D02 : true false false true",
            "D03 : animal chien animal",
            "D04 : long 5 TEXTE",
            "D05 : Leo ouaf true",
            "D06 : ouaf(pet) miaou ...");
            // EXPECTED-END

    static final List<String> API = List.of(
            "class Dog extends Animal implements Pet", "((Dog) a)", "(Dog) a;", "(Animal) p",
            "((String) o)", "instanceof String s &&", "instanceof Pet", "final class Rock",
            // Crescendo : notions des chapitres 8 a 15, interdites au chapitre 7.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:\\(\\s*\\)\\s*->|\\(\\w+(?:\\s*,\\s*\\w+)*\\)\\s*->|[=(]\\s*\\w+\\s*->##lambda (chapitre 8)", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall08", args, EXPECTED, API);
    }
}
