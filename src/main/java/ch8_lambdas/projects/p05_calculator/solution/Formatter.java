package ch8_lambdas.projects.p05_calculator.solution;

/**
 * SOLUTION - un formateur avec un etat (le nombre de decimales). Sa methode format sert de reference
 * "instance d'un objet PRECIS" : fmt::format.
 */
public class Formatter {

    private final int decimals;

    public Formatter(int decimals) {
        this.decimals = decimals;
    }

    public String format(double value) {
        double factor = Math.pow(10, decimals);
        double rounded = Math.round(value * factor) / factor;
        return rounded == Math.rint(rounded) ? String.valueOf((long) rounded) : String.valueOf(rounded);
    }
}
