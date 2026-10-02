package ch6_classdesign.drills.r02_constructors;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall02, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : Point(int,int) Point() | 0,0",
            "D02 : Point(int,int) Point() Point3D() | 0,0,0",
            "D03 : Point(int,int) Point3D(int,int,int) | 1,2,3",
            "D04 : constructeur methode true",
            "D05 : 100 42 101",
            "D06 : standard 1");
            // EXPECTED-END

    static final List<String> API = List.of(
            "this(0, 0)", "super(x, y)", "this.x = x", "void Weird()",
            "final int number", "private Config(", "static Config defaults()", "extends Point",
            // Crescendo : notions des chapitres 7 a 15, interdites au chapitre 6.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!implements ", "!sealed ", "!permits ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat",
            "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!.now()", "!re:\\((?:[A-Z]\\w*)\\)\\s*[\\w(]##cast d objet (chapitre 7)", "!re:(?m)^[ \\t]+(?:(?:public|protected|private|static|final|abstract)\\s+)*class \\w+##classe imbriquee (chapitre 7)",
            "!re:new \\w+\\([^;]*\\)\\s*\\{##classe anonyme (chapitre 7)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall02", args, EXPECTED, API);
    }
}
