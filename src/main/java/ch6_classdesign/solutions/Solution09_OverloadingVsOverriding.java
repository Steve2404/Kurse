package ch6_classdesign.solutions;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.exercises.Exercise09_OverloadingVsOverriding.
 */
public class Solution09_OverloadingVsOverriding {

    static class Calculator {
        int compute(int a, int b) {
            return a + b;
        }

        int compute(int a, int b, int c) {
            return a + b + c;
        }
    }

    static class SmartCalculator extends Calculator {
        @Override
        int compute(int a, int b) {
            // Redefinition : meme signature que le parent ; super.compute reutilise sa version.
            return super.compute(a, b) * 2;
        }

        int compute(double a, double b) {
            // Surcharge : autres types de parametres, une methode de plus (pas de @Override possible).
            return (int) (a + b);
        }
    }
}
