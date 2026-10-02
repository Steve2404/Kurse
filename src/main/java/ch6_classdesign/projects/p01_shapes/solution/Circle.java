package ch6_classdesign.projects.p01_shapes.solution;

/**
 * SOLUTION - un cercle.
 */
public class Circle extends Shape {

    private final double radius;

    public Circle(double radius) {
        super("cercle");          // l'appel au parent doit etre la PREMIERE instruction
        this.radius = radius;
    }

    // Constructeur sans argument : il delegue au constructeur principal avec this(...).
    public Circle() {
        this(1);
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    protected String extra() {
        return " rayon=" + radius;
    }
}
