package ch6_classdesign.projects.p06_algebra;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Algebra, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "f = (((3 * (x ^ 2)) + (2 * x)) - 5) | simplifiee (((3 * (x ^ 2)) + (2 * x)) - 5) | f(2.0) = 11.0",
            "  f' = ((3 * (2 * x)) + 2) | f'(2.0) = 14.0 | noeuds 23 -> 7",
            "f = ((x + 1) * (x - 1)) | simplifiee ((x + 1) * (x - 1)) | f(2.0) = 3.0",
            "  f' = ((x - 1) + (x + 1)) | f'(2.0) = 4.0 | noeuds 15 -> 7",
            "f = (((x ^ 3) - (2 * x)) - 5) | simplifiee (((x ^ 3) - (2 * x)) - 5) | f(2.0) = -1.0",
            "  f' = ((3 * (x ^ 2)) - 2) | f'(2.0) = 10.0 | noeuds 16 -> 6",
            "f = (1 / x) | simplifiee (1 / x) | f(2.0) = 0.5",
            "  f' = (-1 / (x ^ 2)) | f'(2.0) = -0.25 | noeuds 10 -> 4",
            "f = ((2 * (x + 0)) * 1) | simplifiee (2 * x) | f(2.0) = 4.0",
            "  f' = 2 | f'(2.0) = 2.0 | noeuds 21 -> 1",
            "newton (((x ^ 3) - (2 * x)) - 5) : 2.1 2.0946 2.0946 2.0946 2.0946 2.0946 ; f(x) = 0.0",
            "polymorphisme : Add (1 + (x * 1)) -> (1 + x) (Add)");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.EXPRESSIONS", "Data.NEWTON", "Data.START", "abstract class Expr",
            "abstract class BinaryOp extends Expr", "4xextends BinaryOp", "public abstract Expr derive()", "public final String toString()",
            "protected abstract char symbol()", "public Expr simplify()", "new Parser(", "private final String text",
            // Crescendo : notions des chapitres 7 a 15, interdites au chapitre 6.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!implements ", "!sealed ", "!permits ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat",
            "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!.now()", "!re:\\((?:[A-Z]\\w*)\\)\\s*[\\w(]##cast d objet (chapitre 7)", "!re:(?m)^[ \\t]+(?:(?:public|protected|private|static|final|abstract)\\s+)*class \\w+##classe imbriquee (chapitre 7)",
            "!re:new \\w+\\([^;]*\\)\\s*\\{##classe anonyme (chapitre 7)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Algebra", args, EXPECTED, API);
    }
}
