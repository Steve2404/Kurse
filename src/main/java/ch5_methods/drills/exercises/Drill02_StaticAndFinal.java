package ch5_methods.drills.exercises;

import ch5_methods.ExerciseChecker;
import ch5_methods.drills.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;

import static java.lang.Math.PI;
import static java.lang.Math.sqrt;

/**
 * DRILL 02 - static, imports static, final et effectivement final
 * ===============================================================
 *
 * Mode d'emploi : voir Drill01_DeclarationsAndVarargs. Les imports static
 * de PI et sqrt sont deja en haut du fichier : utilise-les SANS "Math.".
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : nextId()               [compteur static] ++counter : 1, puis 2...
 * TODO 2  : resetCounter()         [ecrire un champ static] counter = 0.
 * TODO 3  : maxPlayers()           [constante static final d'une autre classe] Team.MAX_PLAYERS -> 4.
 * TODO 4  : circleArea(r)          [import static PI] PI * r * r.
 * TODO 5  : hypotenuse(a, b)       [import static sqrt] (3, 4) -> 5.0.
 * TODO 6  : badge(name)            [fabrique static d'une classe au constructeur private] Badge.of(name).text() -> "[Ada]".
 * TODO 7  : kindThroughNull()      [appel static via une variable null] -> "badge" (pas d'exception).
 * TODO 8  : finalArrayContent()    [final sur une reference] final int[] a = {1, 2} ; a[0] = 9 ; -> 9.
 * TODO 9  : addBase(base)          [lambda qui capture un parametre] rend x -> x + base ; addBase(5).applyAsInt(10) -> 15.
 * TODO 10 : copiesInLoop()         [copie par tour de boucle] 4 lambdas qui rendent 0, 1, 2, 3 ; somme -> 6.
 * TODO 11 : counterInLambda()      [tableau d'une case] un Runnable qui fait box[0]++ lance 3 fois -> 3.
 * TODO 12 : clubUpper()            [static final + methode d'instance de String] Team.CLUB.toUpperCase() -> "JAVA CLUB".
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   static : a la classe (un seul exemplaire) ; pas de this dans une methode static
 *   static -> instance : il faut un OBJET (new C().im()) ; instance -> static : toujours permis
 *   C.im() sur une methode d'instance : non-static method im() cannot be referenced from a static context
 *   ((C) null).staticMethod() : marche (seul le TYPE compte)
 *   import static java.lang.Math.PI; (jamais "static import", jamais une classe entiere)
 *   final variable : une seule affectation ; final reference : l'OBJET reste modifiable
 *   Lambda : capture seulement des variables final ou effectivement final (jamais reassignees)
 * ---------------------------------------------------------------------
 */
public class Drill02_StaticAndFinal {

    private static int counter;

    public static final class Badge {
        private final String name;

        private Badge(String name) {
            this.name = name;
        }

        public static Badge of(String name) {
            return new Badge(name);
        }

        public static String kind() {
            return "badge";
        }

        public String text() {
            return "[" + name + "]";
        }
    }

    public static int nextId() {
        throw new UnsupportedOperationException("TODO 1 : implementer nextId()");
    }

    public static void resetCounter() {
        throw new UnsupportedOperationException("TODO 2 : implementer resetCounter()");
    }

    public static int maxPlayers() {
        throw new UnsupportedOperationException("TODO 3 : implementer maxPlayers()");
    }

    public static double circleArea(double r) {
        throw new UnsupportedOperationException("TODO 4 : implementer circleArea()");
    }

    public static double hypotenuse(double a, double b) {
        throw new UnsupportedOperationException("TODO 5 : implementer hypotenuse()");
    }

    public static String badge(String name) {
        throw new UnsupportedOperationException("TODO 6 : implementer badge()");
    }

    public static String kindThroughNull() {
        throw new UnsupportedOperationException("TODO 7 : implementer kindThroughNull()");
    }

    public static int finalArrayContent() {
        throw new UnsupportedOperationException("TODO 8 : implementer finalArrayContent()");
    }

    public static IntUnaryOperator addBase(int base) {
        throw new UnsupportedOperationException("TODO 9 : implementer addBase()");
    }

    public static int copiesInLoop() {
        throw new UnsupportedOperationException("TODO 10 : implementer copiesInLoop()");
    }

    public static int counterInLambda() {
        throw new UnsupportedOperationException("TODO 11 : implementer counterInLambda()");
    }

    public static String clubUpper() {
        throw new UnsupportedOperationException("TODO 12 : implementer clubUpper()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  nextId : 1 puis 2", nextId() == 1 && nextId() == 2);
        resetCounter();
        ExerciseChecker.check("2  resetCounter : on repart de 1", nextId() == 1);
        ExerciseChecker.check("3  maxPlayers() == 4", maxPlayers() == 4);
        ExerciseChecker.check("4  circleArea(1) == PI", circleArea(1) == Math.PI);
        ExerciseChecker.check("5  hypotenuse(3, 4) == 5.0", hypotenuse(3, 4) == 5.0);
        ExerciseChecker.check("6  badge(\"Ada\") == \"[Ada]\"", badge("Ada").equals("[Ada]"));
        ExerciseChecker.check("7  kindThroughNull() == \"badge\"", kindThroughNull().equals("badge"));
        ExerciseChecker.check("8  finalArrayContent() == 9", finalArrayContent() == 9);
        ExerciseChecker.check("9  addBase(5).applyAsInt(10) == 15", addBase(5).applyAsInt(10) == 15);
        ExerciseChecker.check("10 copiesInLoop() == 6", copiesInLoop() == 6);
        ExerciseChecker.check("11 counterInLambda() == 3", counterInLambda() == 3);
        ExerciseChecker.check("12 clubUpper() == \"JAVA CLUB\"", clubUpper().equals("JAVA CLUB"));

        ExerciseChecker.summary();
    }
}
