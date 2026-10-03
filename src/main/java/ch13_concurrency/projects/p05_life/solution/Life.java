package ch13_concurrency.projects.p05_life.solution;

/**
 * SOLUTION - les regles du jeu de la vie sur un tore (les bords se rejoignent).
 * Une cellule vivante survit avec 2 ou 3 voisines ; une morte nait avec exactement 3.
 */
public final class Life {

    private Life() {
    }

    public static boolean nextState(boolean[][] g, int r, int c) {
        int n = g.length;
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if ((dr != 0 || dc != 0) && g[(r + dr + n) % n][(c + dc + n) % n]) {
                    count++;
                }
            }
        }
        return g[r][c] ? count == 2 || count == 3 : count == 3;
    }

    // Calcule les lignes [fromRow, toRow) de la generation suivante.
    public static void step(boolean[][] current, boolean[][] next, int fromRow, int toRow) {
        for (int r = fromRow; r < toRow; r++) {
            for (int c = 0; c < current.length; c++) {
                next[r][c] = nextState(current, r, c);
            }
        }
    }

    public static int population(boolean[][] g) {
        int p = 0;
        for (boolean[] row : g) {
            for (boolean cell : row) {
                p += cell ? 1 : 0;
            }
        }
        return p;
    }

    // Une empreinte de la grille, pour comparer deux calculs.
    public static long fingerprint(boolean[][] g) {
        long h = 17;
        for (boolean[] row : g) {
            for (boolean cell : row) {
                h = h * 31 + (cell ? 1 : 0);
            }
        }
        return h;
    }
}
