package ch3_makingdecisions.drills.r02_switchstmt;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall02, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 3 2 1 10 0",
            "D02 : week-end semaine inconnu",
            "D03 : arret",
            "D04 : bien passable",
            "D05 : gel (case sur une constante final)",
            "D06 : deux (switch sur un Integer)",
            "D07 : 15",
            "D08 : impair");
            // EXPECTED-END

    static final List<String> API = List.of(
            "3xbreak;", "2xdefault:", "re:case \\d, \\d##case a plusieurs valeurs", "case 'A':",
            "case FREEZE", "Integer boxed", "var level", "byte small",
            // Crescendo : notions des chapitres 4 a 15, interdites au chapitre 3.
            "!StringBuilder", "!String.format", "!String.valueOf", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!.split(", "!.compareTo(", "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ",
            "!interface ", "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)",
            "!re:case [A-Z]\\w* \\w+ ->##pattern dans un case (preview en Java 17)", "!case null##case null (preview en Java 17)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall02", args, EXPECTED, API);
    }
}
