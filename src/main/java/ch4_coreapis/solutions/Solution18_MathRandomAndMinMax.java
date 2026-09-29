package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 18. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise18_MathRandomAndMinMax.
 */
public class Solution18_MathRandomAndMinMax {

    public static int randomInRange(int min, int max) {
        // Math.random() est dans [0.0, 1.0) : * (max - min + 1) puis cast pour inclure max.
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public static double minOfThree(double a, double b, double c) {
        // Math.min ne prend que 2 arguments : on les emboite.
        return Math.min(a, Math.min(b, c));
    }
}
