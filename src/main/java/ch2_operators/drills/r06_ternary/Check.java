package ch2_operators.drills.r06_ternary;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall06, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : mineur ado",
            "D02 : 1.0 b a",
            "D03 : 5 6",
            "D04 : 123 33 12 51",
            "D05 : 14 20 -2 10",
            "D06 : 8 3",
            "D07 : true 9 1",
            "D08 : 31 trois");
            // EXPECTED-END

    static final List<String> API = List.of(
            "2xre:\\?[^;:]*:[^;]*\\?##ternaires imbriques (a ? b : c ? d : e)", "? 1 : 2.0", "re:\"1\" \\+ 2##concatenation \"1\" + 2", "&&",
            "||", " & ", " | ", "+=",
            // Crescendo : notions des chapitres 3 a 15, interdites au chapitre 2.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!do {",
            "!->", "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ",
            "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)", "!re:instanceof \\w+ \\w+##instanceof avec variable : pattern matching (chapitre 3)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall06", args, EXPECTED, API);
    }
}
