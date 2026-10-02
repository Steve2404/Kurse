package ch1_buildingblocks.drills.r02_literals;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du drill de rappel 2 (ne pas modifier). Consigne : TODO.md.
 * Lance-le tel quel pour verifier TON Recall02, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 8 16 2 10",
            "D02 : 1000000 65535 170",
            "D03 : 9000000000 9223372036854775807",
            "D04 : 1.25 125.0 0.3",
            "D05 : JJJ 75",
            "D06 : -128 32767 16 32",
            "D07 : 0.0 false null",
            "D08 : fff 100 1010");
            // EXPECTED-END

    static final List<String> API = List.of(
            "re:\\b0[0-7]+\\b##litteral octal", "0x", "0b", "re:\\d_\\d##_ entre deux chiffres",
            "re:\\d+L\\b##litteral long (L)", "re:\\d[fF]\\b##litteral float (f)", "re:\\d[eE]-?\\d##notation scientifique (e)", "'\\u",
            "Byte.MIN_VALUE", "Short.MAX_VALUE", "Character.SIZE", "Integer.SIZE",
            "Integer.toHexString(", "Integer.toOctalString(", "Integer.toBinaryString(", "re:(?m)^\\s*static boolean \\w+;##champ boolean non initialise",
            // Crescendo : notions des chapitres 2 a 15, interdites au chapitre 1.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!->", "!+=", "!++",
            "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(", "!.toUpperCase(", "!.toLowerCase(", "!.equals(",
            "!.repeat(", "!.strip", "!.trim(", "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ",
            "!implements ", "!catch", "!throw ", "!Locale");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall02", args, EXPECTED, API);
    }
}
