package ch5_methods.projects.p03_bikes.solution;

import ch5_methods.projects.p03_bikes.Data;

import static java.lang.Math.min;

/**
 * SOLUTION - le reseau : uniquement des membres static, calcules UNE fois au chargement de la classe.
 */
public class Network {

    // 1. Les initialiseurs static s'executent dans l'ORDRE DU FICHIER, au premier usage de la classe.
    static final String[] NAMES = load();

    // static final sans valeur : il DOIT etre affecte une seule fois, dans un bloc static.
    static final int SIZE;

    static {
        SIZE = NAMES.length;
        System.out.println("[charge] Network : bloc static 1 (" + SIZE + " stations)");
    }

    static final int INF = 1_000;
    static final int[][] DIST = new int[SIZE][SIZE];
    private static int routes;

    static {
        // Floyd-Warshall : on autorise peu a peu chaque station k comme etape intermediaire.
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                DIST[i][j] = i == j ? 0 : INF;
            }
        }
        for (String road : Data.ROADS) {
            String[] p = road.split("[- ]");
            int a = index(p[0]);
            int b = index(p[1]);
            int km = Integer.parseInt(p[2]);
            DIST[a][b] = km;
            DIST[b][a] = km;
        }
        for (int k = 0; k < SIZE; k++) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (DIST[i][k] + DIST[k][j] < DIST[i][j]) {
                        DIST[i][j] = DIST[i][k] + DIST[k][j];
                        routes++;
                    }
                }
            }
        }
        System.out.println("[charge] Network : bloc static 2 (Floyd-Warshall, " + routes + " raccourcis trouves)");
    }

    // Une methode static appelee pendant l'initialisation d'un champ static.
    private static String[] load() {
        System.out.println("[charge] Network : champ NAMES");
        return Data.STATIONS.clone();
    }

    static int index(String name) {
        for (int i = 0; i < NAMES.length; i++) {
            if (NAMES[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }

    static int km(int a, int b) {
        return DIST[a][b];
    }

    static int diameter() {
        int worst = 0;
        for (int[] row : DIST) {
            for (int d : row) {
                worst = Math.max(worst, d);
            }
        }
        return worst;
    }

    static int closest(int from) {
        int best = INF;
        for (int j = 0; j < SIZE; j++) {
            if (j != from) {
                best = min(best, DIST[from][j]);   // min vient de l'import static
            }
        }
        return best;
    }
}
