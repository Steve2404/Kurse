package ch15_jdbc.solutions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch15_jdbc.exercises.Exercise09_Savepoints.
 */
public class Solution09_Savepoints {

    private static void insertLog(Connection conn, String label) throws SQLException {
        // Boite magique : une ligne de log, avec un ? pour la valeur.
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO logs (label) VALUES (?)")) {
            ps.setString(1, label);
            ps.executeUpdate();
        }
    }

    public static void processWithSavepoint(Connection conn) throws SQLException {
        // rollback(savepoint) n'annule que ce qui suit le savepoint (B) ; A et C sont valides par le commit.
        conn.setAutoCommit(false);
        try {
            insertLog(conn, "A");
            Savepoint savepoint = conn.setSavepoint();
            insertLog(conn, "B");
            conn.rollback(savepoint);
            insertLog(conn, "C");
            conn.commit();
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
