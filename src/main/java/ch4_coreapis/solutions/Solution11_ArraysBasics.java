package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise11_ArraysBasics.
 */
public class Solution11_ArraysBasics {

    public static int sumArray(int[] nums) {
        // for-each : lecture simple, aucun risque de sortir du tableau.
        int total = 0;
        for (int n : nums) {
            total += n;
        }
        return total;
    }

    public static int lastElement(int[] nums) {
        // Dernier index = length - 1 ; nums[nums.length] lancerait ArrayIndexOutOfBoundsException.
        return nums[nums.length - 1];
    }
}
