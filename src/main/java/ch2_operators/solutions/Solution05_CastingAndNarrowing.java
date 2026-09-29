package ch2_operators.solutions;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise05_CastingAndNarrowing.
 */
public class Solution05_CastingAndNarrowing {

    public static byte narrowToByte(int value) {
        // Retrecir exige un cast explicite ; (byte) garde les 8 derniers bits (130 -> -126).
        return (byte) value;
    }

    public static int truncateToInt(double value) {
        // (int) coupe vers zero, jamais d'arrondi : 3.9 -> 3 et -3.9 -> -3.
        return (int) value;
    }
}
