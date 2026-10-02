package ch3_makingdecisions.projects.p03_drawing;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Drawing, ou avec l'argument "solution".
 */
public class Check {

    static final String[] ARGS = {"5"};

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "-- pyramide --",
            "    *",
            "   ***",
            "  *****",
            " *******",
            "*********",
            "-- losange creux --",
            "    *",
            "   * *",
            "  *   *",
            " *     *",
            "*       *",
            " *     *",
            "  *   *",
            "   * *",
            "    *",
            "-- damier --",
            "#.#.#.#.#.",
            ".#.#.#.#.#",
            "#.#.#.#.#.",
            ".#.#.#.#.#",
            "#.#.#.#.#.",
            "-- croix --",
            "\\   /",
            " \\ /",
            "  +",
            " / \\",
            "/   \\",
            "-- table --",
            "   |   1   2   3   4   5",
            "---+--------------------",
            " 1 |   1   2   3   4   5",
            " 2 |   2   4   6   8  10",
            " 3 |   3   6   9  12  15",
            " 4 |   4   8  12  16  20",
            " 5 |   5  10  15  20  25",
            "-- pascal --",
            "             1",
            "           1   1",
            "         1   2   1",
            "       1   3   3   1",
            "     1   4   6   4   1",
            "   1   5  10  10   5   1");
            // EXPECTED-END

    static final List<String> API = List.of(
            "3xfor (", "continue;", "while (", "re:for \\([^;]*;[^;]*;[^)]*\\) \\{\\s*(\\S[^\\n]*\\n\\s*)*?for \\(##boucles imbriquees",
            "? ", "% 2",
            // Crescendo : notions des chapitres 4 a 15, interdites au chapitre 3.
            "!StringBuilder", "!String.format", "!String.valueOf", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!.split(", "!.compareTo(", "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ",
            "!interface ", "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)",
            "!re:case [A-Z]\\w* \\w+ ->##pattern dans un case (preview en Java 17)", "!case null##case null (preview en Java 17)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Drawing", args, ARGS, EXPECTED, API);
    }
}
