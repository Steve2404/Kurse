package ch9_collections.projects.p02_words;

/**
 * Les donnees du projet 2 (DONNEES, ne pas modifier). Les documents sont numerotes a partir de 1.
 */
public final class Data {

    public static final String[] DOCS = {
            "Java aime les collections : la liste, la map et le set.",
            "Une map associe une cle a une valeur ; le set refuse les doublons.",
            "Les streams de Java traitent les collections, la liste comme le set.",
            "Chien, niche et chine : des anagrammes. Rame, mare et arme aussi !"};

    /** Les mots vides, ignores. */
    public static final String[] STOP = {"la", "le", "les", "et", "a", "une", "de", "des", "comme", "aussi"};

    /** Les requetes : "mot1 & mot2" (et), "mot1 | mot2" (ou), "mot1 - mot2" (sauf). */
    public static final String[] QUERIES = {"java & collections", "map | streams", "set - map", "liste & chien"};

    /** Combien de mots les plus frequents afficher. */
    public static final int TOP = 5;

    private Data() {
    }
}
