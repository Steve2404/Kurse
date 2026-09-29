package ch5_methods.solutions;

import static java.lang.Math.PI;
import static java.lang.Math.max;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans methods.exercises.Exercise07_StaticImportsUsage.
 */
public class Solution07_StaticImportsUsage {

    public static double computeCircumference(double radius) {
        // import static java.lang.Math.PI : PI s'utilise sans ecrire "Math." devant.
        return 2 * PI * radius;
    }

    public static int largerOf(int a, int b) {
        // import static java.lang.Math.max : on importe un MEMBRE static, jamais une classe.
        return max(a, b);
    }
}
