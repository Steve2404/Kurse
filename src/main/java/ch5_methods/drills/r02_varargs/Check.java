package ch5_methods.drills.r02_varargs;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall02, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 0 1 3 3 -1",
            "D02 : x x1 y123",
            "D03 : 0 8 6 5",
            "D04 : 0 2 1 null 1 2",
            "D05 : 0 2 3",
            "D06 : 99 1",
            "D07 : a-b-c []",
            "D08 : 0 0 z78");
            // EXPECTED-END

    static final List<String> API = List.of(
            "int... ", "re:\\(String \\w+, int\\.\\.\\. \\w+\\)##parametre fixe puis varargs", "Object... ", "int[]... ",
            "String... ", "(int[]) null", "(Object) null", "(Object[]) null",
            "(Object[]) new String[]",
            // Crescendo : notions des chapitres 6 a 15, interdites au chapitre 5.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ##extends / heritage (chapitre 6)", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!super##super (chapitre 6)", "!abstract ##abstract (chapitre 6)", "!@Override##@Override (chapitre 6)", "!re:(?m)^\\s*(?:(?:public|protected|private)\\s+)?[A-Z]\\w*\\s*\\([^;{)]*\\)\\s*\\{##constructeur ecrit par toi (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator",
            "!.chars()", "!LocalDate.now()", "!LocalDateTime.now()", "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall02", args, EXPECTED, API);
    }
}
