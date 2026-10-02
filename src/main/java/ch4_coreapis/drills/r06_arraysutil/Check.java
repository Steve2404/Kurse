package ch4_coreapis.drills.r06_arraysutil;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall06, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : [1, 6, 8, 9]",
            "D02 : [10, 100, 9, Apple, Zebre, apple]",
            "D03 : 2 -1 -3 -5",
            "D04 : 0 -1 1 31",
            "D05 : -1 1 2",
            "D06 : false false true",
            "D07 : [7, 0, 0, 7] [2, 4] [2, 4, 6, 8, 0, 0] [4, 6]",
            "D08 : trouve par hasard [5, 1, 4]");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Arrays.sort(", "4xArrays.binarySearch(", "4xArrays.compare(", "3xArrays.mismatch(",
            "Arrays.equals(", "Arrays.fill(", "Arrays.copyOf(", "Arrays.copyOfRange(",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall06", args, EXPECTED, API);
    }
}
