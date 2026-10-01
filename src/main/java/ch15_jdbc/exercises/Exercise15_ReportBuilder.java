package ch15_jdbc.exercises;

import ch15_jdbc.ExerciseChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * EXERCICE 15 - Un generateur de rapports pour N'IMPORTE quelle requete : ResultSetMetaData + parametres (niveau : avance)
 * ========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_JdbcUrlAndDriverManager.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le code ne connait PAS la requete a l'avance : ni le nombre de
 * colonnes, ni leurs noms. ResultSetMetaData (rs.getMetaData()) les
 * donne : getColumnCount(), getColumnLabel(i) (le nom, ou l'alias AS),
 * getColumnTypeName(i). Les colonnes sont numerotees a partir de 1.
 *
 * Le rapport est un tableau texte : chaque colonne aussi large que sa
 * plus longue valeur (titre compris), valeurs alignees a gauche, " | "
 * entre les colonnes, une ligne de tirets "-" sous les titres ("-+-" entre
 * les colonnes), et "NULL" pour une valeur absente.
 *
 *   TITRE     | AUTEUR
 *   ----------+-------
 *   1984      | Orwell
 *   Fondation | Asimov
 *
 *
 * ==================================================================
 * TODO 1 : rows(conn, sql, params)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Executer sql (avec des ?) et rendre une liste de lignes ; la PREMIERE
 * ligne contient les titres (getColumnLabel), les suivantes les valeurs
 * en texte (getString, "NULL" si null).
 *
 * -- Le plan --
 *
 *   1. PreparedStatement ; setObject(i + 1, params[i]) pour chaque parametre.
 *   2. meta = rs.getMetaData() ; n = meta.getColumnCount().
 *   3. Ligne des titres, puis une ligne par next().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : render(conn, sql, params)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. table = rows(...) ; largeur de chaque colonne = max des longueurs.
 *   2. Chaque ligne : les cellules completees a droite par des espaces, jointes par " | ",
 *      puis les espaces de fin retires (stripTrailing).
 *   3. Apres la ligne des titres : des "-" de la largeur de chaque colonne, joints par "-+-".
 *   4. Lignes separees par "\n".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : rows (TODO 1), et "completer un texte a une largeur" (String.format("%-" + n + "s", x)).
 *
 *
 * ==================================================================
 * TODO 3 : columnTypes(conn, sql)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   SELECT title, pub_year FROM books -> [TITLE:CHARACTER VARYING, PUB_YEAR:INTEGER] (noms de types de H2)
 *
 * -- Le plan --
 *
 *   1. getColumnLabel(i) + ":" + getColumnTypeName(i), pour i de 1 a getColumnCount().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. (Les noms de types changent d'un fournisseur a l'autre : voir Exercise14.)
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - ResultSetMetaData meta = rs.getMetaData();
 *   - "-".repeat(n) ; String.join(" | ", cellules)
 */
public class Exercise15_ReportBuilder {

    public static List<List<String>> rows(Connection conn, String sql, Object... params) throws SQLException {
        throw new UnsupportedOperationException("TODO 1 : implementer rows()");
    }

    public static String render(Connection conn, String sql, Object... params) throws SQLException {
        throw new UnsupportedOperationException("TODO 2 : implementer render()");
    }

    public static List<String> columnTypes(Connection conn, String sql) throws SQLException {
        throw new UnsupportedOperationException("TODO 3 : implementer columnTypes()");
    }

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:ex15")) {
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("CREATE TABLE books (isbn VARCHAR(10) PRIMARY KEY, title VARCHAR(100), author VARCHAR(50), pub_year INT, note VARCHAR(20))");
                st.executeUpdate("INSERT INTO books VALUES ('B1', 'Dune', 'Herbert', 1965, 'culte'), ('B2', 'Fondation', 'Asimov', 1951, NULL),"
                        + " ('B5', '1984', 'Orwell', 1949, NULL), ('B8', 'Le Hobbit', 'Tolkien', 1937, 'enfance')");
            }
            List<List<String>> table = rows(conn, "SELECT title, note FROM books WHERE pub_year < ? ORDER BY pub_year", 1950);
            ExerciseChecker.check("rows : titres puis valeurs, NULL pour une valeur absente",
                    List.of(List.of("TITLE", "NOTE"), List.of("Le Hobbit", "enfance"), List.of("1984", "NULL")).equals(table));

            String report = render(conn, "SELECT title AS titre, author AS auteur FROM books WHERE pub_year BETWEEN ? AND ? ORDER BY title", 1940, 1960);
            ExerciseChecker.check("render : colonnes alignees, alias AS en titre",
                    ("TITRE     | AUTEUR\n"
                            + "----------+-------\n"
                            + "1984      | Orwell\n"
                            + "Fondation | Asimov").equals(report));
            ExerciseChecker.check("render sans parametre ni ligne : seulement les titres et les tirets",
                    "ISBN\n----".equals(render(conn, "SELECT isbn FROM books WHERE 1 = 0")));
            ExerciseChecker.check("columnTypes == [TITLE:CHARACTER VARYING, PUB_YEAR:INTEGER]",
                    List.of("TITLE:CHARACTER VARYING", "PUB_YEAR:INTEGER").equals(columnTypes(conn, "SELECT title, pub_year FROM books")));
        }

        ExerciseChecker.summary();
    }
}
