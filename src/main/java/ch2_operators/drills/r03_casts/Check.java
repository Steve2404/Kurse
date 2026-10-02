package ch2_operators.drills.r03_casts;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall03, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : -126 127 0 -1",
            "D02 : 3 -3 9223372036854775807 90",
            "D03 : B z 0.1 0.10000000149011612",
            "D04 : -56 -31072",
            "D05 : 2",
            "D06 : 3 4",
            "D07 : 15 9.0 4 4",
            "D08 : 2");
            // EXPECTED-END

    static final List<String> API = List.of(
            "(byte)", "(short)", "(long)", "(char)",
            "(float)", "(double)", "+=", "*=",
            "/=", "%=", "-=", "re:\\(\\w+ = \\d+\\)##affectation dans une expression (x = 3)",
            // Crescendo : notions des chapitres 3 a 15, interdites au chapitre 2.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!do {",
            "!->", "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ",
            "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)", "!re:instanceof \\w+ \\w+##instanceof avec variable : pattern matching (chapitre 3)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall03", args, EXPECTED, API);
    }
}
