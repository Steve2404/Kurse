package ch6_classdesign.drills.r06_abstract;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall06, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : guitare:~~~~~~ violon:~~~~~~~~",
            "D02 : tambour:boumboumboum",
            "D03 : guitare tambour violon 10",
            "D04 : 6 orchestre de 6",
            "D05 : true true Violin",
            "D06 : marque par Concrete");
            // EXPECTED-END

    static final List<String> API = List.of(
            "abstract class Instrument", "abstract class Strings extends Instrument", "abstract class Marker", "abstract String sound()",
            "abstract int strings()", "protected Instrument(", "static String describeAll()", "extends Marker",
            "instanceof Strings",
            // Crescendo : notions des chapitres 7 a 15, interdites au chapitre 6.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!implements ", "!sealed ", "!permits ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat",
            "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!.now()", "!re:\\((?:[A-Z]\\w*)\\)\\s*[\\w(]##cast d objet (chapitre 7)", "!re:(?m)^[ \\t]+(?:(?:public|protected|private|static|final|abstract)\\s+)*class \\w+##classe imbriquee (chapitre 7)",
            "!re:new \\w+\\([^;]*\\)\\s*\\{##classe anonyme (chapitre 7)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall06", args, EXPECTED, API);
    }
}
