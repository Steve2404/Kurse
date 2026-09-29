package ch2_operators.solutions;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise08_RelationalOperatorsAndInstanceof.
 */
public class Solution08_RelationalOperatorsAndInstanceof {

    public static boolean isInRange(int value, int min, int max) {
        // Deux comparaisons reliees par && : bornes incluses avec >= et <=.
        return value >= min && value <= max;
    }

    public static boolean isNumberType(Object obj) {
        // instanceof teste le vrai type de l'objet : Integer, Double... sont tous des Number.
        return obj instanceof Number;
    }
}
