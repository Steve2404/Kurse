package ch3_makingdecisions.solutions;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise09_ForVsForEach.
 */
public class Solution09_ForVsForEach {

    public static int sumEvenIndices(int[] nums) {
        int total = 0;
        for (int i = 0; i < nums.length; i += 2) {
            total += nums[i];
        }
        return total;
    }

    public static int sumAll(int[] nums) {
        int total = 0;
        for (int value : nums) {
            total += value;
        }
        return total;
    }
}
