package ch11_exceptions.projects.p01_bank;

/**
 * Les donnees du projet 1 (DONNEES, ne pas modifier). Les montants sont en centimes.
 */
public final class Data {

    /** "id titulaire solde" ; le mot "gele" en 4e position cree un compte gele. */
    public static final String[] ACCOUNTS = {"A1 alice 50000", "B2 bob 12000", "C3 carla 30000 gele", "D4 dan 800"};

    /** Les operations du guichet, dans l'ordre. Certaines sont volontairement fausses. */
    public static final String[] OPERATIONS = {
            "DEPOT A1 2500",
            "RETRAIT B2 20000",
            "VIREMENT A1 B2 10000",
            "RETRAIT Z9 100",
            "DEPOT A1 -5",
            "DEPOT A1 12x",
            "RETRAIT C3 100",
            "VIREMENT B2 C3 5000",
            "VIREMENT D4 A1 999999",
            "PARTAGE A1 0",
            "PARTAGE A1 3",
            "RETRAIT",
            "BONUS A1 100"};

    /** Les frais mensuels preleves sur chaque compte a la fin. */
    public static final long FEE = 1000;

    private Data() {
    }
}
