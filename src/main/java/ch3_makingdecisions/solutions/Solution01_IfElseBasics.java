package ch3_makingdecisions.solutions;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise01_IfElseBasics.
 */
public class Solution01_IfElseBasics {

    public static String classifyNumber(int n) {
        if (n < 0) {
            return "negatif";
        } else if (n == 0) {
            return "zero";
        } else {
            return "positif";
        }
    }
}
