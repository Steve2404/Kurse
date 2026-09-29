package ch2_operators.drills.solutions;

import ch2_operators.drills.Grades;

/**
 * Corrige du drill 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.drills.exercises.Drill02_CastsAndCompound.
 */
public class SolutionDrill02_CastsAndCompound {

    public static byte toByte(int value) {
        // Garde les 8 derniers bits : 130 fait le tour et devient -126.
        return (byte) value;
    }

    public static int truncate(double value) {
        // Coupe vers zero, jamais d'arrondi.
        return (int) value;
    }

    public static short toShort(int value) {
        // short : 16 bits, de -32768 a 32767 ; 40000 fait le tour.
        return (short) value;
    }

    public static int roundPositive(double value) {
        // Astuce pour les positifs : + 0.5 puis couper (Math.round fait mieux, chapitre 4).
        return (int) (value + 0.5);
    }

    public static byte addToByte(byte b, int n) {
        // b += n equivaut a b = (byte) (b + n) : le cast est cache.
        b += n;
        return b;
    }

    public static int scaleInt(int v, double f) {
        // Calcul en double (19.9), puis cast cache vers int : 19.
        v *= f;
        return v;
    }

    public static short halveShort(short s) {
        // s = s / 2 ne compilerait pas (int vers short) ; s /= 2 cache le cast.
        s /= 2;
        return s;
    }

    public static char shiftLetter(char c, int n) {
        // Meme cast cache vers char.
        c += n;
        return c;
    }

    public static long widenToLong(int value) {
        // Elargissement : aucune perte possible, donc aucun cast demande.
        return value;
    }

    public static double widenToDouble(long value) {
        // long -> double est automatique (mais peut perdre de la precision pour les tres grands long).
        return value;
    }

    public static int narrowLong(long value) {
        // (int) garde les 32 derniers bits : 4294967296 (2^32) disparait, il reste 5.
        return (int) value;
    }

    public static byte smallPlusTen() {
        // 120 + 10 = 130 ne tient pas dans un byte : += deborde en silence vers -126.
        byte score = Grades.SMALL;
        score += 10;
        return score;
    }
}
