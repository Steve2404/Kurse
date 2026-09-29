package ch4_coreapis.solutions;

import java.util.Arrays;

/**
 * Corrige de l'exercice 13. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise13_ArraysCompareAndMismatch.
 */
public class Solution13_ArraysCompareAndMismatch {

    public static String describeComparison(int[] a, int[] b) {
        // Arrays.compare : seul le SIGNE compte (negatif, 0, positif), pas la valeur exacte.
        int result = Arrays.compare(a, b);
        if (result == 0) {
            return "egaux";
        }
        if (result < 0) {
            return "a avant b";
        }
        return "a apres b";
    }

    public static String describeMismatch(int[] a, int[] b) {
        // mismatch rend -1 si les tableaux sont identiques, sinon le premier index different.
        int result = Arrays.mismatch(a, b);
        if (result == -1) {
            return "identiques";
        }
        return "diverge a l'index " + result;
    }
}
