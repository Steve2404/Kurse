package ch15_jdbc.solutions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 18. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch15_jdbc.exercises.Exercise18_SameCrudAcrossVendors.
 */
public class Solution18_SameCrudAcrossVendors {

    public static List<String> runCrudCycle(Connection conn) throws SQLException {
        // Que du SQL standard et des PreparedStatement : le meme code tourne sur H2, Postgres et MySQL. Un PreparedStatement se reutilise.
        try (Statement ddl = conn.createStatement()) {
            ddl.execute("DROP TABLE IF EXISTS crud_demo");
            ddl.execute("CREATE TABLE crud_demo (id INT PRIMARY KEY, name VARCHAR(50))");
        }

        try (PreparedStatement insert = conn.prepareStatement("INSERT INTO crud_demo (id, name) VALUES (?, ?)")) {
            insert.setInt(1, 1);
            insert.setString(2, "Steve");
            insert.executeUpdate();

            insert.setInt(1, 2);
            insert.setString(2, "Alice");
            insert.executeUpdate();
        }

        List<String> result = new ArrayList<>();
        try (PreparedStatement select = conn.prepareStatement("SELECT id, name FROM crud_demo ORDER BY id");
                ResultSet rs = select.executeQuery()) {
            while (rs.next()) {
                result.add(rs.getInt("id") + ":" + rs.getString("name"));
            }
        }
        return result;
    }
}