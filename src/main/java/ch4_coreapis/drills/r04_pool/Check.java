package ch4_coreapis.drills.r04_pool;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall04, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : true true",
            "D02 : false true true",
            "D03 : true",
            "D04 : false true",
            "D05 : true",
            "D06 : false true",
            "D07 : false true false",
            "D08 : false true true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "new String(", ".intern()", "static final String", ".trim()",
            ".concat(", " == ", ".equals(",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall04", args, EXPECTED, API);
    }
}
