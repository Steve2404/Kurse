package ch15_jdbc.projects.p01_library;

/**
 * Les donnees du projet 1 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Une base H2 EN MEMOIRE : elle nait a la 1re connexion et meurt quand la derniere se ferme. */
    public static final String URL = "jdbc:h2:mem:p01_library";
    public static final String USER = "sa";
    public static final String PASSWORD = "";

    /** La table des livres. */
    public static final String SCHEMA = "CREATE TABLE books (isbn VARCHAR(20) PRIMARY KEY, title VARCHAR(80) NOT NULL, "
            + "author VARCHAR(40) NOT NULL, pub_year INT NOT NULL, pages INT)";

    /** "isbn|titre|auteur|annee|pages" ; pages vide = inconnu (NULL en base). */
    public static final String[] BOOKS = {
            "978-01|Le Petit Prince|Saint-Exupery|1943|96",
            "978-02|Vol de nuit|Saint-Exupery|1931|",
            "978-03|Terre des hommes|Saint-Exupery|1939|224",
            "978-04|L'Etranger|Camus|1942|184",
            "978-05|La Peste|Camus|1947|336",
            "978-06|Le Premier Homme|Camus|1994|",
            "978-07|Les Miserables|Hugo|1862|1900",
            "978-08|Notre-Dame de Paris|Hugo|1831|940",
            "978-09|Le Dernier Jour d'un condamne|Hugo|1829|112",
            "978-10|Le Rouge et le Noir|Stendhal|1830|576"};

    /** Une saisie malveillante, et une saisie innocente qui contient une apostrophe. */
    public static final String INJECTION = "x' OR '1'='1";
    public static final String APOSTROPHE = "L'Etranger";

    private Data() {
    }
}
