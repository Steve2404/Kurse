package ch14_io.projects.p06_duplicates;

/**
 * Les donnees du projet 6 (DONNEES, ne pas modifier).
 */
public final class Data {

    public static final String SANDBOX = "build/ch14/p06_duplicates";

    /** "chemin|contenu|date de modification (ISO-8601, UTC)". */
    public static final String[] FILES = {
            "photos/plage.jpg|PLAGE-PIXELS-0001|2026-07-14T10:00:00Z",
            "photos/copie de plage.jpg|PLAGE-PIXELS-0001|2026-07-20T09:30:00Z",
            "photos/montagne.jpg|MONTAGNE-PIXELS-02|2026-08-02T16:45:00Z",
            "docs/cv.pdf|CV-2026-VERSION-A|2026-03-01T08:00:00Z",
            "docs/archives/cv-ancien.pdf|CV-2026-VERSION-B|2025-11-30T18:00:00Z",
            "docs/archives/cv-final.pdf|CV-2026-VERSION-A|2026-03-02T08:00:00Z",
            "musique/titre.mp3|MP3-DATA-XYZ|2026-01-05T12:00:00Z",
            "cache/tmp1.bin|PLAGE-PIXELS-0001|2026-09-01T00:00:00Z",
            "cache/tmp2.bin|0000000000000000|2026-09-01T00:00:00Z"};

    /** Le dossier ignore pendant le parcours. */
    public static final String SKIPPED = "cache";

    private Data() {
    }
}
