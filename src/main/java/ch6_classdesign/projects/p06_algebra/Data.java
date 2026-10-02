package ch6_classdesign.projects.p06_algebra;

/**
 * Les donnees du projet 6 (DONNEES, ne pas modifier).
 * Grammaire : + - * / ^ (exposant entier), parentheses, nombres, et la variable x.
 */
public final class Data {

    public static final String[] EXPRESSIONS = {"3*x^2 + 2*x - 5", "(x+1)*(x-1)", "x^3 - 2*x - 5", "1/x", "2*(x+0)*1"};

    /** La valeur de x pour evaluer f et f'. */
    public static final double X = 2;

    /** L'equation a resoudre par Newton (f(x) = 0) et le point de depart. */
    public static final String NEWTON = "x^3 - 2*x - 5";
    public static final double START = 2;

    private Data() {
    }
}
