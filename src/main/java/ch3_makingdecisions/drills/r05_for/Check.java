package ch3_makingdecisions.drills.r05_for;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall05, ou avec l'argument "solution".
 */
public class Check {

    static final String[] ARGS = {"4", "8", "15", "16"};

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 01234 | 10 7 4 1",
            "D02 : 06 15 24",
            "D03 : 3",
            "D04 : 4 [4][8][15][16]",
            "D05 : 43",
            "D06 : 16 15 8 4",
            "D07 : 2432902008176640000",
            "D08 : 6",
            "D09 : 012,4,8,15,16");
            // EXPECTED-END

    static final List<String> API = List.of(
            "for (int i = 0, j = ", "i++, j--", "for (; ", "2xfor (String ",
            "args.length - 1", "i -= 3", "for (var i = ", "re:for \\(var \\w+ :##for-each avec var",
            // Crescendo : notions des chapitres 4 a 15, interdites au chapitre 3.
            "!StringBuilder", "!String.format", "!String.valueOf", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!.split(", "!.compareTo(", "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ",
            "!interface ", "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)",
            "!re:case [A-Z]\\w* \\w+ ->##pattern dans un case (preview en Java 17)", "!case null##case null (preview en Java 17)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall05", args, ARGS, EXPECTED, API);
    }
}
