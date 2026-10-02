package ch5_methods.drills.r01_declare;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall01, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 42 2147483648 A -3",
            "D02 : 2",
            "D03 : [1, 2] 4",
            "D04 : + - 0",
            "D05 : r01 3 42",
            "D06 : 5 0",
            "D07 : 2 -1",
            "D08 : 16 10");
            // EXPECTED-END

    static final List<String> API = List.of(
            "final static public String", "static int $count", "static int _twice(", "static long widen(int",
            "static char letter()", "return;", "static int[] pair(", "var ",
            "re:final int \\w+;##final local affecte plus tard", "2xstatic int area(",
            // Crescendo : notions des chapitres 6 a 15, interdites au chapitre 5.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ##extends / heritage (chapitre 6)", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!super##super (chapitre 6)", "!abstract ##abstract (chapitre 6)", "!@Override##@Override (chapitre 6)", "!re:(?m)^\\s*(?:(?:public|protected|private)\\s+)?[A-Z]\\w*\\s*\\([^;{)]*\\)\\s*\\{##constructeur ecrit par toi (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator",
            "!.chars()", "!LocalDate.now()", "!LocalDateTime.now()", "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall01", args, EXPECTED, API);
    }
}
