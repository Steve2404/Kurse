package ch6_classdesign.projects.p06_algebra.solution;

/**
 * SOLUTION - la multiplication.
 */
public class Mul extends BinaryOp {

    public Mul(Expr left, Expr right) {
        super(left, right);
    }

    @Override
    protected char symbol() {
        return '*';
    }

    @Override
    public double eval(double x) {
        return left.eval(x) * right.eval(x);
    }

    // (uv)' = u'v + uv'
    @Override
    public Expr derive() {
        return new Add(new Mul(left.derive(), right), new Mul(left, right.derive()));
    }

    @Override
    public Expr simplify() {
        Expr l = left.simplify();
        Expr r = right.simplify();
        if (l instanceof Num a && r instanceof Num b) {
            return new Num(a.value() * b.value());
        }
        if (is(l, 0) || is(r, 0)) {
            return new Num(0);
        }
        if (is(l, 1)) {
            return r;
        }
        if (is(r, 1)) {
            return l;
        }
        return new Mul(l, r);
    }
}
