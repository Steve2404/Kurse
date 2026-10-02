package ch5_methods.projects.p06_recursion;

/**
 * Les donnees du projet 6 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Le tableau a trier (tri fusion et tri rapide). */
    public static final int[] UNSORTED = {38, 27, 43, 3, 9, 82, 10, 3};

    /** La carte : # = terre, . = eau. Une ile = des # relies horizontalement ou verticalement. */
    public static final String[] MAP = {
            "##..#....#",
            "#...##..##",
            "..#......#",
            ".###..#...",
            "..#..###..",
            "......#..#"};

    /** Les pieces disponibles et la somme a rendre (en centimes). */
    public static final int[] COINS = {1, 2, 5, 10, 20, 50};
    public static final int AMOUNT = 100;

    private Data() {
    }
}
