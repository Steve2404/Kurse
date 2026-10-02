package ch5_methods.projects.p07_shop;

/**
 * Les donnees du projet 7 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Les produits : "reference nom prix_en_centimes stock". */
    public static final String[] PRODUCTS = {"K01 Clavier 4990 5", "S02 Souris 1990 12", "E03 Ecran 18990 2", "C04 Cable 790 30",
            "H05 Casque 7990 4", "W06 Webcam 5490 0", "T07 Tapis 1290 7"};

    /**
     * Les commandes, dans l'ordre :
     * "VENTE ref quantite", "STOCK ref q1 q2 ..." (plusieurs livraisons), "REMISE ref pourcentage", "REMISE ref montantc" (montant fixe en centimes, suffixe c), "INFO position" (a partir de 1).
     */
    public static final String[] ORDERS = {"VENTE K01 2", "VENTE E03 3", "STOCK W06 2 3", "VENTE W06 4", "VENTE X99 1", "STOCK C04",
            "REMISE H05 25", "VENTE H05 3", "INFO 3", "INFO 9", "VENTE K01 3", "STOCK E03 1 1 1 1", "VENTE S02 10", "REMISE T07 90c"};

    /** Le budget du client pour le meilleur panier (en centimes). */
    public static final long BUDGET = 15000;

    private Data() {
    }
}
