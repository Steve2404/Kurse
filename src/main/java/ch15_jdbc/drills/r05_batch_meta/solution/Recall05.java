package ch15_jdbc.drills.r05_batch_meta.solution;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SOLUTION du drill de rappel 5 - lots, cles generees, metadonnees, sous-classes de SQLException.
 */
public class Recall05 {

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:r05");
             Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE tasks (id INT AUTO_INCREMENT PRIMARY KEY, label VARCHAR(20) NOT NULL UNIQUE, done BOOLEAN NOT NULL)");

            // D01 : un lot de PreparedStatement, et les cles creees.
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO tasks (label, done) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                for (String label : List.of("laver", "ranger", "cuisiner")) {
                    ps.setString(1, label);
                    ps.setBoolean(2, false);
                    ps.addBatch();
                }
                int[] counts = ps.executeBatch();
                List<Integer> keys = new ArrayList<>();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    while (rs.next()) {
                        keys.add(rs.getInt(1));
                    }
                }
                System.out.println("D01 : " + Arrays.toString(counts) + " cles " + keys);
            }

            // D02 : un lot de Statement melange des ordres differents.
            st.addBatch("UPDATE tasks SET done = TRUE WHERE id <= 2");
            st.addBatch("DELETE FROM tasks WHERE label = 'absent'");
            st.addBatch("INSERT INTO tasks (label, done) VALUES ('dormir', FALSE)");
            System.out.println("D02 : " + Arrays.toString(st.executeBatch()));

            // D03 : un lot qui echoue en partie ; EXECUTE_FAILED marque les lignes refusees.
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO tasks (label, done) VALUES (?, FALSE)")) {
                for (String label : List.of("lire", "laver", "courir")) {
                    ps.setString(1, label);
                    ps.addBatch();
                }
                ps.executeBatch();
            } catch (BatchUpdateException e) {
                int[] counts = e.getUpdateCounts();
                System.out.println("D03 : " + Arrays.toString(counts) + ", refusee en position " + Arrays.stream(counts).boxed().toList().indexOf(Statement.EXECUTE_FAILED));
            }

            // D04 : clearBatch vide la pile sans rien envoyer.
            st.addBatch("DELETE FROM tasks");
            st.clearBatch();
            System.out.println("D04 : " + st.executeBatch().length + " ordre envoye");

            // D05 : une cle demandee par le NOM de sa colonne.
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO tasks (label, done) VALUES ('nager', TRUE)", new String[]{"ID"})) {
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    System.out.println("D05 : cle " + rs.getInt(1));
                }
            }

            // D06 : ResultSetMetaData d'une requete.
            try (ResultSet rs = st.executeQuery("SELECT label AS tache, done, id * 10 AS rang FROM tasks")) {
                ResultSetMetaData meta = rs.getMetaData();
                List<String> cols = new ArrayList<>();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    cols.add(meta.getColumnLabel(i) + "/" + meta.getColumnName(i) + "/" + meta.getColumnTypeName(i));
                }
                System.out.println("D06 : " + meta.getColumnCount() + " " + cols);
            }

            // D07 : DatabaseMetaData de la base.
            DatabaseMetaData db = conn.getMetaData();
            List<String> columns = new ArrayList<>();
            try (ResultSet rs = db.getColumns(null, "PUBLIC", "TASKS", "%")) {
                while (rs.next()) {
                    columns.add(rs.getString("COLUMN_NAME"));
                }
            }
            int tables = 0;
            try (ResultSet rs = db.getTables(null, "PUBLIC", "%", new String[]{"TABLE"})) {
                while (rs.next()) {
                    tables++;
                }
            }
            System.out.println("D07 : " + db.getDatabaseProductName() + " " + tables + " table " + columns);

            // D08 : les sous-classes standard de SQLException, et le code du fournisseur.
            try {
                st.executeUpdate("INSERT INTO tasks (label, done) VALUES ('nager', TRUE)");
            } catch (SQLException e) {
                System.out.println("D08 : " + (e instanceof SQLIntegrityConstraintViolationException) + " " + e.getSQLState() + " " + e.getErrorCode());
            }
            try {
                st.executeQuery("SELEC 1");
            } catch (SQLException e) {
                System.out.println("D08 : " + (e instanceof SQLSyntaxErrorException) + " " + e.getSQLState() + " " + (e.getNextException() == null));
            }
        }
    }
}
