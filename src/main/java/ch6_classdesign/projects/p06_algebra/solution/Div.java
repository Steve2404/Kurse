package ch6_classdesign.projects.p06_algebra.solution;

/**
 * SOLUTION - la division.
 */
public class Div extends BinaryOp {

    public Div(Expr left, Expr right) {
        super(left, right);
    }

    @Override
    protected char symbol() {
        return '/';
    }

    @Override
    public double eval(double x) {
        return left.eval(x) / right.eval(x);
    }

    // (u/v)' = (u'v - uv') / v^2
    @Override
    public Expr derive() {
        return new Div(new Sub(new Mul(left.derive(), right), new Mul(left, right.derive())), new Pow(right, 2));
    }

    @Override
    public Expr simplify() {
        Expr l = left.simplify();
        Expr r = right.simplify();
        if (is(l, 0)) {
            return new Num(0);
        }
        if (is(r, 1)) {
            return l;
        }
        return new Div(l, r);
    }
}
