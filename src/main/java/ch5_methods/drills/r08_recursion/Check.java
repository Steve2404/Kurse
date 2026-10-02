package ch5_methods.drills.r08_recursion;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 8 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall08, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 5050 noisrucer 3",
            "D02 : 9 1073741824 1",
            "D03 : 10 19",
            "D04 : FF 1010 0",
            "D05 : 6 184756",
            "D06 : 3 2 1 0 0 1 2 3");
            // EXPECTED-END

    static final List<String> API = List.of(
            "static int sumTo(", "static String reverse(", "static int countChar(", "static long pow(",
            "static String toBase(", "int[][] memo", "static void countdown(",
            // Crescendo : notions des chapitres 6 a 15, interdites au chapitre 5.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ##extends / heritage (chapitre 6)", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!super##super (chapitre 6)", "!abstract ##abstract (chapitre 6)", "!@Override##@Override (chapitre 6)", "!re:(?m)^\\s*(?:(?:public|protected|private)\\s+)?[A-Z]\\w*\\s*\\([^;{)]*\\)\\s*\\{##constructeur ecrit par toi (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator",
            "!.chars()", "!LocalDate.now()", "!LocalDateTime.now()", "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall08", args, EXPECTED, API);
    }
}
