package ch3_makingdecisions.drills.r01_if;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall01, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : ABCD",
            "D02 : impair",
            "D03 : grand entier 150 | entier 6 | texte \"ok\" | autre",
            "D04 : 42 -1",
            "D05 : 50",
            "D06 : sans accolades, seule la 1re instruction est dans le if",
            "D07 : null n'est jamais une instance",
            "D08 : doux");
            // EXPECTED-END

    static final List<String> API = List.of(
            "3xelse if", "instanceof Integer i &&", "re:!\\(\\w+ instanceof \\w+ \\w+\\)##portee de flux !(o instanceof T v)", "instanceof String s",
            "re:if \\([^{]*\\)\\s*\\n\\s*System##if sans accolades",
            // Crescendo : notions des chapitres 4 a 15, interdites au chapitre 3.
            "!StringBuilder", "!String.format", "!String.valueOf", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!.split(", "!.compareTo(", "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ",
            "!interface ", "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)",
            "!re:case [A-Z]\\w* \\w+ ->##pattern dans un case (preview en Java 17)", "!case null##case null (preview en Java 17)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall01", args, EXPECTED, API);
    }
}
