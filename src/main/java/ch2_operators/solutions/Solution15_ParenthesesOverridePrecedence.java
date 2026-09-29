package ch2_operators.solutions;

/**
 * Corrige de l'exercice 15. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise15_ParenthesesOverridePrecedence.
 */
public class Solution15_ParenthesesOverridePrecedence {

    public static int forcedAdditionFirst() {
        // Les parentheses forcent l'addition en premier : (2 + 3) * 4 == 20.
        return (2 + 3) * 4;
    }

    public static boolean forcedOrLast(int a, int b, int c) {
        // Sans parentheses, && passerait avant || : a > 0 && b > 0 || c > 0 ne veut pas dire la meme chose.
        return a > 0 && (b > 0 || c > 0);
    }
}
