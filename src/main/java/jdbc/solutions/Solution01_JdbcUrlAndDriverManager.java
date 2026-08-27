package jdbc.solutions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans jdbc.exercises.Exercise01_JdbcUrlAndDriverManager.
 */
public class Solution01_JdbcUrlAndDriverManager {

    public static String[] parseJdbcUrl(String url) {
        return url.split(":", 3);
    }

    public static Connection connectToH2InMemory(String databaseName) throws SQLException {
        return DriverManager.getConnection("jdbc:h2:mem:" + databaseName, "sa", "");
    }
}
