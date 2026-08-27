package jdbc.solutions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans jdbc.exercises.Exercise02_PreparedStatementInsertSelect.
 */
public class Solution02_PreparedStatementInsertSelect {

    public static void createPlayersTable(Connection conn) throws SQLException {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("CREATE TABLE players (id INT PRIMARY KEY, name VARCHAR(50), score INT)");
        }
    }

    public static int insertPlayer(Connection conn, int id, String name, int score) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO players (id, name, score) VALUES (?, ?, ?)")) {
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setInt(3, score);
            return ps.executeUpdate();
        }
    }

    public static List<String> selectAllPlayers(Connection conn) throws SQLException {
        List<String> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT id, name, score FROM players ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(rs.getInt("id") + ":" + rs.getString("name") + ":" + rs.getInt("score"));
            }
        }
        return result;
    }
}
