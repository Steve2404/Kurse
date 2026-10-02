package ch1_buildingblocks.drills.r04_textblocks;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du drill de rappel 4 (ne pas modifier). Consigne : TODO.md.
 * Lance-le tel quel pour verifier TON Recall04, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : [bonjour]",
            "D02 : [a",
            "b",
            "]",
            "D03 :",
            "  x",
            "  y",
            "D04 : [un deux]",
            "D05 : [fin ]",
            "D06 : \"cite\" et \"\"\"triple\"\"\" fin",
            "D07 :",
            "col1	col2",
            "ligne2",
            "D08 :",
            "haut",
            "",
            "bas");
            // EXPECTED-END

    static final List<String> API = List.of(
            "16x\"\"\"##8 text blocks", "\\s", "re:\\\\\\r?\\n##\\ en fin de ligne", "\\t",
            "\\\"\"\"##\\\"\"\" dans un text block",
            // Crescendo : notions des chapitres 2 a 15, interdites au chapitre 1.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!->", "!+=", "!++",
            "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(", "!.toUpperCase(", "!.toLowerCase(", "!.equals(",
            "!.repeat(", "!.strip", "!.trim(", "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ",
            "!implements ", "!catch", "!throw ", "!Locale");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall04", args, EXPECTED, API);
    }
}
