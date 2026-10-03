package ch15_jdbc.projects.p06_report.solution;

import ch15_jdbc.projects.p06_report.Data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * SOLUTION du projet 6 - un generateur de rapports et un outil de copie de base : DatabaseMetaData et ResultSetMetaData.
 */
public class ReportApp {

    static List<String> query(Connection conn, String sql) throws SQLException {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return TablePrinter.render(rs);
        }
    }

    static int count(Connection conn, String table) throws SQLException {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + table)) {
            rs.next();
            return rs.getInt(1);
        }
    }

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection(Data.URL, Data.USER, Data.PASSWORD);
             Connection copy = DriverManager.getConnection(Data.COPY_URL)) {
            try (Statement st = conn.createStatement()) {
                for (String sql : Data.SCHEMA) {
                    st.executeUpdate(sql);
                }
                for (String sql : Data.INSERTS) {
                    st.executeUpdate(sql);
                }
            }
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("base " + meta.getDatabaseProductName() + ", pilote " + meta.getDriverName() + ", utilisateur " + meta.getUserName()
                    + ", lots " + meta.supportsBatchUpdates() + ", points de sauvegarde " + meta.supportsSavepoints());

            for (String report : Data.REPORTS) {
                query(conn, report).forEach(System.out::println);
            }

            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT title AS titre, price * 2 AS double_prix FROM books")) {
                ResultSetMetaData rsm = rs.getMetaData();
                for (int i = 1; i <= rsm.getColumnCount(); i++) {
                    System.out.println("colonne " + i + " : label " + rsm.getColumnLabel(i) + ", nom " + rsm.getColumnName(i) + ", type " + rsm.getColumnTypeName(i)
                            + ", classe Java " + rsm.getColumnClassName(i));
                }
            }

            Schema schema = new Schema(conn);
            for (String table : schema.tables()) {
                System.out.println(table + " : cle " + schema.primaryKey(table) + ", " + schema.columns(table) + ", references " + schema.foreignKeys(table));
            }
            List<String> order = schema.creationOrder();
            List<String> reverse = new ArrayList<>(order);
            Collections.reverse(reverse);
            System.out.println("ordre de creation : " + order);
            System.out.println("ordre de suppression : " + reverse);

            List<String> dump = schema.dump();
            System.out.println("dump : " + dump.size() + " ordres");
            for (String line : dump) {
                if (line.startsWith("CREATE") || line.contains("NULL") || line.contains("''")) {
                    System.out.println("  " + line);
                }
            }

            // On recharge le dump dans une base VIDE, puis on compare les deux, table par table.
            try (Statement st = copy.createStatement()) {
                for (String line : dump) {
                    st.executeUpdate(line);
                }
            }
            int same = 0;
            for (String table : order) {
                if (query(conn, "SELECT * FROM " + table + " ORDER BY 1").equals(query(copy, "SELECT * FROM " + table + " ORDER BY 1"))) {
                    same++;
                }
            }
            System.out.println("copie : " + same + "/" + order.size() + " tables identiques");

            try (Statement st = copy.createStatement()) {
                try {
                    st.executeUpdate("DELETE FROM " + order.get(0));
                } catch (SQLException e) {
                    System.out.println("vider " + order.get(0) + " en premier : " + e.getSQLState());
                }
                int deleted = 0;
                for (String table : reverse) {
                    deleted += st.executeUpdate("DELETE FROM " + table);
                }
                int left = 0;
                for (String table : order) {
                    left += count(copy, table);
                }
                System.out.println("vider dans l'ordre de suppression : " + deleted + " lignes supprimees, il en reste " + left);
            }
        }
    }
}
