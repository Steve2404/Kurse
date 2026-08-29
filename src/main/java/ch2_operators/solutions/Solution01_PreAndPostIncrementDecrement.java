package ch2_operators.solutions;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise01_PreAndPostIncrementDecrement.
 */
public class Solution01_PreAndPostIncrementDecrement {

    public static int prePostSum(int start) {
        return start++ + ++start;
    }

    public static int postPostDiff(int start) {
        return start-- - start--;
    }
}
