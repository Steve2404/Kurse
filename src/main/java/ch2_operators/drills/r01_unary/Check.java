package ch2_operators.drills.r01_unary;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall01, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 5 6 7 7",
            "D02 : 10 8 8",
            "D03 : 10 4",
            "D04 : 1",
            "D05 : -4 4 -5 0",
            "D06 : false true false",
            "D07 : b 98 c",
            "D08 : 3 0.5");
            // EXPECTED-END

    static final List<String> API = List.of(
            "2x++", "2x--", "~", "re:![a-z(!]##operateur !",
            "-(-", "re:\\b(\\w+) = \\1\\+\\+;##le piege x = x++;",
            // Crescendo : notions des chapitres 3 a 15, interdites au chapitre 2.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!do {",
            "!->", "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ",
            "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)", "!re:instanceof \\w+ \\w+##instanceof avec variable : pattern matching (chapitre 3)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall01", args, EXPECTED, API);
    }
}
