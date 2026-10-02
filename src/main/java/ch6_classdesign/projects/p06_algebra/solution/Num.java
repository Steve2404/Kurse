package ch6_classdesign.projects.p06_algebra.solution;

/**
 * SOLUTION - une constante.
 */
public class Num extends Expr {

    private final double value;

    public Num(double value) {
        this.value = value;
    }

    public double value() {
        return value;
    }

    @Override
    public double eval(double x) {
        return value;
    }

    @Override
    public Expr derive() {
        return new Num(0);
    }

    @Override
    public String show() {
        return value == Math.rint(value) ? String.valueOf((long) value) : String.valueOf(value);
    }

    @Override
    public int size() {
        return 1;
    }
}
