package ch1_buildingblocks.drills.r07_kata;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du drill de rappel 7 (ne pas modifier). Consigne : TODO.md.
 * Lance-le tel quel pour verifier TON Recall07, ou avec l'argument "solution".
 */
public class Check {

    static final String[] ARGS = {"3", "0x1F", "rouge", "0.25", "false"};

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 93",
            "D02 : 11 1f",
            "D03 : 123rouge",
            "D04 : 0 1",
            "D05 : 3000000003",
            "D06 : ** ticket **",
            "D07 : 25.0 0",
            "D08 : false AB");
            // EXPECTED-END

    static final List<String> API = List.of(
            "String... ", "Integer.decode(", "Integer.toBinaryString(", "Integer.toHexString(",
            "var ", "final double ", "\"\"\"", "Double.valueOf(",
            "'\\u", "static long ", "3_000_000_000L",
            // Crescendo : notions des chapitres 2 a 15, interdites au chapitre 1.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!->", "!+=", "!++",
            "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(", "!.toUpperCase(", "!.toLowerCase(", "!.equals(",
            "!.repeat(", "!.strip", "!.trim(", "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ",
            "!implements ", "!catch", "!throw ", "!Locale");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall07", args, ARGS, EXPECTED, API);
    }
}
