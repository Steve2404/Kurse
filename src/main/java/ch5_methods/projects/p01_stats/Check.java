package ch5_methods.projects.p01_stats;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON StatsReport, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "somme : liste 6, tableau 149, rien 0",
            "compte : 10 0 0 -1",
            "moyenne : 14.64 (piege : le premier compte deux fois) 5.0 5.0",
            "moyenne juste : 14.9, max 25, max d'un seul -3",
            "mediane : 15.0 2.0, original intact [12, 15, 9, 22, 18, 15, 7, 25, 15, 11]",
            "modes : [15] [1, 2]",
            "k-iemes : 1er 7, 3e 11, 10e 25, original intact true",
            "moyenne glissante (3) : [12.0, 15.33, 16.33, 18.33, 13.33, 15.67, 15.67, 17.0]",
            "concat : [3, 8, 1, 9, 4] 0",
            "join : a, b, c |  | lun/mar/mer/jeu/ven",
            "describe : 4 element(s) : 1 deux 3.0 c | 0 element(s) : | 1 element(s) : null | tableau null | true",
            " 5 |     ##",
            " 4 |     ##      ##",
            " 3 | ##  ##      ##",
            " 2 | ##  ##      ##  ##",
            " 1 | ##  ##  ##  ##  ##",
            "   +--------------------",
            "    lun mar mer jeu ven",
            "histogramme impossible : 5 etiquettes pour 2 valeurs");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.TEMPS", "Data.LABELS", "re:\\(\\w+ \\w+, int\\.\\.\\. \\w+\\)##parametre fixe puis varargs", "int[]... ",
            "Object... ", "String... ", "(int[]) null", "(Object[]) null",
            "(Object) null", "private static", "return;", "Arrays.copyOf(",
            ".clone()", "re:static double\\[\\] ##methode qui rend un double[]", "re:static int\\[\\] ##methode qui rend un int[]", "String.format(",
            // Crescendo : notions des chapitres 6 a 15, interdites au chapitre 5.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ##extends / heritage (chapitre 6)", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!super##super (chapitre 6)", "!abstract ##abstract (chapitre 6)", "!@Override##@Override (chapitre 6)", "!re:(?m)^\\s*(?:(?:public|protected|private)\\s+)?[A-Z]\\w*\\s*\\([^;{)]*\\)\\s*\\{##constructeur ecrit par toi (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator",
            "!.chars()", "!LocalDate.now()", "!LocalDateTime.now()", "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "StatsReport", args, EXPECTED, API);
    }
}
