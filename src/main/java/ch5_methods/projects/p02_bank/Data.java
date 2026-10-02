package ch5_methods.projects.p02_bank;

/**
 * Les donnees du projet 2 (DONNEES, ne pas modifier).
 */
public final class Data {

    /**
     * Les comptes a ouvrir, dans l'ordre (les numeros sont donc 1, 2, 3, 4).
     * "C nom solde" : compte courant ; "P nom solde decouvert" : compte premium. Montants en CENTIMES.
     */
    public static final String[] ACCOUNTS = {"C Alice 120000", "P Bob 50000 20000", "C Chloe 0", "P Dan 300000 50000"};

    /** Les operations, dans l'ordre. Montants en CENTIMES. */
    public static final String[] OPERATIONS = {
            "DEPOT 3 25000",
            "RETRAIT 1 150000",
            "RETRAIT 2 60000",
            "VIREMENT 4 3 75050",
            "RETRAIT 2 15000",
            "VIREMENT 1 9 100",
            "RETRAIT 3 1000",
            "RETRAIT 3 1000",
            "RETRAIT 3 1000",
            "DEPOT 1 -500",
            "INTERETS",
            "DEPOT 2 50000",
            "RELEVE 2",
            "RELEVE 3"};

    private Data() {
    }
}
