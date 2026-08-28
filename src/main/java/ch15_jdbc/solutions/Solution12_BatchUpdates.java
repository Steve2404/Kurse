package ch15_jdbc.solutions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans jdbc.exercises.Exercise12_BatchUpdates.
 */
public class Solution12_BatchUpdates {

    public static int[] insertNamesInBatch(Connection conn, List<String> names) throws SQLException {
        try (Statement ddl = conn.createStatement()) {
            ddl.execute("DROP TABLE IF EXISTS batch_demo");
            ddl.execute("CREATE TABLE batch_demo (id INT PRIMARY KEY, name VARCHAR(50))");
        }

        try (PreparedStatement insert = conn.prepareStatement("INSERT INTO batch_demo (id, name) VALUES (?, ?)")) {
            for (int i = 0; i < names.size(); i++) {
                insert.setInt(1, i + 1);
                insert.setString(2, names.get(i));
                insert.addBatch();
            }
            return insert.executeBatch();
        }
    }
}
