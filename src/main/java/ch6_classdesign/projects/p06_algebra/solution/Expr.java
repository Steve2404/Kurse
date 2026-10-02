package ch6_classdesign.projects.p06_algebra.solution;

/**
 * SOLUTION - un noeud d'expression. Chaque sous-classe sait s'evaluer, se deriver, s'afficher, se simplifier.
 */
public abstract class Expr {

    public abstract double eval(double x);

    public abstract Expr derive();

    public abstract String show();

    public abstract int size();

    // Par defaut, une expression est deja simple ; les operateurs redefinissent.
    public Expr simplify() {
        return this;
    }

    // final : toString passe toujours par show(), quelle que soit la sous-classe.
    @Override
    public final String toString() {
        return show();
    }

    // Petit outil partage : un Num vaut-il exactement v ?
    protected static boolean is(Expr e, double v) {
        return e instanceof Num n && n.value() == v;
    }
}
