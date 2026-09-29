package ch2_operators.drills.exercises;

import ch2_operators.ExerciseChecker;
import ch2_operators.drills.Grades;

/**
 * DRILL 01 - Arithmetique et promotion : / entiere, %, promotion, debordement, division par zero (projet bulletin)
 * ==============================================================================================================
 *
 * -- Comment utiliser un DRILL (different d'un exercice) --
 *
 * Un exercice t'APPREND une notion. Un drill te la fait REPETER jusqu'a
 * ce qu'elle sorte toute seule. Chaque TODO tient en 1 ligne et vise
 * UNE regle precise (entre crochets).
 *
 *   1. Chronometre-toi, note ton temps et ton score dans drills/REVISION.md.
 *   2. Ecris SANS regarder la "carte memoire" en bas. Bloque plus d'une
 *      minute : regarde-la, cache-la, reecris.
 *   3. Refais le MEME drill plus tard, a partir de zero (voir REVISION.md).
 *
 * Donnees : ch2_operators.drills.Grades.
 *
 *
 * -- Les TODO (regle visee entre crochets) --
 *
 * TODO 1  : intDivision(a, b)    [/ entre deux int] 7 / 2 -> 3 ; b == 0 -> ArithmeticException.
 * TODO 2  : realDivision(a, b)   [un double AVANT la division] 7 et 2 -> 3.5.
 * TODO 3  : remainder(a, b)      [% garde le signe de a] -7 et 3 -> -1.
 * TODO 4  : isEven(n)            [% 2] -4 -> true, -3 -> false.
 * TODO 5  : sumOfBytes(a, b)     [byte + byte -> int] rendre la somme en int.
 * TODO 6  : codePlusOne(c)       [char + int -> int] 'a' -> 98.
 * TODO 7  : nextChar(c)          [cast du resultat en char] 'a' -> 'b'.
 * TODO 8  : bigProduct(a, b)     [long AVANT la multiplication] 1000000 et 3000 -> 3000000000.
 * TODO 9  : overflow()           [Integer.MAX_VALUE + 1] -> -2147483648.
 * TODO 10 : classAverage()       [somme / (double) nombre] de Grades.SCORES -> 70 / 6.0.
 * TODO 11 : infinity()           [double / 0 ne plante pas] 1.0 / 0 -> Infinity.
 * TODO 12 : isNotANumber(x)      [NaN n'est egal a rien, pas meme a lui-meme] x != x.
 * TODO 13 : postPlusPre(x)       [x++ + ++x] 5 -> 12.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   int / int -> int (coupe vers zero) ; un double d'un cote -> double
 *   % : reste, signe du DIVIDENDE (-7 % 3 == -1, 7 % -3 == 1)
 *   Promotion : byte/short/char -> int ; puis int < long < float < double
 *   (long) a * b : le cast AVANT l'operation ; (long) (a * b) : trop tard
 *   int / 0 -> ArithmeticException: / by zero ; 1.0 / 0 -> Infinity ; 0.0 / 0 -> NaN
 *   x++ rend l'ancienne valeur ; ++x la nouvelle ; les operandes s'evaluent de gauche a droite
 * ---------------------------------------------------------------------
 */
public class Drill01_ArithmeticAndPromotion {

    public static int intDivision(int a, int b) {
        throw new UnsupportedOperationException("TODO 1 : implementer intDivision()");
    }

    public static double realDivision(int a, int b) {
        throw new UnsupportedOperationException("TODO 2 : implementer realDivision()");
    }

    public static int remainder(int a, int b) {
        throw new UnsupportedOperationException("TODO 3 : implementer remainder()");
    }

    public static boolean isEven(int n) {
        throw new UnsupportedOperationException("TODO 4 : implementer isEven()");
    }

    public static int sumOfBytes(byte a, byte b) {
        throw new UnsupportedOperationException("TODO 5 : implementer sumOfBytes()");
    }

    public static int codePlusOne(char c) {
        throw new UnsupportedOperationException("TODO 6 : implementer codePlusOne()");
    }

    public static char nextChar(char c) {
        throw new UnsupportedOperationException("TODO 7 : implementer nextChar()");
    }

    public static long bigProduct(int a, int b) {
        throw new UnsupportedOperationException("TODO 8 : implementer bigProduct()");
    }

    public static int overflow() {
        throw new UnsupportedOperationException("TODO 9 : implementer overflow()");
    }

    public static double classAverage() {
        throw new UnsupportedOperationException("TODO 10 : implementer classAverage()");
    }

    public static double infinity() {
        throw new UnsupportedOperationException("TODO 11 : implementer infinity()");
    }

    public static boolean isNotANumber(double x) {
        throw new UnsupportedOperationException("TODO 12 : implementer isNotANumber()");
    }

    public static int postPlusPre(int x) {
        throw new UnsupportedOperationException("TODO 13 : implementer postPlusPre()");
    }

    public static void main(String[] args) {
        boolean divZero = false;
        try {
            intDivision(1, 0);
        } catch (ArithmeticException e) {
            divZero = e.getMessage().equals("/ by zero");
        }
        ExerciseChecker.check("1  intDivision(7, 2) == 3, et (1, 0) lance ArithmeticException(/ by zero)",
                intDivision(7, 2) == 3 && divZero);
        ExerciseChecker.check("2  realDivision(7, 2) == 3.5", realDivision(7, 2) == 3.5);
        ExerciseChecker.check("3  remainder(-7, 3) == -1, remainder(7, -3) == 1", remainder(-7, 3) == -1 && remainder(7, -3) == 1);
        ExerciseChecker.check("4  isEven(-4) && !isEven(-3)", isEven(-4) && !isEven(-3));
        ExerciseChecker.check("5  sumOfBytes(100, 100) == 200 (pas de debordement : c'est un int)",
                sumOfBytes((byte) 100, (byte) 100) == 200);
        ExerciseChecker.check("6  codePlusOne('a') == 98", codePlusOne('a') == 98);
        ExerciseChecker.check("7  nextChar('a') == 'b'", nextChar('a') == 'b');
        ExerciseChecker.check("8  bigProduct(1000000, 3000) == 3000000000", bigProduct(1000000, 3000) == 3_000_000_000L);
        ExerciseChecker.check("9  overflow() == -2147483648", overflow() == Integer.MIN_VALUE);
        ExerciseChecker.check("10 classAverage() == 70 / 6.0", classAverage() == 70 / 6.0);
        ExerciseChecker.check("11 infinity() == Infinity", Double.isInfinite(infinity()) && infinity() > 0);
        ExerciseChecker.check("12 isNotANumber(0.0 / 0) && !isNotANumber(1.5)", isNotANumber(0.0 / 0) && !isNotANumber(1.5));
        ExerciseChecker.check("13 postPlusPre(5) == 12", postPlusPre(5) == 12);

        ExerciseChecker.summary();
    }
}
