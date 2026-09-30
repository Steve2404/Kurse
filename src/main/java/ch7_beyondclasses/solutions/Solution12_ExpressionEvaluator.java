package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise12_ExpressionEvaluator.
 */
public class Solution12_ExpressionEvaluator {

    public sealed interface Expr permits Num, Add, Mul, Neg {
    }

    public record Num(int value) implements Expr {
    }

    public record Add(Expr left, Expr right) implements Expr {
    }

    public record Mul(Expr left, Expr right) implements Expr {
    }

    public record Neg(Expr inner) implements Expr {
    }

    public static int eval(Expr e) {
        // sealed : ces 4 cas sont les seuls possibles ; chaque cas s'appelle sur ses enfants.
        if (e instanceof Num n) {
            return n.value();
        }
        if (e instanceof Add a) {
            return eval(a.left()) + eval(a.right());
        }
        if (e instanceof Mul m) {
            return eval(m.left()) * eval(m.right());
        }
        if (e instanceof Neg g) {
            return -eval(g.inner());
        }
        throw new IllegalStateException("type inconnu : " + e);
    }

    public static String show(Expr e) {
        // Des parentheses autour de chaque operation binaire : l'ordre est sans ambiguite.
        if (e instanceof Num n) {
            return String.valueOf(n.value());
        }
        if (e instanceof Add a) {
            return "(" + show(a.left()) + " + " + show(a.right()) + ")";
        }
        if (e instanceof Mul m) {
            return "(" + show(m.left()) + " * " + show(m.right()) + ")";
        }
        if (e instanceof Neg g) {
            return "-" + show(g.inner());
        }
        throw new IllegalStateException("type inconnu : " + e);
    }

    public static Expr simplify(Expr e) {
        // Les enfants d'abord ; les records etant immuables, on construit de nouveaux noeuds.
        if (e instanceof Add a) {
            Expr l = simplify(a.left());
            Expr r = simplify(a.right());
            if (isNum(r, 0)) {
                return l;
            }
            if (isNum(l, 0)) {
                return r;
            }
            return new Add(l, r);
        }
        if (e instanceof Mul m) {
            Expr l = simplify(m.left());
            Expr r = simplify(m.right());
            if (isNum(r, 1)) {
                return l;
            }
            if (isNum(l, 1)) {
                return r;
            }
            return new Mul(l, r);
        }
        if (e instanceof Neg g) {
            Expr inner = simplify(g.inner());
            return inner instanceof Neg double_ ? double_.inner() : new Neg(inner);
        }
        return e;
    }

    public static int countNodes(Expr e) {
        // Chaque noeud compte 1, plus ses enfants.
        if (e instanceof Add a) {
            return 1 + countNodes(a.left()) + countNodes(a.right());
        }
        if (e instanceof Mul m) {
            return 1 + countNodes(m.left()) + countNodes(m.right());
        }
        if (e instanceof Neg g) {
            return 1 + countNodes(g.inner());
        }
        return 1;
    }

    static boolean isNum(Expr e, int value) {
        return e instanceof Num n && n.value() == value;
    }
}
