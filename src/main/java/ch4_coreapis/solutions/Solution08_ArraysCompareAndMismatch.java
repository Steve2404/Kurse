package ch4_coreapis.solutions;

import java.util.Arrays;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise08_ArraysCompareAndMismatch.
 */
public class Solution08_ArraysCompareAndMismatch {

    public static String describeComparison(int[] a, int[] b) {
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
        int result = Arrays.mismatch(a, b);
        if (result == -1) {
            return "identiques";
        }
        return "diverge a l'index " + result;
    }
}
