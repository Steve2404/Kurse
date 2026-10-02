package ch5_methods.projects.p01_stats;

/**
 * Les donnees du projet 1 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Les temperatures relevees sur 10 jours. */
    public static final int[] TEMPS = {12, 15, 9, 22, 18, 15, 7, 25, 15, 11};

    /** Les ventes de trois magasins (une ligne par magasin). */
    public static final int[] SHOP_A = {3, 8, 1};
    public static final int[] SHOP_B = {};
    public static final int[] SHOP_C = {9, 4};

    /** Les etiquettes et valeurs de l'histogramme. */
    public static final String[] LABELS = {"lun", "mar", "mer", "jeu", "ven"};
    public static final int[] VOTES = {3, 5, 1, 4, 2};

    private Data() {
    }
}
