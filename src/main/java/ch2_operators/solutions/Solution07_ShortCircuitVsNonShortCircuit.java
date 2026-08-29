package ch2_operators.solutions;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise07_ShortCircuitVsNonShortCircuit.
 */
public class Solution07_ShortCircuitVsNonShortCircuit {

    private static int counter = 0;

    private static boolean sideEffect() {
        counter++;
        return true;
    }

    public static int evaluateWithShortCircuit(boolean flag) {
        counter = 0;
        boolean result = flag && sideEffect();
        return counter;
    }

    public static int evaluateWithoutShortCircuit(boolean flag) {
        counter = 0;
        boolean result = flag & sideEffect();
        return counter;
    }
}
