package ch2_operators.solutions;

/**
 * Corrige de l'exercice 13. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise13_TernaryAndPromotion.
 */
public class Solution13_TernaryAndPromotion {

    public static int max(int a, int b) {
        // Le ternaire rend directement une valeur : un if/else en une expression.
        return a > b ? a : b;
    }

    public static double promoteInTernary(boolean flag, int intVal, double doubleVal) {
        // Le type du ternaire est fixe a la COMPILATION d'apres les 2 branches (int et double -> double),
        // meme quand c'est la branche int qui est choisie.
        return flag ? intVal : doubleVal;
    }
}
