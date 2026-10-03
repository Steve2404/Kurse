package ch15_jdbc.projects.p06_report;

/**
 * Les donnees du projet 6 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** La base de depart, et la base vide dans laquelle on rechargera la copie. */
    public static final String URL = "jdbc:h2:mem:p06_report";
    public static final String COPY_URL = "jdbc:h2:mem:p06_copy";
    public static final String USER = "sa";
    public static final String PASSWORD = "";

    /** Le schema : des cles etrangeres relient les tables (books depend de authors et de publishers...). */
    public static final String[] SCHEMA = {
            "CREATE TABLE tags (id INT PRIMARY KEY, label VARCHAR(15) NOT NULL)",
            "CREATE TABLE publishers (id INT PRIMARY KEY, name VARCHAR(30) NOT NULL)",
            "CREATE TABLE authors (id INT PRIMARY KEY, name VARCHAR(30) NOT NULL, country VARCHAR(20))",
            "CREATE TABLE books (id INT PRIMARY KEY, title VARCHAR(40) NOT NULL, author_id INT NOT NULL REFERENCES authors(id), "
                    + "publisher_id INT REFERENCES publishers(id), price DECIMAL(6,2) NOT NULL, published DATE)",
            "CREATE TABLE reviews (id INT PRIMARY KEY, book_id INT NOT NULL REFERENCES books(id), stars INT NOT NULL, note VARCHAR(40))"};

    public static final String[] INSERTS = {
            "INSERT INTO tags VALUES (1, 'classique'), (2, 'roman')",
            "INSERT INTO publishers VALUES (1, 'Gallimard'), (2, 'Folio')",
            "INSERT INTO authors VALUES (1, 'Camus', 'France'), (2, 'Hugo', 'France'), (3, 'Kafka', NULL)",
            "INSERT INTO books VALUES (1, 'La Peste', 1, 1, 9.50, DATE '1947-06-10'), (2, 'Les Miserables', 2, 2, 12.90, DATE '1862-04-03'), "
                    + "(3, 'Le Proces', 3, NULL, 7.20, NULL), (4, 'L''Etranger', 1, 2, 6.80, DATE '1942-05-19')",
            "INSERT INTO reviews VALUES (1, 1, 5, 'magistral'), (2, 1, 4, NULL), (3, 2, 5, 'immense'), (4, 4, 3, 'l''absurde'), (5, 3, 4, NULL)"};

    /** Les rapports a afficher avec le moteur generique. */
    public static final String[] REPORTS = {
            "SELECT b.title AS titre, a.name AS auteur, b.price AS prix, b.published AS parution FROM books b JOIN authors a ON a.id = b.author_id ORDER BY b.price DESC",
            "SELECT a.country AS pays, COUNT(r.id) AS avis, CAST(AVG(r.stars * 1.0) AS DECIMAL(3,1)) AS moyenne FROM authors a "
                    + "JOIN books b ON b.author_id = a.id JOIN reviews r ON r.book_id = b.id GROUP BY a.country ORDER BY a.country"};

    private Data() {
    }
}
