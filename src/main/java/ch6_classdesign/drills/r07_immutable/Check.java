package ch6_classdesign.drills.r07_immutable;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 7 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall07, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 20.0C 25.0C 68.0",
            "D02 : [3, 1, 4] 3 1",
            "D03 : true false true",
            "D04 : [3, 1, 4] [3, 1, 9] 4.333333333333333",
            "D05 : final mais modifiable true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "final class Temperature", "final class Series", "private final double", "private final int[]",
            "private Temperature(", "2x.clone()", "Series with(", "public boolean equals(Object",
            "public int hashCode()", "!re:void set\\w*\\(##setter (un objet immuable n en a pas)",
            // Crescendo : notions des chapitres 7 a 15, interdites au chapitre 6.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!implements ", "!sealed ", "!permits ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat",
            "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!.now()", "!re:\\((?:[A-Z]\\w*)\\)\\s*[\\w(]##cast d objet (chapitre 7)", "!re:(?m)^[ \\t]+(?:(?:public|protected|private|static|final|abstract)\\s+)*class \\w+##classe imbriquee (chapitre 7)",
            "!re:new \\w+\\([^;]*\\)\\s*\\{##classe anonyme (chapitre 7)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall07", args, EXPECTED, API);
    }
}
