package jdbc.exercises;

import jdbc.ExerciseChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 7 - Les Savepoint : annuler PARTIELLEMENT une transaction (niveau : difficile)
 * ========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_JdbcUrlAndDriverManager.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * rollback() (Exercise06) annule TOUT depuis le debut de la
 * transaction - parfois trop radical. Un Savepoint est un "signet"
 * pose A UN MOMENT PRECIS de la transaction : rollback(savepoint)
 * n'annule QUE ce qui s'est passe APRES ce signet, en GARDANT tout ce
 * qui etait deja fait AVANT - et surtout, la transaction elle-meme
 * N'EST PAS terminee, on peut continuer a travailler et a commit()
 * normalement ensuite.
 *
 *
 * ==================================================================
 * TODO : processWithSavepoint(conn)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Ecrire "A" dans le journal. Poser un signet. Ecrire "B". Se
 * raviser : annuler JUSTE AU SIGNET (donc annuler "B" seulement, "A"
 * reste). Ecrire "C" (la transaction continue normalement APRES le
 * rollback partiel). Valider TOUT (commit()). Resultat final dans le
 * journal : "A" et "C" - JAMAIS "B".
 *
 * -- Le plan --
 *
 *   1. conn.setAutoCommit(false).
 *   2. Dans un try : ecrire "A" (insertLog(), deja fournie plus bas).
 *   3. Poser un signet : Savepoint sp = conn.setSavepoint().
 *   4. Ecrire "B".
 *   5. conn.rollback(sp) (annule UNIQUEMENT "B", pas "A", et NE
 *      TERMINE PAS la transaction).
 *   6. Ecrire "C".
 *   7. conn.commit().
 *   8. Dans un finally : conn.setAutoCommit(true).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : insertLog() est deja fournie comme petite boite reutilisable,
 * processWithSavepoint() l'utilise 3 fois.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Connection.rollback() (SANS argument, Exercise06) et
 *     Connection.rollback(Savepoint) (Exercise07) sont 2 methodes
 *     SURCHARGEES - la version avec Savepoint est UNIQUEMENT partielle.
 */
public class Exercise07_Savepoints {

    private static void insertLog(Connection conn, String label) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO logs (label) VALUES (?)")) {
            ps.setString(1, label);
            ps.executeUpdate();
        }
    }

    public static void processWithSavepoint(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO : implementer processWithSavepoint()");
    }

    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:exercice07", "sa", "")) {
            try (Statement setup = connection.createStatement()) {
                setup.executeUpdate("CREATE TABLE logs (label VARCHAR(10))");
            }

            processWithSavepoint(connection);

            List<String> labels = new ArrayList<>();
            try (Statement st = connection.createStatement();
                 ResultSet rs = st.executeQuery("SELECT label FROM logs ORDER BY label")) {
                while (rs.next()) {
                    labels.add(rs.getString("label"));
                }
            }
            ExerciseChecker.check("le journal contient A et C, mais PAS B (annule par le savepoint)",
                    labels.equals(List.of("A", "C")));
        }

        ExerciseChecker.summary();
    }
}
