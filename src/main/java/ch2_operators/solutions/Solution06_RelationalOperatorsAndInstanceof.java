package ch2_operators.solutions;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise06_RelationalOperatorsAndInstanceof.
 */
public class Solution06_RelationalOperatorsAndInstanceof {

    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    public static boolean isNumberType(Object obj) {
        return obj instanceof Number;
    }
}
