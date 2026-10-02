package ch4_coreapis.drills.r10_kata;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 10 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall10, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 5 LJEUL LUEJL",
            "D02 : kayaK true false",
            "D03 : [1, 3, 5, 7, 9] 3 -3 5",
            "D04 : [[1, 2, 3], [2, 4, 6], [3, 6, 9]] 6",
            "D05 : 3 -2 -0.0 9.0 2",
            "D06 : 2026-02-28 2026-03-28 2026-03-31 P1M1D",
            "D07 : 328 FRIDAY",
            "D08 : true false true Java-007");
            // EXPECTED-END

    static final List<String> API = List.of(
            ".split(", ".strip()", "new StringBuilder(", ".reverse()",
            "Arrays.copyOf(", "Arrays.sort(", "Arrays.binarySearch(", "Arrays.deepToString(",
            "Math.round(", "Math.ceil(", ".plusMonths(", "Period.between(",
            "ChronoUnit.DAYS.between(", ".formatted(", "new String(",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall10", args, EXPECTED, API);
    }
}
