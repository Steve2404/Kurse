package ch8_lambdas.projects.p07_orders;

/**
 * Les donnees du projet 7 (DONNEES, ne pas modifier). Montants en centimes, poids en grammes.
 */
public final class Data {

    /** "reference prix poids". */
    public static final String[] CATALOG = {"BOOK 1590 400", "PEN 250 20", "LAMP 3490 1500", "MUG 890 350"};

    /** "id client niveau ref:quantite ...". Niveaux : gold, silver, bronze, none. */
    public static final String[] PURCHASES = {
            "P1 alice gold BOOK:2 PEN:5", "P2 bob silver LAMP:1 MUG:2", "P3 chloe none", "P4 dan bronze PEN:40",
            "P5 eve gold LAMP:2 BOOK:1 MUG:1 XYZ:1", "P6 fred none MUG:3", "P7 gina silver LAMP:3 BOOK:2"};

    /** "code type valeur minimum" : fixed = remise fixe, percent = pourcentage ; applicable si le montant >= minimum. */
    public static final String[] PROMOS = {"MINUS5 fixed 500 3000", "TEN percent 10 0", "BIG percent 20 10000"};

    /** Les regles de validation. */
    public static final String[] RULES = {"not-empty", "known-skus", "max-qty 30"};

    private Data() {
    }
}
