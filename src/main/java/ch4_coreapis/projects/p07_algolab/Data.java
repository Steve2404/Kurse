package ch4_coreapis.projects.p07_algolab;

/**
 * Les donnees du projet 7 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Les nombres a trier, dedoublonner et parcourir. */
    public static final int[] NUMBERS = {29, 3, 17, 8, 3, 42, 15, 8, 1, 23};

    /** La somme cherchee par les deux pointeurs. */
    public static final int TARGET = 32;

    /** Les requetes de somme d'intervalle [debut, fin] (fin INCLUSE), sur NUMBERS non trie. */
    public static final int[][] RANGES = {{0, 4}, {2, 2}, {5, 9}, {0, 9}};

    /** La taille de la fenetre glissante. */
    public static final int WINDOW = 3;

    /** Les gains et pertes de chaque jour (Kadane). */
    public static final int[] PROFITS = {-2, 1, -3, 4, -1, 2, 1, -5, 4};

    /** La limite du crible d'Eratosthene. */
    public static final int SIEVE_LIMIT = 60;

    /** La matrice a transposer, tourner et parcourir en spirale. */
    public static final int[][] MATRIX = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12}};

    /** Le nombre de lignes du triangle de Pascal. */
    public static final int PASCAL_ROWS = 7;

    /** Le labyrinthe : S depart, E sortie, # mur, . passage. */
    public static final String[] MAZE = {
            "S.#.......",
            ".##.####.#",
            "....#....#",
            "#.#.#.##..",
            "..#...#E#.",
            ".####.#.#.",
            "......#..."};

    private Data() {
    }
}
