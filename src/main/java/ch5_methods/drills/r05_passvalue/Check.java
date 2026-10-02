package ch5_methods.drills.r05_passvalue;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall05, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 1 2",
            "D02 : 2",
            "D03 : x+ y",
            "D04 : hey HEY",
            "D05 : [1, 2, 3] [2, 4, 6]",
            "D06 : [[7, 7], [7, 7]]",
            "D07 : [2, 2, 30]",
            "D08 : a++ z");
            // EXPECTED-END

    static final List<String> API = List.of(
            "static void bump(int ", "static void bump(int[] ", "static void replace(", "static void swapRefs(",
            "static void shout(", "static String shouted(", ".clone()", "Arrays.deepToString(",
            // Crescendo : notions des chapitres 6 a 15, interdites au chapitre 5.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ##extends / heritage (chapitre 6)", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!super##super (chapitre 6)", "!abstract ##abstract (chapitre 6)", "!@Override##@Override (chapitre 6)", "!re:(?m)^\\s*(?:(?:public|protected|private)\\s+)?[A-Z]\\w*\\s*\\([^;{)]*\\)\\s*\\{##constructeur ecrit par toi (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator",
            "!.chars()", "!LocalDate.now()", "!LocalDateTime.now()", "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall05", args, EXPECTED, API);
    }
}
