package ch9_collections.projects.p01_inventory;

/**
 * Les donnees du projet 1 (DONNEES, ne pas modifier). "reference nom categorie stock prix_en_centimes".
 */
public final class Data {

    public static final String[] ITEMS = {
            "K01 clavier info 12 4990", "S02 souris info 0 1990", "E03 ecran info 3 18990", "C04 cable info 40 790",
            "T05 tasse cuisine 25 890", "P06 poele cuisine 0 3490", "L07 lampe maison 6 2490", "V08 vase maison 2 4590",
            "B09 bouilloire cuisine 4 2990", "H10 horloge maison 9 1990"};

    /** Les quantites vendues dans la journee (des Integer, avec doublons). */
    public static final int[] SALES = {3, 7, 3, 12, 7, 3, 1};

    /** Le stock minimal souhaite par article, et le budget de reassort en centimes. */
    public static final int TARGET_STOCK = 5;
    public static final long BUDGET = 30_000;

    /** La graine du melange. */
    public static final long SEED = 9;

    private Data() {
    }
}
