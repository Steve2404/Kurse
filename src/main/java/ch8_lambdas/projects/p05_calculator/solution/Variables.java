package ch8_lambdas.projects.p05_calculator.solution;

/**
 * SOLUTION - les variables nommees ; lookup sert de reference de methode d'instance (vars::lookup).
 */
public class Variables {

    private final String[] names;
    private final double[] values;

    public Variables(String[] names, double[] values) {
        this.names = names.clone();
        this.values = values.clone();
    }

    public double lookup(String name) {
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(name)) {
                return values[i];
            }
        }
        return Double.NaN;
    }
}
