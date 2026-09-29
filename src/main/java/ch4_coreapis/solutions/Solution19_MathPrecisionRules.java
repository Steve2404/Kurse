package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 19. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise19_MathPrecisionRules.
 */
public class Solution19_MathPrecisionRules {

    public static long myRound(double x) {
        // La vraie regle de Math.round : + 0.5 puis floor ; d'ou -2.5 -> -2.
        return (long) Math.floor(x + 0.5);
    }

    public static double myFloor(double x) {
        // Le cast coupe vers zero : pour un negatif non entier, il faut descendre d'un cran.
        long t = (long) x;
        return x < t ? t - 1 : t;
    }

    public static double myCeil(double x) {
        // Symetrique de floor : pour un positif non entier, on monte d'un cran.
        long t = (long) x;
        return x > t ? t + 1 : t;
    }

    public static String maxType(String t1, String t2) {
        // Promotion : le type le plus "large" des deux gagne (int < long < float < double).
        return rank(t1) >= rank(t2) ? t1 : t2;
    }

    private static int rank(String type) {
        // Petite boite : l'ordre d'elargissement des types numeriques.
        return switch (type) {
            case "int" -> 0;
            case "long" -> 1;
            case "float" -> 2;
            case "double" -> 3;
            default -> throw new IllegalArgumentException(type);
        };
    }

    public static String safeAdd(int a, int b) {
        // addExact lance ArithmeticException au lieu de deborder en silence.
        try {
            return String.valueOf(Math.addExact(a, b));
        } catch (ArithmeticException e) {
            return "debordement";
        }
    }

    public static double roundTo(double x, int decimals) {
        // Diviser par un double (factor), jamais par un int : sinon division entiere.
        double factor = Math.pow(10, decimals);
        return Math.round(x * factor) / factor;
    }
}
