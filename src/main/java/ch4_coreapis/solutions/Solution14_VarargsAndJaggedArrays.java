package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 14. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise14_VarargsAndJaggedArrays.
 */
public class Solution14_VarargsAndJaggedArrays {

    public static int sumVarargs(int... nums) {
        // int... nums EST un int[] : for-each marche, et sumVarargs() donne un tableau vide.
        int total = 0;
        for (int n : nums) {
            total += n;
        }
        return total;
    }

    public static int[][] buildJaggedArray() {
        // Tableau en escalier : chaque ligne a sa propre longueur.
        return new int[][] {{1}, {2, 3}, {4, 5, 6}};
    }

    public static int sumJagged(int[][] grid) {
        // Parcourir ligne par ligne : chaque row.length peut etre different.
        int total = 0;
        for (int[] row : grid) {
            for (int value : row) {
                total += value;
            }
        }
        return total;
    }
}
