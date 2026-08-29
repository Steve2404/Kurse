package ch2_operators.solutions;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise10_OperatorPrecedence.
 */
public class Solution10_OperatorPrecedence {

    public static int multiplyBeforeAdd() {
        return 2 + 3 * 4;
    }

    public static boolean mixedComparisonAndLogic(int a, int b, int c) {
        return a + b > c && a > 0;
    }
}
