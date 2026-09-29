package ch2_operators.solutions;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise10_BooleanBitwiseOperators.
 */
public class Solution10_BooleanBitwiseOperators {

    public static boolean exactlyOneTrue(boolean a, boolean b) {
        // ^ (ou exclusif) sur des boolean : vrai si les deux valeurs sont differentes.
        return a ^ b;
    }

    public static boolean bothSameValue(boolean a, boolean b) {
        // La negation du ou exclusif : vrai si les deux valeurs sont egales.
        return !(a ^ b);
    }
}
