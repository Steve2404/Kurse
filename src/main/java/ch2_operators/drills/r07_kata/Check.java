package ch2_operators.drills.r07_kata;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 7 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall07, ou avec l'argument "solution".
 */
public class Check {

    static final String[] ARGS = {"29", "5", "1011"};

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 5 4 4 5.8",
            "D02 : 29 impair false",
            "D03 : 34 29000",
            "D04 : 31 28",
            "D05 : true 2",
            "D06 : 2 27 5 100",
            "D07 : d D debut",
            "D08 : 2147483676 -2147483620");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Integer.parseInt(", "re:Integer\\.parseInt\\([^,()]+(\\[\\d\\])?, 2\\)##parseInt en base 2", "+=", "(byte)",
            "(short)", "(char)", "&&", "||",
            "<<", ">>", "~", "++",
            "--", " ? ",
            // Crescendo : notions des chapitres 3 a 15, interdites au chapitre 2.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!do {",
            "!->", "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ",
            "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)", "!re:instanceof \\w+ \\w+##instanceof avec variable : pattern matching (chapitre 3)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall07", args, ARGS, EXPECTED, API);
    }
}
