package jdbc.solutions;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans jdbc.exercises.Exercise09_JdbcUrlAcrossVendors.
 */
public class Solution09_JdbcUrlAcrossVendors {

    public static String buildPostgresUrl(String host, int port, String database) {
        return "jdbc:postgresql://" + host + ":" + port + "/" + database;
    }

    public static String buildMysqlUrl(String host, int port, String database) {
        return "jdbc:mysql://" + host + ":" + port + "/" + database;
    }

    public static boolean hasRegisteredDriver(String url) {
        try {
            DriverManager.getDriver(url);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
