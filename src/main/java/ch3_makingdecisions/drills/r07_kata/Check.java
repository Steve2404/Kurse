package ch3_makingdecisions.drills.r07_kata;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 7 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall07, ou avec l'argument "solution".
 */
public class Check {

    static final String[] ARGS = {"30", "un", "7", "deux", "12"};

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 225",
            "D02 : 12",
            "D03 : pair texte impair texte pair",
            "D04 : 10",
            "D05 : 111",
            "D06 : I II III IV",
            "D07 : 2",
            "D08 : #|##|###|");
            // EXPECTED-END

    static final List<String> API = List.of(
            "instanceof Integer i &&", "continue outer;", "yield ", "do {",
            "for (String ", "||", "re:= switch \\(##switch expression affectee",
            // Crescendo : notions des chapitres 4 a 15, interdites au chapitre 3.
            "!StringBuilder", "!String.format", "!String.valueOf", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!.split(", "!.compareTo(", "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ",
            "!interface ", "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)",
            "!re:case [A-Z]\\w* \\w+ ->##pattern dans un case (preview en Java 17)", "!case null##case null (preview en Java 17)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall07", args, ARGS, EXPECTED, API);
    }
}
