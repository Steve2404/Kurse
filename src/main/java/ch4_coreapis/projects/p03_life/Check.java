package ch4_coreapis.projects.p03_life;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Life, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "generation 0 (vivantes : 8)",
            "..........",
            "..#.......",
            "...#...###",
            ".###......",
            "..........",
            "..........",
            "..........",
            "generation 1 (vivantes : 8)",
            "..........",
            "........#.",
            ".#.#....#.",
            "..##....#.",
            "..#.......",
            "..........",
            "..........",
            "generation 2 (vivantes : 8)",
            "..........",
            "..........",
            "...#...###",
            ".#.#......",
            "..##......",
            "..........",
            "..........",
            "generation 3 (vivantes : 8)",
            "..........",
            "........#.",
            "..#.....#.",
            "...##...#.",
            "..##......",
            "..........",
            "..........",
            "generation 4 (vivantes : 8)",
            "..........",
            "..........",
            "...#...###",
            "....#.....",
            "..###.....",
            "..........",
            "..........",
            "clignotant : 1 etape identique false, 2 etapes identique true, Arrays.equals(lignes) false, equals false",
            "ligne 2 apres 1 etape : [false, true, true, true, false], dimensions 5x5");
            // EXPECTED-END

    static final List<String> API = List.of(
            "re:new boolean\\[[^\\]]+\\]\\[[^\\]]+\\]##tableau 2D boolean[][]", "Arrays.deepEquals(", "Arrays.equals(", "Arrays.toString(",
            "re:\\.length\\b(?!\\()##.length d\\'un tableau", "grid[r][c]", "continue;", "re:for \\(boolean\\[\\] \\w+ :##for-each sur les lignes",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Life", args, EXPECTED, API);
    }
}
