package ch7_beyondclasses.drills.solutions;

/**
 * Corrige du drill 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.drills.exercises.Drill01_Interfaces.
 */
public class SolutionDrill01_Interfaces {

    interface Named {
        default String name() {
            return "nomme";
        }
    }

    interface Shape {
        int SIDES_OF_SQUARE = 4;

        double area();

        default String name() {
            return "forme";
        }

        default String describe() {
            // Un default s'appuie sur l'abstract (fourni par la classe) et sur une private.
            return "aire=" + round1(area());
        }

        private double round1(double x) {
            // Aide invisible de l'exterieur, utilisable par les default.
            return Math.round(x * 10) / 10.0;
        }

        static Shape unit() {
            // Fabrique static : s'appelle Shape.unit().
            return new Square(1);
        }
    }

    interface Scorer {
        int score(int points);
    }

    static class Square implements Shape, Named {
        private final double side;

        Square(double side) {
            this.side = side;
        }

        @Override
        public double area() {
            // public obligatoire : la methode d'interface l'est implicitement.
            return side * side;
        }

        @Override
        public String name() {
            // Deux default en conflit : on redefinit et on choisit avec X.super.
            return Shape.super.name() + "/" + Named.super.name();
        }
    }

    public static int sides() {
        // Constante d'interface : public static final.
        return Shape.SIDES_OF_SQUARE;
    }

    public static Scorer doubler() {
        // Une seule methode abstraite : une lambda suffit.
        return points -> points * 2;
    }

    public static Scorer tripler() {
        // La meme chose en classe anonyme.
        return new Scorer() {
            @Override
            public int score(int points) {
                return points * 3;
            }
        };
    }

    public static boolean isShape(Object o) {
        // instanceof marche aussi avec une interface.
        return o instanceof Shape;
    }

    public static double unitArea() {
        // Une methode static d'interface s'appelle par le nom de l'interface.
        return Shape.unit().area();
    }
}
