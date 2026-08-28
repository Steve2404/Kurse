package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise10_MathReturnTypes.
 */
public class Solution10_MathReturnTypes {

    public static int roundFloatAsInt(float f) {
        return Math.round(f);
    }

    public static long roundDoubleAsLong(double d) {
        return Math.round(d);
    }

    public static double ceilingOf(double value) {
        return Math.ceil(value);
    }
}
