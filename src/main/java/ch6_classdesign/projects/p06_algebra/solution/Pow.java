package ch6_classdesign.projects.p06_algebra.solution;

/**
 * SOLUTION - une puissance entiere. Pas un BinaryOp : l'exposant est un int, pas une Expr.
 */
public class Pow extends Expr {

    private final Expr base;
    private final int exponent;

    public Pow(Expr base, int exponent) {
        this.base = base;
        this.exponent = exponent;
    }

    @Override
    public double eval(double x) {
        return Math.pow(base.eval(x), exponent);
    }

    // (u^n)' = n * u^(n-1) * u'
    @Override
    public Expr derive() {
        return new Mul(new Mul(new Num(exponent), new Pow(base, exponent - 1)), base.derive());
    }

    @Override
    public String show() {
        return "(" + base + " ^ " + exponent + ")";
    }

    @Override
    public int size() {
        return 1 + base.size();
    }

    @Override
    public Expr simplify() {
        Expr b = base.simplify();
        if (exponent == 0) {
            return new Num(1);
        }
        if (exponent == 1) {
            return b;
        }
        if (b instanceof Num n) {
            return new Num(Math.pow(n.value(), exponent));
        }
        return new Pow(b, exponent);
    }
}
