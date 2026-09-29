package ch4_coreapis.solutions;

import java.util.Arrays;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise12_ArraysSortAndBinarySearch.
 */
public class Solution12_ArraysSortAndBinarySearch {

    public static void sortAscending(int[] nums) {
        // Arrays.sort trie EN PLACE et rend void : rien a recuperer.
        Arrays.sort(nums);
    }

    public static int insertionPointFor(int[] sortedArr, int target) {
        // binarySearch rend -(point d'insertion) - 1 si absent : on inverse la formule.
        int result = Arrays.binarySearch(sortedArr, target);
        if (result >= 0) {
            return result;
        }
        return -(result) - 1;
    }
}
