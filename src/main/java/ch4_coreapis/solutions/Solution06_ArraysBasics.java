package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise06_ArraysBasics.
 */
public class Solution06_ArraysBasics {

    public static int sumArray(int[] nums) {
        int total = 0;
        for (int n : nums) {
            total += n;
        }
        return total;
    }

    public static int lastElement(int[] nums) {
        return nums[nums.length - 1];
    }
}
