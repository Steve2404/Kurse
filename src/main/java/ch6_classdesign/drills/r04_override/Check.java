package ch6_classdesign.drills.r04_override;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall04, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : C+B+A B+A A",
            "D02 : 7 7 Integer",
            "D03 : c c dit bonjour c",
            "D04 : C public final de A",
            "D05 : objet C false");
            // EXPECTED-END

    static final List<String> API = List.of(
            "extends A", "extends B", "super.hello()", "Integer value()",
            "Number value()", "String say(String", "public String visible()", "protected String visible()",
            "final String fixed()", "@Override",
            // Crescendo : notions des chapitres 7 a 15, interdites au chapitre 6.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!implements ", "!sealed ", "!permits ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat",
            "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!.now()", "!re:\\((?:[A-Z]\\w*)\\)\\s*[\\w(]##cast d objet (chapitre 7)", "!re:(?m)^[ \\t]+(?:(?:public|protected|private|static|final|abstract)\\s+)*class \\w+##classe imbriquee (chapitre 7)",
            "!re:new \\w+\\([^;]*\\)\\s*\\{##classe anonyme (chapitre 7)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall04", args, EXPECTED, API);
    }
}
