package ch15_jdbc.solutions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Corrige de l'exercice 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans jdbc.exercises.Exercise03_PreparedStatementUpdateDelete.
 */
public class Solution03_PreparedStatementUpdateDelete {

    public static int updateScore(Connection conn, int id, int newScore) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE players SET score = ? WHERE id = ?")) {
            ps.setInt(1, newScore);
            ps.setInt(2, id);
            return ps.executeUpdate();
        }
    }

    public static int deletePlayer(Connection conn, int id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM players WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }
}
