package ch15_jdbc.solutions;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Corrige de l'exercice 17. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch15_jdbc.exercises.Exercise17_ConnectToRealDatabases.
 */
public class Solution17_ConnectToRealDatabases {

    public static String describeDatabase(String url, String user, String password) throws SQLException {
        // DatabaseMetaData decrit la base elle-meme (nom, version) ; la connexion est fermee par le try.
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            DatabaseMetaData meta = conn.getMetaData();
            return meta.getDatabaseProductName() + " " + meta.getDatabaseProductVersion();
        }
    }
}
