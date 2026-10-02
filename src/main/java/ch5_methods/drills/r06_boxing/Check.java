package ch5_methods.drills.r06_boxing;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall06, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : true false true true",
            "D02 : 10 5.0 x false",
            "D03 : 42 4 true false",
            "D04 : false true true true",
            "D05 : 12 12 1.5 true false",
            "D06 : 16 11",
            "D07 : 10 1",
            "D08 : 2147483647 -2147483648 -1 8 1010");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Integer ", "Double ", "Character ", "Boolean ",
            "Long ", ".intValue()", "Integer.parseInt(", "Integer.valueOf(",
            "Boolean.parseBoolean(", "Integer.compare(", "Character.getNumericValue(", "Integer[] ",
            // Crescendo : notions des chapitres 6 a 15, interdites au chapitre 5.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ##extends / heritage (chapitre 6)", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!super##super (chapitre 6)", "!abstract ##abstract (chapitre 6)", "!@Override##@Override (chapitre 6)", "!re:(?m)^\\s*(?:(?:public|protected|private)\\s+)?[A-Z]\\w*\\s*\\([^;{)]*\\)\\s*\\{##constructeur ecrit par toi (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator",
            "!.chars()", "!LocalDate.now()", "!LocalDateTime.now()", "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall06", args, EXPECTED, API);
    }
}
