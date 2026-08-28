package ch3_makingdecisions.solutions;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise11_LabeledLoops.
 */
public class Solution11_LabeledLoops {

    public static int rowsContainingValue(int[][] grid, int target) {
        int rowCount = 0;
        for (int[] row : grid) {
            for (int value : row) {
                if (value == target) {
                    rowCount++;
                    break;
                }
            }
        }
        return rowCount;
    }

    public static int[] findFirstOccurrenceLabeled(int[][] grid, int target) {
        int[] found = null;
        search:
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == target) {
                    found = new int[] {i, j};
                    break search;
                }
            }
        }
        return found;
    }
}
