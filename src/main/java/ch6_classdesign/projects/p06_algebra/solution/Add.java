package ch6_classdesign.projects.p06_algebra.solution;

/**
 * SOLUTION - l'addition.
 */
public class Add extends BinaryOp {

    public Add(Expr left, Expr right) {
        super(left, right);
    }

    @Override
    protected char symbol() {
        return '+';
    }

    @Override
    public double eval(double x) {
        return left.eval(x) + right.eval(x);
    }

    @Override
    public Expr derive() {
        return new Add(left.derive(), right.derive());
    }

    @Override
    public Expr simplify() {
        Expr l = left.simplify();
        Expr r = right.simplify();
        if (l instanceof Num a && r instanceof Num b) {
            return new Num(a.value() + b.value());
        }
        if (is(l, 0)) {
            return r;
        }
        if (is(r, 0)) {
            return l;
        }
        return new Add(l, r);
    }
}
