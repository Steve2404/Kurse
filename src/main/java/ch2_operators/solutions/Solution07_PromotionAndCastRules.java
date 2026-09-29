package ch2_operators.solutions;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise07_PromotionAndCastRules.
 */
public class Solution07_PromotionAndCastRules {

    public static String binaryPromotion(String left, String right) {
        // Du plus "grand" au plus petit : double > float > long > int ; byte/short/char montent en int.
        if (left.equals("double") || right.equals("double")) {
            return "double";
        }
        if (left.equals("float") || right.equals("float")) {
            return "float";
        }
        if (left.equals("long") || right.equals("long")) {
            return "long";
        }
        return "int";
    }

    public static int wrapToByte(int value) {
        // Calcul en long pour que value + 128 ne deborde pas ; le "+ 256) % 256" corrige
        // le reste negatif de Java (-7 % 3 == -1).
        long r = ((value + 128L) % 256 + 256) % 256 - 128;
        return (int) r;
    }

    public static double truncateTowardZero(double value) {
        // (int) coupe vers zero : floor pour les positifs, ceil pour les negatifs.
        return value >= 0 ? Math.floor(value) : Math.ceil(value);
    }

    public static long safeMultiply(int a, int b) {
        // Le cast s'applique a "a" AVANT la multiplication, qui se fait donc en long.
        // (long) (a * b) serait trop tard : le debordement aurait deja eu lieu en int.
        return (long) a * b;
    }
}
