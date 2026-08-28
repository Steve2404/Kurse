package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise09_VarargsAndJaggedArrays.
 */
public class Solution09_VarargsAndJaggedArrays {

    public static int sumVarargs(int... nums) {
        int total = 0;
        for (int n : nums) {
            total += n;
        }
        return total;
    }

    public static int[][] buildJaggedArray() {
        return new int[][] {{1}, {2, 3}, {4, 5, 6}};
    }

    public static int sumJagged(int[][] grid) {
        int total = 0;
        for (int[] row : grid) {
            for (int value : row) {
                total += value;
            }
        }
        return total;
    }
}
