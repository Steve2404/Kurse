package ch5_methods.drills.r09_kata;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 9 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall09, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 0 3 12 3",
            "D02 : long Integer Object long",
            "D03 : [1] [5]",
            "D04 : 12 3",
            "D05 : [-2, 9] [3, 3]",
            "D06 : false true true",
            "D07 : kata 4");
            // EXPECTED-END

    static final List<String> API = List.of(
            "int... ", "static {", "import static", "static String kind(long",
            "static String kind(Integer", "static String kind(Object", "re:\\(int \\w+, int\\.\\.\\. \\w+\\)##parametre fixe puis varargs", "static int gcd(",
            // Crescendo : notions des chapitres 6 a 15, interdites au chapitre 5.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ##extends / heritage (chapitre 6)", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!super##super (chapitre 6)", "!abstract ##abstract (chapitre 6)", "!@Override##@Override (chapitre 6)", "!re:(?m)^\\s*(?:(?:public|protected|private)\\s+)?[A-Z]\\w*\\s*\\([^;{)]*\\)\\s*\\{##constructeur ecrit par toi (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator",
            "!.chars()", "!LocalDate.now()", "!LocalDateTime.now()", "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall09", args, EXPECTED, API);
    }
}
