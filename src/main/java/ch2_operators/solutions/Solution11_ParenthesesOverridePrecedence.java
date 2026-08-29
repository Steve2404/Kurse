package ch2_operators.solutions;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise11_ParenthesesOverridePrecedence.
 */
public class Solution11_ParenthesesOverridePrecedence {

    public static int forcedAdditionFirst() {
        return (2 + 3) * 4;
    }

    public static boolean forcedOrLast(int a, int b, int c) {
        return a > 0 && (b > 0 || c > 0);
    }
}
