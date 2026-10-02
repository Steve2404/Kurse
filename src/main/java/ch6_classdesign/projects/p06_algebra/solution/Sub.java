package ch6_classdesign.projects.p06_algebra.solution;

/**
 * SOLUTION - la soustraction.
 */
public class Sub extends BinaryOp {

    public Sub(Expr left, Expr right) {
        super(left, right);
    }

    @Override
    protected char symbol() {
        return '-';
    }

    @Override
    public double eval(double x) {
        return left.eval(x) - right.eval(x);
    }

    @Override
    public Expr derive() {
        return new Sub(left.derive(), right.derive());
    }

    @Override
    public Expr simplify() {
        Expr l = left.simplify();
        Expr r = right.simplify();
        if (l instanceof Num a && r instanceof Num b) {
            return new Num(a.value() - b.value());
        }
        if (is(r, 0)) {
            return l;
        }
        return new Sub(l, r);
    }
}
