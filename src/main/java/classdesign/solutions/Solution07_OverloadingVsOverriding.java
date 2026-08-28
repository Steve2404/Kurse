package classdesign.solutions;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans classdesign.exercises.Exercise07_OverloadingVsOverriding.
 */
public class Solution07_OverloadingVsOverriding {

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
            return super.compute(a, b) * 2;
        }

        int compute(double a, double b) {
            return (int) (a + b);
        }
    }
}
