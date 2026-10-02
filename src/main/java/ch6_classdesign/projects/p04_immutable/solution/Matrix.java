package ch6_classdesign.projects.p04_immutable.solution;

import java.util.Arrays;

/**
 * SOLUTION - une matrice IMMUABLE : le tableau interne ne sort jamais, et n'entre jamais sans copie.
 */
public final class Matrix {

    private final long[][] cells;

    private Matrix(long[][] cells) {
        this.cells = cells;   // prive : seuls les appels internes passent ici, avec un tableau deja neuf
    }

    // Copie defensive a l'ENTREE : si l'appelant modifie son tableau ensuite, la matrice ne bouge pas.
    public static Matrix of(long[][] source) {
        return new Matrix(copy(source));
    }

    public static Matrix identity(int n) {
        long[][] c = new long[n][n];
        for (int i = 0; i < n; i++) {
            c[i][i] = 1;
        }
        return new Matrix(c);
    }

    private static long[][] copy(long[][] source) {
        long[][] c = new long[source.length][];
        for (int i = 0; i < source.length; i++) {
            c[i] = source[i].clone();
        }
        return c;
    }

    // Copie defensive a la SORTIE.
    public long[][] toArray() {
        return copy(cells);
    }

    public long get(int r, int c) {
        return cells[r][c];
    }

    public Matrix times(Matrix o) {
        int n = cells.length;
        int m = o.cells[0].length;
        long[][] r = new long[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < o.cells.length; k++) {
                    r[i][j] += cells[i][k] * o.cells[k][j];
                }
            }
        }
        return new Matrix(r);
    }

    // Puissance rapide : chaque etape rend un NOUVEL objet, l'original n'est jamais modifie.
    public Matrix power(int n) {
        if (n == 0) {
            return identity(cells.length);
        }
        Matrix half = power(n / 2);
        Matrix sq = half.times(half);
        return n % 2 == 0 ? sq : sq.times(this);
    }

    public Matrix transpose() {
        long[][] t = new long[cells[0].length][cells.length];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                t[j][i] = cells[i][j];
            }
        }
        return new Matrix(t);
    }

    // Determinant EXACT par elimination de Gauss sur des fractions (aucune erreur d'arrondi).
    public Fraction determinant() {
        int n = cells.length;
        Fraction[][] a = new Fraction[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = Fraction.of(cells[i][j]);
            }
        }
        Fraction det = Fraction.ONE;
        for (int col = 0; col < n; col++) {
            int pivot = col;
            while (pivot < n && a[pivot][col].isZero()) {
                pivot++;
            }
            if (pivot == n) {
                return Fraction.ZERO;            // colonne nulle : matrice singuliere
            }
            if (pivot != col) {                  // echange de lignes : le determinant change de signe
                Fraction[] t = a[pivot];
                a[pivot] = a[col];
                a[col] = t;
                det = det.times(Fraction.of(-1));
            }
            det = det.times(a[col][col]);
            for (int r = col + 1; r < n; r++) {
                Fraction factor = a[r][col].divide(a[col][col]);
                for (int c = col; c < n; c++) {
                    a[r][c] = a[r][c].minus(factor.times(a[col][c]));
                }
            }
        }
        return det;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Matrix m && Arrays.deepEquals(m.cells, cells);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(cells);
    }

    @Override
    public String toString() {
        return Arrays.deepToString(cells);
    }
}
