package ch6_classdesign.projects.p06_algebra.solution;

/**
 * SOLUTION - la variable x.
 */
public class Var extends Expr {

    @Override
    public double eval(double x) {
        return x;
    }

    @Override
    public Expr derive() {
        return new Num(1);
    }

    @Override
    public String show() {
        return "x";
    }

    @Override
    public int size() {
        return 1;
    }
}
