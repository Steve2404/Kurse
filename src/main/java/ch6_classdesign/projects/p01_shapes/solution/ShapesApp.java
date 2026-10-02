package ch6_classdesign.projects.p01_shapes.solution;

import ch6_classdesign.projects.p01_shapes.Data;

/**
 * SOLUTION du projet 1 - les formes.
 */
public class ShapesApp {

    public static void main(String[] args) {
        Shape[] shapes = {new Circle(2), new Circle(), new Rectangle(3, 4), new Square(3), new Triangle(3, 4, 5), new Triangle(1, 2, 5)};
        // Un tableau de Shape : chaque appel a describe() execute les methodes de l'OBJET reel.
        for (Shape s : shapes) {
            System.out.println(s.describe());
        }
        for (int i = 1; i < shapes.length; i++) {      // tri par insertion, aire decroissante
            Shape key = shapes[i];
            int j = i - 1;
            while (j >= 0 && shapes[j].area() < key.area()) {
                shapes[j + 1] = shapes[j];
                j--;
            }
            shapes[j + 1] = key;
        }
        StringBuilder order = new StringBuilder("par aire :");
        double total = 0;
        Shape longest = shapes[0];
        for (Shape s : shapes) {
            order.append(' ').append(s.getName());
            total += s.area();
            if (s.perimeter() > longest.perimeter()) {
                longest = s;
            }
        }
        System.out.println(order);
        System.out.println("aire totale " + Shape.r2(total) + ", plus grand perimetre : " + longest.getName());

        Rectangle r = new Square(5);
        System.out.println("Rectangle r = new Square(5) : " + r + " | carre ? " + (r instanceof Square) + " | " + r.getClass().getSimpleName());

        Polygon hull = Polygon.convexHull(Data.POINTS);
        System.out.println("enveloppe : " + hull.vertices() + " -> " + hull);
        StringBuilder tests = new StringBuilder("dedans :");
        for (double[] t : Data.TESTS) {
            tests.append(" (").append(t[0]).append(',').append(t[1]).append(")=").append(hull.contains(t[0], t[1]));
        }
        System.out.println(tests);
        System.out.println("formes creees : " + Shape.created());
    }
}
