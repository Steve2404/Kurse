package ch15_jdbc.projects.p04_import.solution;

import ch15_jdbc.projects.p04_import.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SOLUTION du projet 4 - l'import de fichiers clients : lots (batch), cles generees, BatchUpdateException.
 */
public class ImportApp {

    static List<String> rows(Connection conn, String sql) throws SQLException {
        List<String> rows = new ArrayList<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                rows.add(rs.getString(1) + "=" + rs.getString(2));
            }
        }
        return rows;
    }

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection(Data.URL)) {
            try (Statement st = conn.createStatement()) {
                for (String ddl : Data.SCHEMA) {
                    st.executeUpdate(ddl);
                }
            }
            Importer importer = new Importer(conn, Data.CHUNK);
            String report = importer.importFile("clients.csv", Data.CLIENTS, false);
            System.out.println("nettoyage : " + Data.CLIENTS.length + " lignes, invalides " + importer.problems() + ", doublons " + importer.duplicates());
            System.out.println(report);
            System.out.println(importer.importFile("delta.csv", Data.DELTA, true));
            System.out.println("  clients apres l'import strict : " + rows(conn, "SELECT 'clients', COUNT(*) FROM customers"));
            System.out.println(importer.importFile("delta.csv", Data.DELTA, false));

            // Un lot de Statement peut melanger des ordres differents (mais jamais un SELECT).
            try (Statement st = conn.createStatement()) {
                st.addBatch("UPDATE customers SET city = UPPER(city)");
                st.addBatch("DELETE FROM customers WHERE city = 'NICE'");
                st.addBatch("INSERT INTO import_log (file, inserted, rejected) VALUES ('nettoyage', 0, 0)");
                System.out.println("lot mixte : " + Arrays.toString(st.executeBatch()));
                st.addBatch("DELETE FROM customers");
                st.clearBatch();
                System.out.println("lot vide apres clearBatch : " + st.executeBatch().length + " ordre");
            }
            conn.commit();

            // Demander une cle par le NOM de sa colonne, au lieu de RETURN_GENERATED_KEYS.
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO customers (email, name, city) VALUES (?, ?, ?)", new String[]{"ID"})) {
                ps.setString(1, "zoe@mail.fr");
                ps.setString(2, "Zoe");
                ps.setString(3, "LYON");
                ps.executeUpdate();
                try (ResultSet key = ps.getGeneratedKeys()) {
                    key.next();
                    System.out.println("zoe : cle " + key.getInt(1));
                }
            }
            conn.commit();
            System.out.println("par ville : " + rows(conn, "SELECT city, COUNT(*) AS n FROM customers GROUP BY city ORDER BY n DESC, city"));
            System.out.println("journal : " + rows(conn, "SELECT file, inserted || '/' || rejected FROM import_log ORDER BY id"));
        }
    }
}
