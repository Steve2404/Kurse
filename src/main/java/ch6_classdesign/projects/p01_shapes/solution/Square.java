package ch6_classdesign.projects.p01_shapes.solution;

/**
 * SOLUTION - un carre EST un rectangle : il herite de area() et perimeter() sans les reecrire.
 */
public class Square extends Rectangle {

    public Square(double side) {
        super("carre", side, side);
    }

    // super.extra() appelle la version du PARENT, puis on complete.
    @Override
    protected String extra() {
        return super.extra() + " (cote " + width + ")";
    }
}
