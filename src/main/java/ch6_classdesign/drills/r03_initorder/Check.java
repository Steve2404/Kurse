package ch6_classdesign.drills.r03_initorder;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall03, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : Ps Cs Pi Pc vu=0 Ci Cc",
            "D02 : Pi Pc vu=0 Ci Cc",
            "D03 : Pi Pc vu=parent",
            "D04 : vu=0 Ci Cc apres=5",
            "D05 : [] K [Os] 7",
            "D06 : [Bs] 3");
            // EXPECTED-END

    static final List<String> API = List.of(
            "4xstatic {", "re:(?m)^\\s+\\{\\s*$##bloc { } d objet", "extends Parent", "extends Base",
            "static final String CONST", "int value = 5",
            // Crescendo : notions des chapitres 7 a 15, interdites au chapitre 6.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!implements ", "!sealed ", "!permits ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat",
            "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!.now()", "!re:\\((?:[A-Z]\\w*)\\)\\s*[\\w(]##cast d objet (chapitre 7)", "!re:(?m)^[ \\t]+(?:(?:public|protected|private|static|final|abstract)\\s+)*class \\w+##classe imbriquee (chapitre 7)",
            "!re:new \\w+\\([^;]*\\)\\s*\\{##classe anonyme (chapitre 7)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall03", args, EXPECTED, API);
    }
}
