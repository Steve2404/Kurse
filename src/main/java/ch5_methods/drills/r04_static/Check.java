package ch5_methods.drills.r04_static;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall04, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : Recall04 charge",
            "D02 : main",
            "D03 : champ static base = 10",
            "D03 : bloc static = 2",
            "D03 : champ d'instance value = 10",
            "D03 : bloc d'instance id = 1",
            "D03 : champ d'instance value = 10",
            "D03 : bloc d'instance id = 2",
            "D04 : 1 2 2 12 14 12",
            "D03 : champ d'instance value = 100",
            "D03 : bloc d'instance id = 3",
            "D05 : 100 3 3",
            "D06 : 2 ab",
            "D07 : 8 314");
            // EXPECTED-END

    static final List<String> API = List.of(
            "static {", "2xstatic {", "static final int", "re:(?m)^\\s+\\{\\s*$##bloc { } de l objet",
            "final int id", "import static", "re:\\w+ \\w+ = null;##reference null", "final int[]",
            "final StringBuilder",
            // Crescendo : notions des chapitres 6 a 15, interdites au chapitre 5.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ##extends / heritage (chapitre 6)", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!super##super (chapitre 6)", "!abstract ##abstract (chapitre 6)", "!@Override##@Override (chapitre 6)", "!re:(?m)^\\s*(?:(?:public|protected|private)\\s+)?[A-Z]\\w*\\s*\\([^;{)]*\\)\\s*\\{##constructeur ecrit par toi (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator",
            "!.chars()", "!LocalDate.now()", "!LocalDateTime.now()", "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall04", args, EXPECTED, API);
    }
}
