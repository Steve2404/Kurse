package ch13_concurrency.projects.p03_bank;

/**
 * Les donnees du projet 3 (DONNEES, ne pas modifier). Montants en centimes.
 */
public final class Data {

    public static final int ACCOUNTS = 6;
    public static final long INITIAL = 100_000;
    public static final int TRANSFERS = 20_000;
    public static final int THREADS = 8;
    public static final long FEE = 3;

    /** Le i-eme virement, genere de facon deterministe : {source, cible, montant}. */
    public static long[] transfer(int i) {
        long x = i * 2_654_435_761L + 12_345;
        int from = (int) (x % ACCOUNTS);
        int to = (int) ((x / 7 + 1 + from) % ACCOUNTS);
        if (to == from) {
            to = (from + 1) % ACCOUNTS;
        }
        return new long[] {from, to, 100 + x % 900};
    }

    private Data() {
    }
}
