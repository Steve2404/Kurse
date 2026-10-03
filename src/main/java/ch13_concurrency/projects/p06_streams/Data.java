package ch13_concurrency.projects.p06_streams;

/**
 * Les donnees du projet 6 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Rayon du disque pour le comptage des points entiers. */
    public static final int RADIUS = 20_000;

    /** On cherche la plus longue suite de Collatz pour un depart dans [1, COLLATZ_LIMIT). */
    public static final int COLLATZ_LIMIT = 1_000_000;

    public static final String TEXT = "le parallele ne garantit pas l ordre mais le resultat d une reduction associative reste le meme "
            + "un stream parallele decoupe la source en morceaux traites par plusieurs threads puis combine les resultats partiels";

    private Data() {
    }
}
