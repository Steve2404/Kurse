package ch3_makingdecisions.solutions;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise11_ForVsForEach.
 */
public class Solution11_ForVsForEach {

    public static int sumEvenIndices(int[] nums) {
        // for classique : on a besoin de l'index (i += 2 saute une case sur deux).
        int total = 0;
        for (int i = 0; i < nums.length; i += 2) {
            total += nums[i];
        }
        return total;
    }

    public static int sumAll(int[] nums) {
        // for-each : pas d'index a gerer, donc pas d'erreur de borne possible.
        int total = 0;
        for (int value : nums) {
            total += value;
        }
        return total;
    }
}
