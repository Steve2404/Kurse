package ch3_makingdecisions.drills.r06_jumps;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall06, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 135",
            "D02 : 11 21 31",
            "D03 : 3x4",
            "D04 : 8",
            "D05 : 35 10",
            "D06 : 3 10",
            "D07 : a.b.c",
            "D08 : 10");
            // EXPECTED-END

    static final List<String> API = List.of(
            "continue outer;", "break search;", "break loop;", "continue rows;",
            "while (true)", "re:for \\([^;]*; ; ##for sans condition", "continue;", "do {",
            // Crescendo : notions des chapitres 4 a 15, interdites au chapitre 3.
            "!StringBuilder", "!String.format", "!String.valueOf", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!.split(", "!.compareTo(", "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ",
            "!interface ", "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)",
            "!re:case [A-Z]\\w* \\w+ ->##pattern dans un case (preview en Java 17)", "!case null##case null (preview en Java 17)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall06", args, EXPECTED, API);
    }
}
