package ch6_classdesign.projects.p06_algebra.solution;

import ch6_classdesign.projects.p06_algebra.Data;

/**
 * SOLUTION du projet 6 - le calcul formel.
 */
public class Algebra {

    static double r4(double v) {
        return Math.round(v * 10_000) / 10_000.0;
    }

    public static void main(String[] args) {
        for (String text : Data.EXPRESSIONS) {
            Expr f = Parser.parse(text);
            Expr d = f.derive();
            Expr ds = d.simplify();
            System.out.println("f = " + f + " | simplifiee " + f.simplify() + " | f(" + Data.X + ") = " + r4(f.eval(Data.X)));
            System.out.println("  f' = " + ds + " | f'(" + Data.X + ") = " + r4(ds.eval(Data.X)) + " | noeuds " + d.size() + " -> " + ds.size());
        }
        // Newton : x <- x - f(x) / f'(x), 6 iterations.
        Expr f = Parser.parse(Data.NEWTON);
        Expr fp = f.derive().simplify();
        double x = Data.START;
        StringBuilder steps = new StringBuilder("newton " + f + " :");
        for (int i = 0; i < 6; i++) {
            x = x - f.eval(x) / fp.eval(x);
            steps.append(' ').append(r4(x));
        }
        System.out.println(steps + " ; f(x) = " + r4(f.eval(x)));
        Expr e = new Add(new Num(1), new Mul(new Var(), new Num(1)));
        System.out.println("polymorphisme : " + e.getClass().getSimpleName() + " " + e + " -> " + e.simplify() + " (" + e.simplify().getClass().getSimpleName() + ")");
    }
}
