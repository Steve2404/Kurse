package ch8_lambdas.projects.p03_events;

/**
 * Les donnees du projet 3 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** La graine du generateur pseudo-aleatoire. */
    public static final long SEED = 42;

    /** Le nombre de guichets, la duree d'ouverture (minutes) et les bornes des tirages (minutes, bornes incluses). */
    public static final int TELLERS = 2;
    public static final int CLOSING = 60;
    public static final int ARRIVAL_MIN = 1;
    public static final int ARRIVAL_MAX = 5;
    public static final int SERVICE_MIN = 4;
    public static final int SERVICE_MAX = 11;

    /** Le nombre de lignes du journal a afficher. */
    public static final int LOG_LINES = 14;

    private Data() {
    }
}
