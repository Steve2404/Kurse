package ch6_classdesign.projects.p06_algebra.solution;

/**
 * SOLUTION - une classe abstraite INTERMEDIAIRE : elle implemente show() et size() pour tous les operateurs
 * a deux operandes, et laisse eval(), derive() et symbol() aux classes concretes.
 */
public abstract class BinaryOp extends Expr {

    protected final Expr left;
    protected final Expr right;

    protected BinaryOp(Expr left, Expr right) {
        this.left = left;
        this.right = right;
    }

    protected abstract char symbol();

    // Une classe abstraite peut implementer une methode abstraite de son parent.
    @Override
    public String show() {
        return "(" + left + " " + symbol() + " " + right + ")";
    }

    @Override
    public int size() {
        return 1 + left.size() + right.size();
    }
}
