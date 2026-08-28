package methods.solutions;

import static java.lang.Math.PI;
import static java.lang.Math.max;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans methods.exercises.Exercise05_StaticImportsUsage.
 */
public class Solution05_StaticImportsUsage {

    public static double computeCircumference(double radius) {
        return 2 * PI * radius;
    }

    public static int largerOf(int a, int b) {
        return max(a, b);
    }
}
