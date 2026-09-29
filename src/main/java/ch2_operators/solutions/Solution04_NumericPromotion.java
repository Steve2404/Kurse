package ch2_operators.solutions;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise04_NumericPromotion.
 */
public class Solution04_NumericPromotion {

    public static int sumBytes(byte a, byte b) {
        // byte + byte est TOUJOURS un int (promotion) : d'ou le type de retour int.
        return a + b;
    }

    public static double addIntAndDouble(int a, double b) {
        // int + double : l'int est promu en double avant l'addition.
        return a + b;
    }
}
