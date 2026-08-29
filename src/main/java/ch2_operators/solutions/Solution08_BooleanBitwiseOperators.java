package ch2_operators.solutions;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise08_BooleanBitwiseOperators.
 */
public class Solution08_BooleanBitwiseOperators {

    public static boolean exactlyOneTrue(boolean a, boolean b) {
        return a ^ b;
    }

    public static boolean bothSameValue(boolean a, boolean b) {
        return !(a ^ b);
    }
}
