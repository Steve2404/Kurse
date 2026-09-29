package ch2_operators.drills.exercises;

import ch2_operators.ExerciseChecker;
import ch2_operators.drills.Grades;

/**
 * DRILL 03 - Logique et bits : && || ! ^, court-circuit, masques & | ^ ~, decalages << >> >>> (projet bulletin)
 * ============================================================================================================
 *
 * Mode d'emploi : voir Drill01_ArithmeticAndPromotion.
 *
 *
 * -- Les TODO (operateur vise entre crochets) --
 *
 * TODO 1  : both(a, b)              [&&]
 * TODO 2  : either(a, b)            [||]
 * TODO 3  : exactlyOne(a, b)        [^ sur des boolean]
 * TODO 4  : opposite(a)             [!]
 * TODO 5  : safeRatioAbove(a, b, n) [&& garde du corps] b != 0 ET a / b > n ; (5, 0, 1) -> false sans exception.
 * TODO 6  : hasOption(options, o)   [(x & o) != 0] Grades.OPTIONS a EXEMPT ? oui ; LATE ? non.
 * TODO 7  : addOption(options, o)   [|]
 * TODO 8  : removeOption(options, o)[& ~]
 * TODO 9  : flipOption(options, o)  [^ sur des int]
 * TODO 10 : shiftLeft(x, n)         [<<] 3 et 2 -> 12.
 * TODO 11 : shiftRight(x, n)        [>> garde le signe] -16 et 2 -> -4.
 * TODO 12 : shiftRightUnsigned(x, n)[>>> fait entrer des 0] -16 et 28 -> 15.
 * TODO 13 : isOdd(n)                [n & 1] -3 -> true, 4 -> false.
 * TODO 14 : lowestBit(n)            [n & -n] 12 (1100) -> 4.
 * TODO 15 : complement(x)           [~] 5 -> -6.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   && || : court-circuit (le cote droit peut ne pas etre evalue) ; & | ^ : les 2 cotes toujours
 *   Sur des int : & | ^ ~ bit par bit ; donner (x | o), retirer (x & ~o), tester ((x & o) != 0),
 *                basculer (x ^ o) ; ~x == -(x + 1)
 *   << : * 2^n ; >> : garde le signe (arrondi vers le bas) ; >>> : 0 a gauche
 *   Precedence : == et != passent AVANT & ^ | : parentheses obligatoires dans (x & o) != 0
 * ---------------------------------------------------------------------
 */
public class Drill03_LogicAndBits {

    public static boolean both(boolean a, boolean b) {
        throw new UnsupportedOperationException("TODO 1 : implementer both()");
    }

    public static boolean either(boolean a, boolean b) {
        throw new UnsupportedOperationException("TODO 2 : implementer either()");
    }

    public static boolean exactlyOne(boolean a, boolean b) {
        throw new UnsupportedOperationException("TODO 3 : implementer exactlyOne()");
    }

    public static boolean opposite(boolean a) {
        throw new UnsupportedOperationException("TODO 4 : implementer opposite()");
    }

    public static boolean safeRatioAbove(int a, int b, int n) {
        throw new UnsupportedOperationException("TODO 5 : implementer safeRatioAbove()");
    }

    public static boolean hasOption(int options, int option) {
        throw new UnsupportedOperationException("TODO 6 : implementer hasOption()");
    }

    public static int addOption(int options, int option) {
        throw new UnsupportedOperationException("TODO 7 : implementer addOption()");
    }

    public static int removeOption(int options, int option) {
        throw new UnsupportedOperationException("TODO 8 : implementer removeOption()");
    }

    public static int flipOption(int options, int option) {
        throw new UnsupportedOperationException("TODO 9 : implementer flipOption()");
    }

    public static int shiftLeft(int x, int n) {
        throw new UnsupportedOperationException("TODO 10 : implementer shiftLeft()");
    }

    public static int shiftRight(int x, int n) {
        throw new UnsupportedOperationException("TODO 11 : implementer shiftRight()");
    }

    public static int shiftRightUnsigned(int x, int n) {
        throw new UnsupportedOperationException("TODO 12 : implementer shiftRightUnsigned()");
    }

    public static boolean isOdd(int n) {
        throw new UnsupportedOperationException("TODO 13 : implementer isOdd()");
    }

    public static int lowestBit(int n) {
        throw new UnsupportedOperationException("TODO 14 : implementer lowestBit()");
    }

    public static int complement(int x) {
        throw new UnsupportedOperationException("TODO 15 : implementer complement()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  both(true, false) == false", !both(true, false) && both(true, true));
        ExerciseChecker.check("2  either(true, false) == true", either(true, false) && !either(false, false));
        ExerciseChecker.check("3  exactlyOne(true, false) && !exactlyOne(true, true)", exactlyOne(true, false) && !exactlyOne(true, true));
        ExerciseChecker.check("4  opposite(true) == false", !opposite(true));
        ExerciseChecker.check("5  safeRatioAbove(10, 2, 3) oui ; (5, 0, 1) non sans exception",
                safeRatioAbove(10, 2, 3) && !safeRatioAbove(5, 0, 1));
        ExerciseChecker.check("6  Grades.OPTIONS a EXEMPT mais pas LATE",
                hasOption(Grades.OPTIONS, Grades.EXEMPT) && !hasOption(Grades.OPTIONS, Grades.LATE));
        ExerciseChecker.check("7  addOption(5, LATE) == 7", addOption(Grades.OPTIONS, Grades.LATE) == 7);
        ExerciseChecker.check("8  removeOption(5, ABSENT) == 4", removeOption(Grades.OPTIONS, Grades.ABSENT) == 4);
        ExerciseChecker.check("9  flipOption(5, EXEMPT) == 1", flipOption(Grades.OPTIONS, Grades.EXEMPT) == 1);
        ExerciseChecker.check("10 shiftLeft(3, 2) == 12", shiftLeft(3, 2) == 12);
        ExerciseChecker.check("11 shiftRight(-16, 2) == -4", shiftRight(-16, 2) == -4);
        ExerciseChecker.check("12 shiftRightUnsigned(-16, 28) == 15", shiftRightUnsigned(-16, 28) == 15);
        ExerciseChecker.check("13 isOdd(-3) && !isOdd(4)", isOdd(-3) && !isOdd(4));
        ExerciseChecker.check("14 lowestBit(12) == 4", lowestBit(12) == 4);
        ExerciseChecker.check("15 complement(5) == -6", complement(5) == -6);

        ExerciseChecker.summary();
    }
}
