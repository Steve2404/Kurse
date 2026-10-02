package ch4_coreapis.projects.p07_algolab;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 7 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON AlgoLab, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "insertion : [1, 3, 3, 8, 8, 15, 17, 23, 29, 42] decalages=24",
            "selection : echanges=6 identique a Arrays.sort true true original intact true",
            "uniques : [1, 3, 8, 15, 17, 23, 29, 42] (8 sur 10)",
            "paires de somme 32 : 3+29 15+17 en 7 etapes",
            "borne inferieure : 8->3 9->5 0->0 50->10",
            "sommes d'intervalle : [0,4]=60 [2,2]=17 [5,9]=89 [0,9]=149",
            "fenetre de 3 : max=65 a partir de l'indice 5 [42, 15, 8]",
            "kadane : max=6 jours 3 a 6 [4, -1, 2, 1]",
            "premiers <= 60 (17) : 2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59",
            "jumeaux : 6 paires",
            "transposee : [[1, 5, 9], [2, 6, 10], [3, 7, 11], [4, 8, 12]]",
            "rotation : [[9, 5, 1], [10, 6, 2], [11, 7, 3], [12, 8, 4]]",
            "spirale : 1 2 3 4 8 12 11 10 9 5 6 7",
            "diagonale : 18 transposee de la transposee identique true",
            "          [1]",
            "         [1, 1]",
            "       [1, 2, 1]",
            "      [1, 3, 3, 1]",
            "    [1, 4, 6, 4, 1]",
            "  [1, 5, 10, 10, 5, 1]",
            "[1, 6, 15, 20, 15, 6, 1]",
            "somme de la derniere ligne : 64 = 2^6 true",
            "labyrinthe : plus court chemin = 23 pas, 45 cases explorees",
            "  S.#******.",
            "  *##*####*#",
            "  ****#...*#",
            "  #.#.#.##**",
            "  ..#...#E#*",
            "  .####.#*#*",
            "  ......#***");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.NUMBERS", "Data.MAZE", "Arrays.copyOf(", "Arrays.sort(",
            "Arrays.equals(", "Arrays.mismatch(", "Arrays.copyOfRange(", "Arrays.deepToString(",
            "Arrays.deepEquals(", "Arrays.fill(", ".clone()", ".toCharArray()",
            "new String(", ".repeat(", "re:new boolean\\[[^\\]]+\\]##crible boolean[]", "re:new int\\[[\\w.]+\\]\\[\\]##tableau irregulier (Pascal)",
            "re:new char\\[\\w+\\]\\[\\]##grille char[][]", "re:\\+ 1\\] - \\w+\\[##somme prefixe p[b + 1] - p[a]", ">>> 1",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "AlgoLab", args, EXPECTED, API);
    }
}
