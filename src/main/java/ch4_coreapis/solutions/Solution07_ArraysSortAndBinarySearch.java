package ch4_coreapis.solutions;

import java.util.Arrays;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise07_ArraysSortAndBinarySearch.
 */
public class Solution07_ArraysSortAndBinarySearch {

    public static void sortAscending(int[] nums) {
        Arrays.sort(nums);
    }

    public static int insertionPointFor(int[] sortedArr, int target) {
        int result = Arrays.binarySearch(sortedArr, target);
        if (result >= 0) {
            return result;
        }
        return -(result) - 1;
    }
}
