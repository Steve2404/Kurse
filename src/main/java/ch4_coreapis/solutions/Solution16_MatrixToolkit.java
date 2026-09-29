package ch4_coreapis.solutions;

import java.util.Arrays;

/**
 * Corrige de l'exercice 16. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise16_MatrixToolkit.
 */
public class Solution16_MatrixToolkit {

    public static int[][] transpose(int[][] m) {
        // Dimensions inversees : [colonnes][lignes].
        int[][] t = new int[m[0].length][m.length];
        for (int r = 0; r < m.length; r++) {
            for (int c = 0; c < m[0].length; c++) {
                t[c][r] = m[r][c];
            }
        }
        return t;
    }

    public static int[][] rotateClockwise(int[][] m) {
        // La ligne r atterrit dans la colonne rows - 1 - r (la premiere ligne passe a droite).
        int rows = m.length;
        int cols = m[0].length;
        int[][] out = new int[cols][rows];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                out[c][rows - 1 - r] = m[r][c];
            }
        }
        return out;
    }

    public static int[][] pascal(int n) {
        // Tableau en escalier : new int[n][] puis chaque ligne a sa propre taille.
        int[][] t = new int[n][];
        for (int i = 0; i < n; i++) {
            t[i] = new int[i + 1];
            t[i][0] = 1;
            t[i][i] = 1;
            for (int j = 1; j < i; j++) {
                t[i][j] = t[i - 1][j - 1] + t[i - 1][j];
            }
        }
        return t;
    }

    public static String spiral(int[][] m) {
        // 4 bornes qui se resserrent ; les deux if evitent de relire une ligne ou colonne deja faite.
        StringBuilder sb = new StringBuilder();
        int top = 0;
        int bottom = m.length - 1;
        int left = 0;
        int right = m[0].length - 1;
        while (top <= bottom && left <= right) {
            for (int c = left; c <= right; c++) {
                append(sb, m[top][c]);
            }
            top++;
            for (int r = top; r <= bottom; r++) {
                append(sb, m[r][right]);
            }
            right--;
            if (top <= bottom) {
                for (int c = right; c >= left; c--) {
                    append(sb, m[bottom][c]);
                }
                bottom--;
            }
            if (left <= right) {
                for (int r = bottom; r >= top; r--) {
                    append(sb, m[r][left]);
                }
                left++;
            }
        }
        return sb.toString();
    }

    private static void append(StringBuilder sb, int value) {
        // Petite boite : l'espace seulement entre deux nombres.
        if (sb.length() > 0) {
            sb.append(' ');
        }
        sb.append(value);
    }

    public static boolean isSymmetric(int[][] m) {
        // Symetrique = egale a sa transposee ; deepEquals compare le contenu des lignes.
        return m.length == m[0].length && Arrays.deepEquals(m, transpose(m));
    }

    public static int sumAll(int[]... rows) {
        // Le varargs est un vrai int[][] : sumAll() donne un tableau vide, pas null.
        int sum = 0;
        for (int[] row : rows) {
            for (int v : row) {
                sum += v;
            }
        }
        return sum;
    }
}
