package ch6_classdesign.drills.exercises;

import ch6_classdesign.ExerciseChecker;

/**
 * DRILL 03 - Classes abstraites, methodes template final, classes anonymes
 * =======================================================================
 *
 * Mode d'emploi : voir Drill01_InheritanceAndConstructors.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : Circle.area()            [remplir un trou abstract] PI * r * r.
 * TODO 2  : Square.area()            [remplir un trou abstract] cote * cote.
 * TODO 3  : Shape.describe()         [methode final qui appelle l'abstract] "Square d'aire 4.0".
 * TODO 4  : Square.name()            [redefinir une methode concrete] "Carre".
 * TODO 5  : unitShape()              [classe anonyme] new Shape() { ... } d'aire 1.0.
 * TODO 6  : totalArea(shapes...)     [varargs du type abstrait] somme des aires.
 * TODO 7  : biggest(shapes...)       [polymorphisme] la forme d'aire maximale.
 * TODO 8  : countSquares(shapes...)  [instanceof] nombre de Square.
 * TODO 9  : Shape.scaled(factor)     [methode abstract qui rend le type parent] le Square de cote 2 * 3 -> aire 36.
 * TODO 10 : Circle.scaled(factor)    [idem pour Circle] rayon * factor.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   abstract class : jamais de new directement ; peut avoir constructeurs, champs, methodes concretes
 *   abstract methode : pas de corps ; interdite avec final, private, static
 *   La 1re classe CONCRETE doit implementer TOUTES les methodes abstract heritees
 *   Une classe abstract peut laisser des trous a ses enfants
 *   new Shape() { double area() { return 1; } } : classe anonyme concrete
 *   final methode : jamais redefinie ; final classe : jamais etendue
 * ---------------------------------------------------------------------
 */
public class Drill03_AbstractAndFinal {

    abstract static class Shape {
        abstract double area();

        abstract Shape scaled(double factor);

        String name() {
            return getClass().getSimpleName();
        }

        final String describe() {
            throw new UnsupportedOperationException("TODO 3 : implementer describe()");
        }
    }

    static class Circle extends Shape {
        final double radius;

        Circle(double radius) {
            this.radius = radius;
        }

        @Override
        double area() {
            throw new UnsupportedOperationException("TODO 1 : implementer Circle.area()");
        }

        @Override
        Shape scaled(double factor) {
            throw new UnsupportedOperationException("TODO 10 : implementer Circle.scaled()");
        }
    }

    static class Square extends Shape {
        final double side;

        Square(double side) {
            this.side = side;
        }

        @Override
        double area() {
            throw new UnsupportedOperationException("TODO 2 : implementer Square.area()");
        }

        @Override
        String name() {
            throw new UnsupportedOperationException("TODO 4 : implementer Square.name()");
        }

        @Override
        Shape scaled(double factor) {
            throw new UnsupportedOperationException("TODO 9 : implementer Square.scaled()");
        }
    }

    public static Shape unitShape() {
        throw new UnsupportedOperationException("TODO 5 : implementer unitShape()");
    }

    public static double totalArea(Shape... shapes) {
        throw new UnsupportedOperationException("TODO 6 : implementer totalArea()");
    }

    public static Shape biggest(Shape... shapes) {
        throw new UnsupportedOperationException("TODO 7 : implementer biggest()");
    }

    public static int countSquares(Shape... shapes) {
        throw new UnsupportedOperationException("TODO 8 : implementer countSquares()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  Circle(1).area() == PI", new Circle(1).area() == Math.PI);
        ExerciseChecker.check("2  Square(2).area() == 4.0", new Square(2).area() == 4.0);
        ExerciseChecker.check("3  describe : Circle d'aire ...", new Circle(1).describe().equals("Circle d'aire " + Math.PI));
        ExerciseChecker.check("4  Square.name() == Carre et describe l'utilise", new Square(2).describe().equals("Carre d'aire 4.0"));
        ExerciseChecker.check("5  unitShape : aire 1.0", unitShape().area() == 1.0);
        ExerciseChecker.check("6  totalArea(Square 2, Square 3) == 13.0", totalArea(new Square(2), new Square(3)) == 13.0);
        Square big = new Square(5);
        ExerciseChecker.check("7  biggest", biggest(new Circle(1), big, new Square(1)) == big);
        ExerciseChecker.check("8  countSquares == 2", countSquares(new Circle(1), big, new Square(1)) == 2);
        ExerciseChecker.check("9  Square(2).scaled(3).area() == 36.0", new Square(2).scaled(3).area() == 36.0);
        ExerciseChecker.check("10 Circle(1).scaled(2) : rayon 2", ((Circle) new Circle(1).scaled(2)).radius == 2.0);

        ExerciseChecker.summary();
    }
}
