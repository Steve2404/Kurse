package ch6_classdesign.drills.r05_hiding;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall05, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : P C P C/P/C",
            "D02 : P.s C.s P.s C.s",
            "D03 : C.i C.i",
            "D04 : secret de P secret de C",
            "D05 : 1 2 1 2");
            // EXPECTED-END

    static final List<String> API = List.of(
            "extends P", "2xstatic String s()", "2xString name = ", "2xstatic int count",
            "2xprivate String secret()", "super.name", "String readName()",
            // Crescendo : notions des chapitres 7 a 15, interdites au chapitre 6.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!implements ", "!sealed ", "!permits ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat",
            "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!.now()", "!re:\\((?:[A-Z]\\w*)\\)\\s*[\\w(]##cast d objet (chapitre 7)", "!re:(?m)^[ \\t]+(?:(?:public|protected|private|static|final|abstract)\\s+)*class \\w+##classe imbriquee (chapitre 7)",
            "!re:new \\w+\\([^;]*\\)\\s*\\{##classe anonyme (chapitre 7)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall05", args, EXPECTED, API);
    }
}
