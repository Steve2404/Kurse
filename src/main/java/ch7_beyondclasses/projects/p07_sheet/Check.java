package ch7_beyondclasses.projects.p07_sheet;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 7 (capstone) (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON SheetApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "arbre de B3 : Binary[op=PLUS, left=Binary[op=DIVIDE, left=Cell[ref=B2], right=Cell[ref=A1]], right=Cell[ref=B1]]",
            "cellules : 3 nombres, 1 texte, 8 formules ; operateurs 4 ; TIMES.apply(6, 7) = 42.0",
            "ordre de calcul : B1 B2 B3 C3 C2 D3",
            "   A        B        C        D",
            "1  10       20       Total    #CYCLE",
            "2  20       60       -2       #CYCLE",
            "3  30       26       110      -2",
            "apres A1=5 :",
            "   A        B        C        D",
            "1  5        10       Total    #CYCLE",
            "2  20       55       -4.5     #CYCLE",
            "3  30       21       90       -4.5");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.CELLS", "Data.CHANGE", "record Ref(", "enum Op",
            "public abstract double apply(", "sealed interface Expr", "record Binary(", "sealed interface Content permits",
            "static class Parser", "class Evaluator", "Sheet.this.values", "class Graph",
            "new Sheet.CellVisitor()", "new Sheet.Parser(",
            // Crescendo : notions des chapitres 8 a 15, interdites au chapitre 7.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:\\(\\s*\\)\\s*->|\\(\\w+(?:\\s*,\\s*\\w+)*\\)\\s*->|[=(]\\s*\\w+\\s*->##lambda (chapitre 8)", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "SheetApp", args, EXPECTED, API);
    }
}
