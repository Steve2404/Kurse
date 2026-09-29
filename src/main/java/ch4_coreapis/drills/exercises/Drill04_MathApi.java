package ch4_coreapis.drills.exercises;

import ch4_coreapis.ExerciseChecker;
import ch4_coreapis.drills.Journal;

/**
 * DRILL 04 - L'API Math et ses types de retour : une methode par TODO
 * ===================================================================
 *
 * Mode d'emploi : voir Drill01_StringApi. Regarde bien le TYPE DE RETOUR
 * de chaque methode : c'est la moitie du piege a l'examen.
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : roundUp()           [Math.round(double) rend un long] Math.round(2.5) -> 3.
 * TODO 2  : roundNegative()     [le piege] Math.round(-2.5) -> -2.
 * TODO 3  : roundFloat()        [Math.round(float) rend un int] 7.6f -> 8.
 * TODO 4  : ceilNegative()      [Math.ceil] -1.5 -> -1.0.
 * TODO 5  : floorNegative()     [Math.floor] -1.5 -> -2.0.
 * TODO 6  : power()             [Math.pow rend un double] 2^10 -> 1024.0.
 * TODO 7  : root()              [Math.sqrt] 81 -> 9.0.
 * TODO 8  : mixedMax()          [Math.max(int, double) rend un double] (3, 2.5) -> 3.0.
 * TODO 9  : lowestScore()       [Math.min dans une boucle] sur Journal.SCORES -> 7.
 * TODO 10 : absMinValue()       [le piege Math.abs] Math.abs(Integer.MIN_VALUE) -> -2147483648.
 * TODO 11 : rollDie()           [Math.random] un entier entre 1 et 6 inclus.
 * TODO 12 : roundedAverage()    [Math.round d'une moyenne en double] 226 / 6 = 37.67 -> 38.
 * TODO 13 : checkedSum(a, b)    [Math.addExact] somme, ou -1 si debordement.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   round(float) -> int      round(double) -> long      regle : floor(x + 0.5)
 *   ceil, floor, pow, sqrt, random -> double
 *   min, max, abs : 4 versions (int, long, float, double) ; le plus petit type qui accepte tout
 *   random() dans [0.0, 1.0) : (int) (Math.random() * n) + 1 -> de 1 a n
 *   addExact, multiplyExact, toIntExact : ArithmeticException au lieu de deborder
 * ---------------------------------------------------------------------
 */
public class Drill04_MathApi {

    public static long roundUp() {
        throw new UnsupportedOperationException("TODO 1 : implementer roundUp()");
    }

    public static long roundNegative() {
        throw new UnsupportedOperationException("TODO 2 : implementer roundNegative()");
    }

    public static int roundFloat() {
        throw new UnsupportedOperationException("TODO 3 : implementer roundFloat()");
    }

    public static double ceilNegative() {
        throw new UnsupportedOperationException("TODO 4 : implementer ceilNegative()");
    }

    public static double floorNegative() {
        throw new UnsupportedOperationException("TODO 5 : implementer floorNegative()");
    }

    public static double power() {
        throw new UnsupportedOperationException("TODO 6 : implementer power()");
    }

    public static double root() {
        throw new UnsupportedOperationException("TODO 7 : implementer root()");
    }

    public static double mixedMax() {
        throw new UnsupportedOperationException("TODO 8 : implementer mixedMax()");
    }

    public static int lowestScore() {
        throw new UnsupportedOperationException("TODO 9 : implementer lowestScore()");
    }

    public static int absMinValue() {
        throw new UnsupportedOperationException("TODO 10 : implementer absMinValue()");
    }

    public static int rollDie() {
        throw new UnsupportedOperationException("TODO 11 : implementer rollDie()");
    }

    public static long roundedAverage() {
        throw new UnsupportedOperationException("TODO 12 : implementer roundedAverage()");
    }

    public static int checkedSum(int a, int b) {
        throw new UnsupportedOperationException("TODO 13 : implementer checkedSum()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  roundUp() == 3", roundUp() == 3);
        ExerciseChecker.check("2  roundNegative() == -2", roundNegative() == -2);
        ExerciseChecker.check("3  roundFloat() == 8", roundFloat() == 8);
        ExerciseChecker.check("4  ceilNegative() == -1.0", ceilNegative() == -1.0);
        ExerciseChecker.check("5  floorNegative() == -2.0", floorNegative() == -2.0);
        ExerciseChecker.check("6  power() == 1024.0", power() == 1024.0);
        ExerciseChecker.check("7  root() == 9.0", root() == 9.0);
        ExerciseChecker.check("8  mixedMax() == 3.0", mixedMax() == 3.0);
        ExerciseChecker.check("9  lowestScore() == 7", lowestScore() == 7);
        ExerciseChecker.check("10 absMinValue() == -2147483648", absMinValue() == Integer.MIN_VALUE);
        boolean inRange = true;
        boolean sawOne = false;
        boolean sawSix = false;
        for (int i = 0; i < 2000; i++) {
            int die = rollDie();
            inRange &= die >= 1 && die <= 6;
            sawOne |= die == 1;
            sawSix |= die == 6;
        }
        ExerciseChecker.check("11 rollDie() toujours entre 1 et 6, et 1 et 6 sortent (2000 lancers)", inRange && sawOne && sawSix);
        ExerciseChecker.check("12 roundedAverage() == 38", roundedAverage() == 38);
        ExerciseChecker.check("13 checkedSum : 5 et -1", checkedSum(2, 3) == 5 && checkedSum(Integer.MAX_VALUE, 1) == -1);

        ExerciseChecker.summary();
    }
}
