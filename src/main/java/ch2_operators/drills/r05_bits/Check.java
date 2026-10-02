package ch2_operators.drills.r05_bits;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall05, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 8 14 6 110",
            "D02 : -1 -6 10 5",
            "D03 : 16 12 32 -2147483648",
            "D04 : -4 15 -1 4",
            "D05 : 4 100 true",
            "D06 : 255 136 0",
            "D07 : true ff8800",
            "D08 : 1 0 4 3",
            "D09 : 40 -16 15 -128");
            // EXPECTED-END

    static final List<String> API = List.of(
            "0b", "0x", " & ", " | ",
            " ^ ", "~", "<<", ">>>",
            "|=", "^=", "&= ~", "Integer.toHexString(",
            "Integer.toBinaryString(", "<<=", ">>=", ">>>=",
            // Crescendo : notions des chapitres 3 a 15, interdites au chapitre 2.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!do {",
            "!->", "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ",
            "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)", "!re:instanceof \\w+ \\w+##instanceof avec variable : pattern matching (chapitre 3)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall05", args, EXPECTED, API);
    }
}
