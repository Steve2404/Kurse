package jdbc.solutions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 14. A ne consulter qu'apres avoir essaye par
 * vous-meme dans jdbc.exercises.Exercise14_ResultSetMetaDataAcrossVendors.
 */
public class Solution14_ResultSetMetaDataAcrossVendors {

    public static List<String> describeColumns(Connection conn) throws SQLException {
        try (Statement ddl = conn.createStatement()) {
            ddl.execute("DROP TABLE IF EXISTS meta_demo");
            ddl.execute("CREATE TABLE meta_demo (id INT PRIMARY KEY, name VARCHAR(50))");
        }

        List<String> columns = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM meta_demo")) {
            ResultSetMetaData meta = rs.getMetaData();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                columns.add(meta.getColumnName(i).toUpperCase());
            }
        }
        return columns;
    }
}
