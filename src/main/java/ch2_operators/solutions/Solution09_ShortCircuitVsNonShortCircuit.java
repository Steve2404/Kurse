package ch2_operators.solutions;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise09_ShortCircuitVsNonShortCircuit.
 */
public class Solution09_ShortCircuitVsNonShortCircuit {

    private static int counter = 0;

    private static boolean sideEffect() {
        counter++;
        return true;
    }

    public static int evaluateWithShortCircuit(boolean flag) {
        // && : flag faux suffit, sideEffect() n'est jamais appelee (compteur a 0).
        counter = 0;
        boolean result = flag && sideEffect();
        return counter;
    }

    public static int evaluateWithoutShortCircuit(boolean flag) {
        // & : les deux cotes sont TOUJOURS evalues, meme si le gauche est faux.
        counter = 0;
        boolean result = flag & sideEffect();
        return counter;
    }
}
