package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans beyondclasses.exercises.Exercise06_SealedClasses.
 */
public class Solution06_SealedClasses {

    sealed interface Shape permits Circle, Square, Rectangle {
    }

    record Circle(double radius) implements Shape {
    }

    record Square(double side) implements Shape {
    }

    record Rectangle(double width, double height) implements Shape {
    }

    public static double areaOf(Shape shape) {
        if (shape instanceof Circle c) {
            return Math.PI * c.radius() * c.radius();
        } else if (shape instanceof Square s) {
            return s.side() * s.side();
        } else if (shape instanceof Rectangle r) {
            return r.width() * r.height();
        }
        return 0;
    }
}
