package ch6_classdesign.projects.p01_shapes.solution;

/**
 * SOLUTION - un rectangle ; Square en herite.
 */
public class Rectangle extends Shape {

    protected final double width;
    protected final double height;

    public Rectangle(double width, double height) {
        this("rectangle", width, height);
    }

    // Constructeur protected : il permet a Square de choisir son propre nom.
    protected Rectangle(String name, double width, double height) {
        super(name);
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public double perimeter() {
        return 2 * (width + height);
    }

    @Override
    protected String extra() {
        return " " + width + "x" + height;
    }
}
