package ch2_operators.drills.exercises;

import ch2_operators.ExerciseChecker;
import ch2_operators.drills.Grades;

/**
 * DRILL 02 - Casts et operateurs composes : retrecir, elargir, et le cast cache de += (projet bulletin)
 * ====================================================================================================
 *
 * Mode d'emploi : voir Drill01_ArithmeticAndPromotion.
 *
 *
 * -- Les TODO (regle visee entre crochets) --
 *
 * TODO 1  : toByte(value)            [(byte)] 130 -> -126.
 * TODO 2  : truncate(value)          [(int) coupe vers zero] -3.9 -> -3.
 * TODO 3  : toShort(value)           [(short)] 40000 -> -25536.
 * TODO 4  : roundPositive(value)     [(int) (x + 0.5)] 2.5 -> 3, 2.4 -> 2.
 * TODO 5  : addToByte(b, n)          [b += n : cast cache] 10 et 5 -> 15.
 * TODO 6  : scaleInt(v, f)           [v *= f avec f double] 10 et 1.99 -> 19.
 * TODO 7  : halveShort(s)            [s /= 2 sur un short] 9 -> 4.
 * TODO 8  : shiftLetter(c, n)        [c += n sur un char] 'a' et 2 -> 'c'.
 * TODO 9  : widenToLong(value)       [int -> long sans cast]
 * TODO 10 : widenToDouble(value)     [long -> double sans cast] 5 -> 5.0.
 * TODO 11 : narrowLong(value)        [(int) sur un long : 32 derniers bits] 4294967301 -> 5.
 * TODO 12 : smallPlusTen()           [Grades.SMALL += 10 dans une variable byte] 120 -> -126.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Elargir (byte -> short -> int -> long -> float -> double, char -> int) : automatique
 *   Retrecir : cast OBLIGATOIRE ; les entiers gardent les derniers bits (debordement silencieux),
 *              (int) sur un double coupe vers zero
 *   x op= y  ==  x = (TypeDeX) (x op y) : le cast est cache, meme vers byte/short/char
 *   byte b = 10; b = b + 5;  -> ne compile pas ;  b += 5; -> compile
 * ---------------------------------------------------------------------
 */
public class Drill02_CastsAndCompound {

    public static byte toByte(int value) {
        throw new UnsupportedOperationException("TODO 1 : implementer toByte()");
    }

    public static int truncate(double value) {
        throw new UnsupportedOperationException("TODO 2 : implementer truncate()");
    }

    public static short toShort(int value) {
        throw new UnsupportedOperationException("TODO 3 : implementer toShort()");
    }

    public static int roundPositive(double value) {
        throw new UnsupportedOperationException("TODO 4 : implementer roundPositive()");
    }

    public static byte addToByte(byte b, int n) {
        throw new UnsupportedOperationException("TODO 5 : implementer addToByte()");
    }

    public static int scaleInt(int v, double f) {
        throw new UnsupportedOperationException("TODO 6 : implementer scaleInt()");
    }

    public static short halveShort(short s) {
        throw new UnsupportedOperationException("TODO 7 : implementer halveShort()");
    }

    public static char shiftLetter(char c, int n) {
        throw new UnsupportedOperationException("TODO 8 : implementer shiftLetter()");
    }

    public static long widenToLong(int value) {
        throw new UnsupportedOperationException("TODO 9 : implementer widenToLong()");
    }

    public static double widenToDouble(long value) {
        throw new UnsupportedOperationException("TODO 10 : implementer widenToDouble()");
    }

    public static int narrowLong(long value) {
        throw new UnsupportedOperationException("TODO 11 : implementer narrowLong()");
    }

    public static byte smallPlusTen() {
        throw new UnsupportedOperationException("TODO 12 : implementer smallPlusTen()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  toByte(130) == -126, toByte(-129) == 127", toByte(130) == -126 && toByte(-129) == 127);
        ExerciseChecker.check("2  truncate(-3.9) == -3, truncate(3.9) == 3", truncate(-3.9) == -3 && truncate(3.9) == 3);
        ExerciseChecker.check("3  toShort(40000) == -25536", toShort(40000) == -25536);
        ExerciseChecker.check("4  roundPositive(2.5) == 3, (2.4) == 2", roundPositive(2.5) == 3 && roundPositive(2.4) == 2);
        ExerciseChecker.check("5  addToByte(10, 5) == 15", addToByte((byte) 10, 5) == 15);
        ExerciseChecker.check("6  scaleInt(10, 1.99) == 19", scaleInt(10, 1.99) == 19);
        ExerciseChecker.check("7  halveShort(9) == 4", halveShort((short) 9) == 4);
        ExerciseChecker.check("8  shiftLetter('a', 2) == 'c'", shiftLetter('a', 2) == 'c');
        ExerciseChecker.check("9  widenToLong(7) == 7L", widenToLong(7) == 7L);
        ExerciseChecker.check("10 widenToDouble(5L) == 5.0", widenToDouble(5L) == 5.0);
        ExerciseChecker.check("11 narrowLong(4294967301L) == 5", narrowLong(4294967301L) == 5);
        ExerciseChecker.check("12 smallPlusTen() == -126", smallPlusTen() == -126);

        ExerciseChecker.summary();
    }
}
