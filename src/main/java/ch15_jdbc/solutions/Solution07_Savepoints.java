package ch15_jdbc.solutions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans jdbc.exercises.Exercise07_Savepoints.
 */
public class Solution07_Savepoints {

    private static void insertLog(Connection conn, String label) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO logs (label) VALUES (?)")) {
            ps.setString(1, label);
            ps.executeUpdate();
        }
    }

    public static void processWithSavepoint(Connection conn) throws SQLException {
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
