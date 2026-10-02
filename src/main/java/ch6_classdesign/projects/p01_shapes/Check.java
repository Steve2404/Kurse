package ch6_classdesign.projects.p01_shapes;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON ShapesApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "cercle aire=12.57 perimetre=12.57 rayon=2.0",
            "cercle aire=3.14 perimetre=6.28 rayon=1.0",
            "rectangle aire=12.0 perimetre=14.0 3.0x4.0",
            "carre aire=9.0 perimetre=12.0 3.0x3.0 (cote 3.0)",
            "triangle aire=6.0 perimetre=12.0",
            "triangle aire=0.0 perimetre=8.0 INVALIDE",
            "par aire : cercle rectangle carre triangle cercle triangle",
            "aire totale 42.71, plus grand perimetre : rectangle",
            "Rectangle r = new Square(5) : carre aire=25.0 perimetre=20.0 5.0x5.0 (cote 5.0) | carre ? true | Square",
            "enveloppe : (0,0)(4,0)(5,1)(4,3)(2,4)(0,3) -> polygone(6) aire=15.5 perimetre=15.12",
            "dedans : (2.0,2.0)=true (5.0,3.0)=false (1.0,3.4)=true (-1.0,1.0)=false",
            "formes creees : 8");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.POINTS", "Data.TESTS", "abstract class Shape", "protected Shape(",
            "public abstract double area()", "public final String describe()", "protected String extra()", "4xextends Shape",
            "extends Rectangle", "super(", "this(", "super.extra()",
            "@Override", "instanceof Square", "getSimpleName()", ".clone()",
            // Crescendo : notions des chapitres 7 a 15, interdites au chapitre 6.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!implements ", "!sealed ", "!permits ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat",
            "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!.now()", "!re:\\((?:[A-Z]\\w*)\\)\\s*[\\w(]##cast d objet (chapitre 7)", "!re:(?m)^[ \\t]+(?:(?:public|protected|private|static|final|abstract)\\s+)*class \\w+##classe imbriquee (chapitre 7)",
            "!re:new \\w+\\([^;]*\\)\\s*\\{##classe anonyme (chapitre 7)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "ShapesApp", args, EXPECTED, API);
    }
}
