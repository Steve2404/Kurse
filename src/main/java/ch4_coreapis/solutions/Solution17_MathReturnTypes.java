package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 17. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise17_MathReturnTypes.
 */
public class Solution17_MathReturnTypes {

    public static int roundFloatAsInt(float f) {
        // Math.round(float) rend un int ; Math.round(double) rendrait un long.
        return Math.round(f);
    }

    public static long roundDoubleAsLong(double d) {
        // Math.round(double) rend un long : l'affecter a un int ne compilerait pas sans cast.
        return Math.round(d);
    }

    public static double ceilingOf(double value) {
        // Math.ceil rend toujours un double, meme pour un resultat entier.
        return Math.ceil(value);
    }
}
