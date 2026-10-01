package ch15_jdbc.exercises;

import ch15_jdbc.ExerciseChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * EXERCICE 20 (CAPSTONE) - Un service d'emprunts : transaction, cle generee, batch et erreurs par SQLState, sur H2 seul (niveau : capstone)
 * =========================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_JdbcUrlAndDriverManager.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * La bibliotheque :
 *
 *   members (id VARCHAR PRIMARY KEY, name VARCHAR)
 *   books   (isbn VARCHAR PRIMARY KEY, title VARCHAR, stock INT CHECK (stock >= 0))
 *   loans   (id INT AUTO_INCREMENT PRIMARY KEY, member_id VARCHAR, isbn VARCHAR)
 *
 * Emprunter = DEUX ordres qui doivent reussir ENSEMBLE (diminuer le
 * stock, ajouter l'emprunt) : une transaction. Le code metier ne doit
 * jamais voir de SQLException brute : on la traduit en exception metier
 * selon la FAMILLE de son SQLState (les 2 premiers caracteres, communs a
 * tous les fournisseurs : "23" = contrainte violee, "42" = erreur de
 * syntaxe ou objet inconnu).
 *
 *
 * ==================================================================
 * TODO 1 : family(e)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. state = e.getSQLState() ; null -> "OTHER".
 *   2. Commence par "23" -> "CONSTRAINT" ; par "42" -> "SYNTAX" ; sinon "OTHER".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non, mais family est la boite magique des TODO 2 et 3.
 *
 *
 * ==================================================================
 * TODO 2 : borrow(conn, memberId, isbn)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Dans UNE transaction :
 *   1. UPDATE books SET stock = stock - 1 WHERE isbn = ? AND stock > 0 ;
 *      0 ligne touchee -> rollback et LibraryException("plus d'exemplaire : " + isbn).
 *   2. INSERT INTO loans (member_id, isbn) VALUES (?, ?) avec Statement.RETURN_GENERATED_KEYS.
 *   3. commit ; rendre l'id genere (getGeneratedKeys()).
 * Toute SQLException -> rollback, puis LibraryException("emprunt refuse : " + family(e)).
 * Toujours remettre l'auto-commit (finally).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : family.
 *
 *
 * ==================================================================
 * TODO 3 : registerAll(conn, members)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Inscrire plusieurs membres ({id, nom}) en UN seul voyage (batch), dans
 * une transaction : tout ou rien. Rendre le nombre d'inscrits. Un id deja
 * pris fait echouer executeBatch (BatchUpdateException, une SQLException) :
 * rollback, puis LibraryException("inscription refusee : " + family(e)).
 *
 * -- Le plan --
 *
 *   1. setAutoCommit(false) ; PreparedStatement INSERT INTO members VALUES (?, ?).
 *   2. Pour chaque membre : setString, setString, addBatch().
 *   3. executeBatch() -> int[] ; commit ; rendre la somme (ou la longueur).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : family.
 *
 *
 * ==================================================================
 * TODO 4 : loansReport(conn)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   -> [Hugo: 1984, Lea: Dune, Lea: Le Hobbit] (nom du membre, titre ; tries par nom puis titre)
 *
 * -- Le plan --
 *
 *   1. SELECT m.name, b.title FROM loans l JOIN members m ON ... JOIN books b ON ... ORDER BY m.name, b.title.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) ; try (ResultSet keys = ps.getGeneratedKeys()) { keys.next(); keys.getInt(1); }
 *   - throw new LibraryException("...", e) garde la cause.
 */
public class Exercise20_LoanServiceCapstone {

    public static class LibraryException extends Exception {
        private static final long serialVersionUID = 1L;

        public LibraryException(String message) {
            super(message);
        }

        public LibraryException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static String family(SQLException e) {
        throw new UnsupportedOperationException("TODO 1 : implementer family()");
    }

    public static int borrow(Connection conn, String memberId, String isbn) throws LibraryException, SQLException {
        throw new UnsupportedOperationException("TODO 2 : implementer borrow()");
    }

    public static int registerAll(Connection conn, List<String[]> members) throws LibraryException, SQLException {
        throw new UnsupportedOperationException("TODO 3 : implementer registerAll()");
    }

    public static List<String> loansReport(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 4 : implementer loansReport()");
    }

    public static void main(String[] args) throws Exception {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:ex20")) {
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("CREATE TABLE members (id VARCHAR(5) PRIMARY KEY, name VARCHAR(30))");
                st.executeUpdate("CREATE TABLE books (isbn VARCHAR(5) PRIMARY KEY, title VARCHAR(50), stock INT CHECK (stock >= 0))");
                st.executeUpdate("CREATE TABLE loans (id INT AUTO_INCREMENT PRIMARY KEY, member_id VARCHAR(5), isbn VARCHAR(5))");
                st.executeUpdate("INSERT INTO books VALUES ('B1', 'Dune', 2), ('B5', '1984', 1), ('B8', 'Le Hobbit', 1)");
            }

            String duplicateFamily = "";
            String syntaxFamily = "";
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("INSERT INTO books VALUES ('B1', 'Doublon', 1)");
            } catch (SQLException e) {
                duplicateFamily = family(e);
            }
            try (Statement st = conn.createStatement()) {
                st.executeQuery("SELECT * FROM table_absente");
            } catch (SQLException e) {
                syntaxFamily = family(e);
            }
            ExerciseChecker.check("family : cle en double -> CONSTRAINT, table inconnue -> SYNTAX, sans etat -> OTHER",
                    "CONSTRAINT".equals(duplicateFamily) && "SYNTAX".equals(syntaxFamily) && "OTHER".equals(family(new SQLException("x"))));

            ExerciseChecker.check("registerAll(Lea, Hugo) == 2", registerAll(conn, List.of(new String[]{"M1", "Lea"}, new String[]{"M2", "Hugo"})) == 2);
            String refused = "";
            try {
                registerAll(conn, List.of(new String[]{"M3", "Ines"}, new String[]{"M1", "Doublon"}));
            } catch (LibraryException e) {
                refused = e.getMessage();
            }
            ExerciseChecker.check("registerAll avec un id deja pris -> inscription refusee : CONSTRAINT, et Ines n'est PAS inscrite (tout ou rien)",
                    "inscription refusee : CONSTRAINT".equals(refused) && count(conn, "SELECT COUNT(*) FROM members") == 2 && conn.getAutoCommit());

            int first = borrow(conn, "M1", "B1");
            int second = borrow(conn, "M1", "B8");
            int third = borrow(conn, "M2", "B5");
            ExerciseChecker.check("borrow : 3 emprunts, cles generees 1, 2, 3, stock de B1 passe a 1",
                    first == 1 && second == 2 && third == 3 && count(conn, "SELECT stock FROM books WHERE isbn = 'B1'") == 1);
            String noStock = "";
            try {
                borrow(conn, "M2", "B5");
            } catch (LibraryException e) {
                noStock = e.getMessage();
            }
            ExerciseChecker.check("borrow d'un livre sans exemplaire -> plus d'exemplaire : B5, rien n'est enregistre",
                    "plus d'exemplaire : B5".equals(noStock) && count(conn, "SELECT COUNT(*) FROM loans") == 3 && conn.getAutoCommit());
            ExerciseChecker.check("loansReport == [Hugo: 1984, Lea: Dune, Lea: Le Hobbit]",
                    List.of("Hugo: 1984", "Lea: Dune", "Lea: Le Hobbit").equals(loansReport(conn)));
        }

        ExerciseChecker.summary();
    }

    static int count(Connection conn, String sql) throws SQLException {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return rs.getInt(1);
        }
    }
}
