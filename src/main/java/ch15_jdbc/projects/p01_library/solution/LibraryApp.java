package ch15_jdbc.projects.p01_library.solution;

import ch15_jdbc.projects.p01_library.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * SOLUTION du projet 1 - le catalogue d'une bibliotheque : connexion, Statement, PreparedStatement, ResultSet.
 */
public class LibraryApp {

    // Le contrat de l'URL JDBC : "jdbc" : sous-protocole (le fournisseur) : le reste (propre au fournisseur).
    static String urlParts(String url) {
        String[] parts = url.split(":", 3);
        return String.join(" | ", parts);
    }

    public static void main(String[] args) throws SQLException {
        Statement kept;
        try (Connection conn = DriverManager.getConnection(Data.URL, Data.USER, Data.PASSWORD)) {
            System.out.println("connexion : " + urlParts(Data.URL) + " ; autoCommit " + conn.getAutoCommit() + " ; valide " + conn.isValid(1));

            // execute rend true seulement si le resultat est un ResultSet ; executeUpdate d'un DDL rend 0.
            kept = conn.createStatement();
            boolean isQuery = kept.execute(Data.SCHEMA);
            int ddl = kept.executeUpdate("CREATE INDEX idx_author ON books(author)");
            System.out.println("schema : execute " + isQuery + ", executeUpdate " + ddl);

            Catalog catalog = new Catalog(conn);
            int rows = 0;
            int withoutPages = 0;
            for (String line : Data.BOOKS) {
                Book book = Book.parse(line);
                rows += catalog.add(book);
                if (book.pages() == null) {
                    withoutPages++;
                }
            }
            System.out.println("insertion : " + Data.BOOKS.length + " livres, " + rows + " lignes, " + withoutPages + " sans pages");

            List<String> camus = new ArrayList<>();
            for (Book b : catalog.byAuthor("Camus")) {
                camus.add(b.shortName());
            }
            System.out.println("Camus : " + camus);
            System.out.println("find 978-05 : " + catalog.find("978-05").orElseThrow());
            System.out.println("find 978-06 : pages " + catalog.find("978-06").orElseThrow().pages());
            System.out.println("find 999 : " + catalog.find("999"));

            // On demande des pages jusqu'a la premiere vide.
            int page = 1;
            List<String> titles = catalog.search("le", page, 3);
            while (!titles.isEmpty()) {
                System.out.println("recherche \"le\" page " + page + " : " + titles);
                page++;
                titles = catalog.search("le", page, 3);
            }
            System.out.println("recherche \"le\" : arret a la page " + page + " (vide)");
            System.out.println("par siecle : " + catalog.countByCentury());

            int updated = catalog.addPages("Hugo", 10);
            int deleted = catalog.deleteBefore(1830);
            System.out.println("Hugo +10 pages : " + updated + " lignes ; supprimes avant 1830 : " + deleted + " ; inconnu : "
                    + catalog.addPages("Zola", 10) + " ; restent " + catalog.count());

            pitfalls(conn);

            System.out.println("injection :");
            System.out.println("  Statement avec " + Data.INJECTION + " : " + catalog.countByTitleUnsafe(Data.INJECTION) + " livres");
            System.out.println("  PreparedStatement avec " + Data.INJECTION + " : " + catalog.countByTitle(Data.INJECTION) + " livre");
            try {
                catalog.countByTitleUnsafe(Data.APOSTROPHE);
            } catch (SQLException e) {
                System.out.println("  Statement avec " + Data.APOSTROPHE + " : SQLState " + e.getSQLState());
            }
            System.out.println("  PreparedStatement avec " + Data.APOSTROPHE + " : " + catalog.countByTitle(Data.APOSTROPHE) + " livre");
        }
        // La connexion est fermee : ses Statement ne sont plus utilisables.
        try {
            kept.executeQuery("SELECT 1");
        } catch (SQLException e) {
            System.out.println("apres le try : Statement inutilisable, SQLState " + e.getSQLState());
        }
    }

    // Chaque erreur JDBC est une SQLException ; getSQLState() donne un code standard (5 caracteres).
    private static void pitfalls(Connection conn) throws SQLException {
        System.out.println("pieges :");
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT title FROM books ORDER BY isbn");
            try {
                rs.getString(1);
            } catch (SQLException e) {
                System.out.println("  getString avant next : " + e.getSQLState());
            }
            rs.next();
            try {
                rs.getString(0);
            } catch (SQLException e) {
                System.out.println("  colonne 0 : " + e.getSQLState());
            }
            try {
                rs.getString("nope");
            } catch (SQLException e) {
                System.out.println("  colonne inconnue : " + e.getSQLState());
            }
            try {
                st.executeUpdate("SELECT * FROM books");
            } catch (SQLException e) {
                System.out.println("  executeUpdate d'un SELECT : " + e.getSQLState());
            }
            try {
                st.executeQuery("DELETE FROM books WHERE pub_year > 3000");
            } catch (SQLException e) {
                System.out.println("  executeQuery d'un DELETE : " + e.getSQLState());
            }
            try {
                st.executeQuery("SELECT * FROM livres");
            } catch (SQLException e) {
                System.out.println("  table inconnue : " + e.getSQLState());
            }
            // Un Statement n'a qu'UN ResultSet ouvert : une nouvelle requete ferme l'ancien.
            ResultSet first = st.executeQuery("SELECT isbn FROM books");
            st.executeQuery("SELECT title FROM books");
            System.out.println("  1er ResultSet ferme par la 2e requete : " + first.isClosed());
            try {
                first.next();
            } catch (SQLException e) {
                System.out.println("  next sur un ResultSet ferme : " + e.getSQLState());
            }
        }
    }
}
