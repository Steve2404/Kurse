package ch13_concurrency.projects.p02_primes;

/**
 * Les donnees du projet 2 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** L'intervalle a explorer [LOW, HIGH), decoupe en SEGMENTS morceaux egaux ; et la taille du pool. */
    public static final int LOW = 1_000_000;
    public static final int HIGH = 3_000_000;
    public static final int SEGMENTS = 8;
    public static final int THREADS = 4;

    /** Les miroirs de telechargement : un seul repond. */
    public static final String[] MIRRORS = {"A:panne", "B:panne", "C:ok", "D:panne"};

    private Data() {
    }
}
