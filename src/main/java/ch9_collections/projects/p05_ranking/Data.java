package ch9_collections.projects.p05_ranking;

/**
 * Les donnees du projet 5 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** "nom equipe score age bonus" ; bonus "-" = aucun (null). */
    public static final String[] PLAYERS = {
            "Lea rouge 1200 31 50", "Hugo bleu 1500 25 -", "Ines rouge 1500 31 20", "Adam vert 980 42 -", "Zoe bleu 1200 25 80",
            "Bob vert 1500 37 10", "Emma rouge 870 29 -", "Noah bleu 1200 42 30"};

    /** Les intervalles (debut fin) a fusionner et a planifier. */
    public static final int[][] INTERVALS = {{1, 3}, {8, 10}, {2, 6}, {15, 18}, {9, 12}, {17, 20}, {5, 7}};

    /** Le flux de valeurs pour la mediane glissante. */
    public static final int[] STREAM = {5, 15, 1, 3, 8, 7, 9, 10, 20, 2};

    private Data() {
    }
}
