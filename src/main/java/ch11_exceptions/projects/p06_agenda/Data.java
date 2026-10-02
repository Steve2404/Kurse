package ch11_exceptions.projects.p06_agenda;

/**
 * Les donnees du projet 6 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Des rendez-vous saisis a la main : "date et heure|titre", dans des formats varies. */
    public static final String[] RAW = {"2026-03-14T09:30|lancement", "20/03/2026 14:00|revue", "March 27, 2026 4:00 PM|demo client",
            "2026-02-30 10:00|budget", "31/04/2026 08:00|audit", "2026-04-32 10:00|fantome", "demain matin|flou"};

    /** Les motifs essayes, dans l'ordre, apres ISO_LOCAL_DATE_TIME ; le 2e se lit en anglais (noms de mois). */
    public static final String[] PATTERNS = {"dd/MM/uuuu HH:mm", "MMMM d, uuuu h:mm a", "uuuu-MM-dd HH:mm"};

    /** Recurrence : le N-ieme jour de semaine du mois, a partir d'un mois de depart. */
    public static final int NTH = 2;
    public static final String WEEKDAY = "TUESDAY";
    public static final String FIRST_MONTH = "2026-03";
    public static final int OCCURRENCES = 4;

    /** Jours ouvres : depart, nombre de jours a ajouter, jours feries. */
    public static final String START = "2026-04-01";
    public static final int BUSINESS_DAYS = 10;
    public static final String[] HOLIDAYS = {"2026-04-06", "2026-05-01"};

    /** Une reunion fixee a Paris, et les villes ou l'afficher. */
    public static final String MEETING = "2026-03-27T16:00";
    public static final String[] ZONES = {"Europe/Paris", "America/New_York", "Asia/Tokyo"};

    /** La date d'exemple des styles localises. */
    public static final String SAMPLE = "2026-03-14T09:30";
    public static final String[] LOCALES = {"en-US", "fr-FR", "de-DE"};

    private Data() {
    }
}
