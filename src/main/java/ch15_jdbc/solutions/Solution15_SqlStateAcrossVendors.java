package ch15_jdbc.solutions;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Corrige de l'exercice 15. A ne consulter qu'apres avoir essaye par
 * vous-meme dans jdbc.exercises.Exercise15_SqlStateAcrossVendors.
 */
public class Solution15_SqlStateAcrossVendors {

    public static String sqlStateFamilyOfDuplicateKey(Connection conn) throws SQLException {
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
