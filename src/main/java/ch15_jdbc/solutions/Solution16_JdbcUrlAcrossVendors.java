package ch15_jdbc.solutions;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Corrige de l'exercice 16. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch15_jdbc.exercises.Exercise16_JdbcUrlAcrossVendors.
 */
public class Solution16_JdbcUrlAcrossVendors {

    public static String buildPostgresUrl(String host, int port, String database) {
        // Format jdbc:postgresql://hote:port/base.
        return "jdbc:postgresql://" + host + ":" + port + "/" + database;
    }

    public static String buildMysqlUrl(String host, int port, String database) {
        // Meme forme que Postgres, seul le nom du fournisseur change.
        return "jdbc:mysql://" + host + ":" + port + "/" + database;
    }

    public static boolean hasRegisteredDriver(String url) {
        // getDriver lance SQLException ("No suitable driver") si aucun pilote ne reconnait l'URL.
        try {
            DriverManager.getDriver(url);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
