package ch4_coreapis.drills.r05_arrays;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall05, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : [0, 0, 0] [4, 5, 6] 2",
            "D02 : [null, null] [false, false] [0.0] 0",
            "D03 : 2 3 [[0, 0, 0], [0, 0, 9]] 3 3",
            "D04 : [1, 2] true 3",
            "D05 : [99, 2, 3] [1, 2, 3] true false",
            "D06 : [2, 4, 6]",
            "D07 : 2 y 21",
            "D08 : java av [a, -, b]");
            // EXPECTED-END

    static final List<String> API = List.of(
            "new int[3]", "re:int \\w+\\[\\] = \\{##declaration int x[]", "new int[] {", "re:int\\[\\] \\w+\\[\\]##declaration int[] x[]",
            "new int[2][3]", "new int[3][]", ".clone()", "Arrays.deepToString(",
            "new boolean[", "new char[", "new String(", "String.valueOf(",
            ".toCharArray()", "Object[]",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall05", args, EXPECTED, API);
    }
}
