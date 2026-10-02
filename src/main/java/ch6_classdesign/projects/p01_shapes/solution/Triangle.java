package ch6_classdesign.projects.p01_shapes.solution;

/**
 * SOLUTION - un triangle donne par ses trois cotes (formule de Heron).
 */
public class Triangle extends Shape {

    private final double a;
    private final double b;
    private final double c;
    private final boolean valid;

    public Triangle(double a, double b, double c) {
        super("triangle");
        this.a = a;
        this.b = b;
        this.c = c;
        // Inegalite triangulaire : chaque cote est plus petit que la somme des deux autres.
        valid = a + b > c && a + c > b && b + c > a;
    }

    @Override
    public double area() {
        if (!valid) {
            return 0;
        }
        double s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    @Override
    public double perimeter() {
        return a + b + c;
    }

    @Override
    protected String extra() {
        return valid ? "" : " INVALIDE";
    }
}
