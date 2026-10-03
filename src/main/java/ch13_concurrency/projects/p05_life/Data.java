package ch13_concurrency.projects.p05_life;

/**
 * Les donnees du projet 5 (DONNEES, ne pas modifier) : la grille initiale du jeu de la vie (tore).
 */
public final class Data {

    public static final int SIZE = 48;
    public static final int GENERATIONS = 40;
    public static final int WORKERS = 4;

    /** La cellule (r, c) est-elle vivante au depart ? Environ une sur quatre. */
    public static boolean alive(int r, int c) {
        long x = (r * 1_000_003L + c) * 6_364_136_223_846_793_005L + 1_442_695_040_888_963_407L;
        return (x >>> 60) < 4;
    }

    private Data() {
    }
}
