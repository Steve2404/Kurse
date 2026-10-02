package ch4_coreapis.drills.r12_algos;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 12 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall12, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : [6, 5, 4, 3, 2, 1]",
            "D02 : 4 -3 -8 true",
            "D03 : [1, 2, 5, 5, 6, 9]",
            "D04 : 6",
            "D05 : 25",
            "D06 : true noir chat le",
            "D07 : i4",
            "D08 : [[7, 10], [15, 22]] [4, 5, 1, 2, 3]");
            // EXPECTED-END

    static final List<String> API = List.of(
            ">>> 1", "re:-\\(\\w+ \\+ 1\\)##-(point d’insertion) - 1", "new boolean[101]", "new int[26]",
            ".reverse()", "Math.max(", "re:\\[r\\]\\[c\\] \\+= ##produit de matrices", "re:% \\w+\\.length\\]##rotation modulo",
            "Arrays.binarySearch(", "!Arrays.sort(##Arrays.sort (ce drill trie A LA MAIN)",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall12", args, EXPECTED, API);
    }
}
