package ch2_operators.solutions;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise09_TernaryAndPromotion.
 */
public class Solution09_TernaryAndPromotion {

    public static int max(int a, int b) {
        return a > b ? a : b;
    }

    public static double promoteInTernary(boolean flag, int intVal, double doubleVal) {
        return flag ? intVal : doubleVal;
    }
}
