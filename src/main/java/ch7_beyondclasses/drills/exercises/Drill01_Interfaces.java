package ch7_beyondclasses.drills.exercises;

import ch7_beyondclasses.ExerciseChecker;

/**
 * DRILL 01 - Interfaces : abstract, default, static, private, constantes, diamant
 * ==============================================================================
 *
 * -- Comment utiliser un DRILL (different d'un exercice) --
 *
 * Un exercice t'APPREND une notion. Un drill te la fait REPETER jusqu'a
 * ce qu'elle sorte toute seule. Chaque TODO tient en quelques lignes et
 * vise UNE forme precise (entre crochets).
 *
 *   1. Chronometre-toi, note ton temps et ton score dans drills/REVISION.md.
 *   2. Ecris SANS regarder la "carte memoire" en bas. Bloque plus d'une
 *      minute : regarde-la, cache-la, reecris.
 *   3. Refais le MEME drill plus tard, a partir de zero (voir REVISION.md).
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : Square.area()            [implementer une methode abstract : public obligatoire] cote * cote.
 * TODO 2  : Shape.describe()         [default qui appelle l'abstract] "aire=" + round1(area()).
 * TODO 3  : Shape.round1(x)          [private d'interface] arrondi a 1 decimale : Math.round(x * 10) / 10.0.
 * TODO 4  : Shape.unit()             [static d'interface = fabrique] un Square de cote 1.
 * TODO 5  : Square.name()            [resoudre un diamant] Shape.name() et Named.name() sont deux default :
 *                                     rendre Shape.super.name() + "/" + Named.super.name() -> "forme/nomme".
 * TODO 6  : sides()                  [constante d'interface] rendre Shape.SIDES_OF_SQUARE -> 4.
 * TODO 7  : doubler()                [lambda pour une interface a une methode] un Scorer qui double -> score(21) == 42.
 * TODO 8  : tripler()                [classe anonyme] le meme Scorer, en anonyme, qui triple.
 * TODO 9  : isShape(o)               [instanceof sur une interface]
 * TODO 10 : unitArea()               [appel static par le nom de l'interface] Shape.unit().area() -> 1.0.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   champ           : public static final (valeur obligatoire)
 *   abstract        : public abstract, pas de corps ; l'implementation doit etre public
 *   default         : public, corps obligatoire ; peut appeler abstract, private, static
 *   static          : public static, s'appelle I.m() ; PAS heritee par les classes
 *   private         : corps obligatoire ; aide les default (private static aide les static)
 *   Diamant : deux default de meme signature -> la classe redefinit ; A.super.m() choisit
 * ---------------------------------------------------------------------
 */
public class Drill01_Interfaces {

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
            throw new UnsupportedOperationException("TODO 2 : implementer describe()");
        }

        private double round1(double x) {
            throw new UnsupportedOperationException("TODO 3 : implementer round1()");
        }

        static Shape unit() {
            throw new UnsupportedOperationException("TODO 4 : implementer unit()");
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
            throw new UnsupportedOperationException("TODO 1 : implementer area()");
        }

        @Override
        public String name() {
            throw new UnsupportedOperationException("TODO 5 : implementer name()");
        }
    }

    public static int sides() {
        throw new UnsupportedOperationException("TODO 6 : implementer sides()");
    }

    public static Scorer doubler() {
        throw new UnsupportedOperationException("TODO 7 : implementer doubler()");
    }

    public static Scorer tripler() {
        throw new UnsupportedOperationException("TODO 8 : implementer tripler()");
    }

    public static boolean isShape(Object o) {
        throw new UnsupportedOperationException("TODO 9 : implementer isShape()");
    }

    public static double unitArea() {
        throw new UnsupportedOperationException("TODO 10 : implementer unitArea()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  Square(3).area() == 9.0", new Square(3).area() == 9.0);
        ExerciseChecker.check("2  describe : aire=2.3", new Square(1.5).describe().equals("aire=2.3"));
        ExerciseChecker.check("3  round1 (via describe) : aire=9.0", new Square(3).describe().equals("aire=9.0"));
        ExerciseChecker.check("4  Shape.unit() est un Square", Shape.unit() instanceof Square);
        ExerciseChecker.check("5  diamant : forme/nomme", new Square(1).name().equals("forme/nomme"));
        ExerciseChecker.check("6  sides() == 4", sides() == 4);
        ExerciseChecker.check("7  doubler().score(21) == 42", doubler().score(21) == 42);
        ExerciseChecker.check("8  tripler().score(5) == 15", tripler().score(5) == 15);
        ExerciseChecker.check("9  isShape : Square oui, String non", isShape(new Square(1)) && !isShape("carre"));
        ExerciseChecker.check("10 unitArea() == 1.0", unitArea() == 1.0);

        ExerciseChecker.summary();
    }
}
