package ch3_makingdecisions.solutions;

/**
 * Corrige de l'exercice 14. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise14_LabeledLoops.
 */
public class Solution14_LabeledLoops {

    public static int rowsContainingValue(int[][] grid, int target) {
        // Un break sans etiquette ne quitte que la boucle INTERIEURE : on passe a la ligne suivante.
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
        // break search quitte les DEUX boucles des la premiere occurrence.
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
