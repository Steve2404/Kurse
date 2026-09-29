package ch2_operators.solutions;

/**
 * Corrige de l'exercice 14. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise14_OperatorPrecedence.
 */
public class Solution14_OperatorPrecedence {

    public static int multiplyBeforeAdd() {
        // * passe avant + : 2 + (3 * 4) == 14.
        return 2 + 3 * 4;
    }

    public static boolean mixedComparisonAndLogic(int a, int b, int c) {
        // Ordre : + d'abord, puis les comparaisons, puis && en dernier.
        return a + b > c && a > 0;
    }
}
