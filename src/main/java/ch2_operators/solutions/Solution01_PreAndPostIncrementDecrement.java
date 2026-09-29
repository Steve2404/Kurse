package ch2_operators.solutions;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise01_PreAndPostIncrementDecrement.
 */
public class Solution01_PreAndPostIncrementDecrement {

    public static int prePostSum(int start) {
        // Gauche a droite : start++ rend 5 (start passe a 6), puis ++start le passe a 7 et rend 7 : 5 + 7.
        return start++ + ++start;
    }

    public static int postPostDiff(int start) {
        // Le 1er start-- rend 10 (start = 9), le 2e rend 9 (start = 8) : 10 - 9.
        return start-- - start--;
    }
}
