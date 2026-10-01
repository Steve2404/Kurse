package ch15_jdbc.solutions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch15_jdbc.exercises.Exercise01_JdbcUrlAndDriverManager.
 */
public class Solution01_JdbcUrlAndDriverManager {

    public static String[] parseJdbcUrl(String url) {
        // Limite 3 : "jdbc", le fournisseur, puis tout le reste (qui peut contenir d'autres ":").
        return url.split(":", 3);
    }

    public static Connection connectToH2InMemory(String databaseName) throws SQLException {
        // DriverManager trouve tout seul le pilote (JDBC 4+ : plus besoin de Class.forName) grace au prefixe de l'URL.
        return DriverManager.getConnection("jdbc:h2:mem:" + databaseName, "sa", "");
    }
}
