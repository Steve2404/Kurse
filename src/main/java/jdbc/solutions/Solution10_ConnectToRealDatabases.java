package jdbc.solutions;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans jdbc.exercises.Exercise10_ConnectToRealDatabases.
 */
public class Solution10_ConnectToRealDatabases {

    public static String describeDatabase(String url, String user, String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            DatabaseMetaData meta = conn.getMetaData();
            return meta.getDatabaseProductName() + " " + meta.getDatabaseProductVersion();
        }
    }
}
