package ch1_buildingblocks.drills.r03_wrappers;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du drill de rappel 3 (ne pas modifier). Consigne : TODO.md.
 * Lance-le tel quel pour verifier TON Recall03, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 123 123 123",
            "D02 : 2.5 2 -2",
            "D03 : 1 -128 4464",
            "D04 : 511 31 15",
            "D05 : 31 31 15 -17",
            "D06 : true false false",
            "D07 : 123456789012 0.5 -32768",
            "D08 : -2147483648 0.01 65535");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Integer.parseInt(", "Integer.valueOf(", ".intValue()", "Double.valueOf(",
            ".byteValue()", ".shortValue()", "Integer.decode(", "Boolean.valueOf(",
            ".booleanValue()", "Long.parseLong(", "Float.parseFloat(", "Short.parseShort(",
            "Integer.MIN_VALUE", "Character.MAX_VALUE", "re:Integer\\.parseInt\\(\"\\w+\", \\d+\\)##parseInt avec une base",
            // Crescendo : notions des chapitres 2 a 15, interdites au chapitre 1.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!->", "!+=", "!++",
            "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(", "!.toUpperCase(", "!.toLowerCase(", "!.equals(",
            "!.repeat(", "!.strip", "!.trim(", "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ",
            "!implements ", "!catch", "!throw ", "!Locale");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall03", args, EXPECTED, API);
    }
}
