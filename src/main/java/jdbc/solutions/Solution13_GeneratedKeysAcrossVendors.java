package jdbc.solutions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Corrige de l'exercice 13. A ne consulter qu'apres avoir essaye par
 * vous-meme dans jdbc.exercises.Exercise13_GeneratedKeysAcrossVendors.
 */
public class Solution13_GeneratedKeysAcrossVendors {

    public static long insertAndReturnGeneratedId(Connection conn, String insertSql, String name)
            throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }
}
