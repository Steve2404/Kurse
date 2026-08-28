package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise11_MathRandomAndMinMax.
 */
public class Solution11_MathRandomAndMinMax {

    public static int randomInRange(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public static double minOfThree(double a, double b, double c) {
        return Math.min(a, Math.min(b, c));
    }
}
