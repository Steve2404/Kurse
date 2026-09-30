package ch6_classdesign.drills.solutions;

/**
 * Corrige du drill 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.drills.exercises.Drill03_AbstractAndFinal.
 */
public class SolutionDrill03_AbstractAndFinal {

    abstract static class Shape {
        abstract double area();

        abstract Shape scaled(double factor);

        String name() {
            return getClass().getSimpleName();
        }

        final String describe() {
            // final : les enfants ne changent pas la phrase ; name() et area() sont les leurs.
            return name() + " d'aire " + area();
        }
    }

    static class Circle extends Shape {
        final double radius;

        Circle(double radius) {
            this.radius = radius;
        }

        @Override
        double area() {
            // La classe concrete remplit le trou abstract.
            return Math.PI * radius * radius;
        }

        @Override
        Shape scaled(double factor) {
            // Retourner le type parent (Shape) laisse chaque enfant choisir son vrai type.
            return new Circle(radius * factor);
        }
    }

    static class Square extends Shape {
        final double side;

        Square(double side) {
            this.side = side;
        }

        @Override
        double area() {
            return side * side;
        }

        @Override
        String name() {
            // Redefinir une methode concrete du parent (non final) est permis.
            return "Carre";
        }

        @Override
        Shape scaled(double factor) {
            return new Square(side * factor);
        }
    }

    public static Shape unitShape() {
        // Classe anonyme : une sous-classe concrete sans nom, qui remplit tous les trous.
        return new Shape() {
            @Override
            double area() {
                return 1.0;
            }

            @Override
            Shape scaled(double factor) {
                return this;
            }
        };
    }

    public static double totalArea(Shape... shapes) {
        // Un tableau du type abstrait contient des objets concrets.
        double total = 0;
        for (Shape s : shapes) {
            total += s.area();
        }
        return total;
    }

    public static Shape biggest(Shape... shapes) {
        // area() est resolue sur chaque objet reel.
        Shape best = shapes[0];
        for (Shape s : shapes) {
            if (s.area() > best.area()) {
                best = s;
            }
        }
        return best;
    }

    public static int countSquares(Shape... shapes) {
        // instanceof teste le type reel.
        int count = 0;
        for (Shape s : shapes) {
            if (s instanceof Square) {
                count++;
            }
        }
        return count;
    }
}
