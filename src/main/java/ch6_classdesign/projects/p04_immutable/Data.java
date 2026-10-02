package ch6_classdesign.projects.p04_immutable;

/**
 * Les donnees du projet 4 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Les matrices dont on calcule le determinant exact. */
    public static final long[][] M3 = {{2, -3, 1}, {2, 0, -1}, {1, 4, 5}};
    public static final long[][] M4 = {{1, 0, 2, -1}, {3, 0, 0, 5}, {2, 1, 4, -3}, {1, 0, 5, 0}};
    public static final long[][] SINGULAR = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    public static final long[][] HALVES = {{2, 1}, {1, 3}};

    /** Le montant a partager (centimes) et les parts. */
    public static final long BILL = 10000;
    public static final int[] SHARES = {1, 1, 1};
    public static final int[] WEIGHTS = {50, 30, 20};

    private Data() {
    }
}
