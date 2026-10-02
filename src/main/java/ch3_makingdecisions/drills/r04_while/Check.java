package ch3_makingdecisions.drills.r04_while;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall04, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 15 0",
            "D02 : 0 1",
            "D03 : 1024 10",
            "D04 : 6 39",
            "D05 : 4 18",
            "D06 : 1",
            "D07 : 8 2142",
            "D08 : 3 6");
            // EXPECTED-END

    static final List<String> API = List.of(
            "4xwhile (", "2xdo {", "re:while \\(\\w+-- >##while (x-- > ...)", "/= 10",
            // Crescendo : notions des chapitres 4 a 15, interdites au chapitre 3.
            "!StringBuilder", "!String.format", "!String.valueOf", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!.split(", "!.compareTo(", "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ",
            "!interface ", "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)",
            "!re:case [A-Z]\\w* \\w+ ->##pattern dans un case (preview en Java 17)", "!case null##case null (preview en Java 17)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall04", args, EXPECTED, API);
    }
}
