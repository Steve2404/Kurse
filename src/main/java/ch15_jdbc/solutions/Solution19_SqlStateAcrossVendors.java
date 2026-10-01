package ch15_jdbc.solutions;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Corrige de l'exercice 19. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch15_jdbc.exercises.Exercise19_SqlStateAcrossVendors.
 */
public class Solution19_SqlStateAcrossVendors {

    public static String sqlStateFamilyOfDuplicateKey(Connection conn) throws SQLException {
        // Le code complet varie (23505 contre 23000) mais la classe "23" (contrainte violee) est commune a tous.
        try (Statement ddl = conn.createStatement()) {
            ddl.execute("DROP TABLE IF EXISTS exc_demo");
            ddl.execute("CREATE TABLE exc_demo (id INT PRIMARY KEY)");
            ddl.execute("INSERT INTO exc_demo (id) VALUES (1)");
        }

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT INTO exc_demo (id) VALUES (1)");
            throw new AssertionError("Le doublon de cle primaire aurait du lancer une SQLException");
        } catch (SQLException e) {
            return e.getSQLState().substring(0, 2);
        }
    }
}
