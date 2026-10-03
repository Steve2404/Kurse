package ch15_jdbc.drills.r06_kata.solution;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * SOLUTION du drill de rappel 6 (test final) - tout le chapitre, dans un carnet de notes.
 */
public class Recall06 {

    // Une ressource qui note sa fermeture : pour voir l'ordre du try-with-resources.
    record Tracked(String name, List<String> log) implements AutoCloseable {
        @Override
        public void close() {
            log.add(name);
        }
    }

    public static int words(String text) {
        return text.isBlank() ? 0 : text.trim().split("\\s+").length;
    }

    static int count(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM notes")) {
            rs.next();
            return rs.getInt(1);
        }
    }

    public static void main(String[] args) throws SQLException {
        // D01 : on ferme dans l'ordre INVERSE de l'ouverture : le ResultSet, puis le Statement, puis la Connection.
        List<String> log = new ArrayList<>();
        try (Tracked c = new Tracked("connexion", log); Tracked s = new Tracked("statement", log); Tracked r = new Tracked("resultset", log)) {
            log.add("ouvert " + c.name() + ">" + s.name() + ">" + r.name());
        }
        System.out.println("D01 : " + log);

        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:r06")) {
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("CREATE TABLE notes (id INT AUTO_INCREMENT PRIMARY KEY, topic VARCHAR(10) NOT NULL, body VARCHAR(60) NOT NULL, stars INT)");
                st.execute("CREATE ALIAS WORDS FOR \"" + Recall06.class.getName() + ".words\"");
            }

            // D02 : un lot dans une transaction, puis les cles.
            conn.setAutoCommit(false);
            String[][] notes = {{"java", "les streams sont paresseux", "5"}, {"sql", "un index accelere la lecture", ""},
                    {"java", "var est local", "3"}, {"sql", "commit valide", "4"}, {"java", "record est final", "4"}};
            List<Integer> keys = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO notes (topic, body, stars) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                for (String[] n : notes) {
                    ps.setString(1, n[0]);
                    ps.setString(2, n[1]);
                    if (n[2].isEmpty()) {
                        ps.setNull(3, Types.INTEGER);
                    } else {
                        ps.setInt(3, Integer.parseInt(n[2]));
                    }
                    ps.addBatch();
                }
                ps.executeBatch();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    while (rs.next()) {
                        keys.add(rs.getInt(1));
                    }
                }
            }
            conn.commit();
            System.out.println("D02 : " + keys);

            // D03 : la page 2 (taille 2), triee par id.
            List<String> page = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM notes ORDER BY id LIMIT ? OFFSET ?")) {
                ps.setInt(1, 2);
                ps.setInt(2, 2);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        page.add(rs.getString("id"));
                    }
                }
            }
            System.out.println("D03 : " + page);

            // D04 : un regroupement ; COUNT(colonne) et SUM ignorent les NULL, COUNT(*) compte les lignes.
            List<String> groups = new ArrayList<>();
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT topic, COUNT(*), COUNT(stars), SUM(stars) FROM notes GROUP BY topic ORDER BY topic")) {
                while (rs.next()) {
                    groups.add(rs.getString(1) + " " + rs.getInt(2) + "/" + rs.getInt(3) + "/" + rs.getObject(4));
                }
            }
            System.out.println("D04 : " + groups);

            // D05 : la fonction stockee, en OUT puis dans le SQL.
            try (CallableStatement cs = conn.prepareCall("{? = call WORDS(?)}");
                 Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT SUM(WORDS(body)) FROM notes")) {
                cs.registerOutParameter(1, Types.INTEGER);
                cs.setString(2, "  un  deux trois ");
                cs.execute();
                rs.next();
                System.out.println("D05 : " + cs.getInt(1) + " " + rs.getInt(1));
            }

            // D06 : un Savepoint sauve une partie du travail.
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("DELETE FROM notes WHERE topic = 'sql'");
                Savepoint sp = conn.setSavepoint("sql-supprime");
                int java = st.executeUpdate("DELETE FROM notes WHERE topic = 'java'");
                conn.rollback(sp);
                conn.commit();
                System.out.println("D06 : " + java + " supprimees puis restaurees, il reste " + count(conn));
            }

            // D07 : les metadonnees du resultat et de la base.
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM notes")) {
                System.out.println("D07 : " + rs.getMetaData().getColumnCount() + " colonnes, " + rs.getMetaData().getColumnName(4)
                        + ", savepoints " + conn.getMetaData().supportsSavepoints());
            }

            // D08 : une colonne inconnue, puis wasNull sur une ligne connue.
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT stars FROM notes WHERE id = 1")) {
                rs.next();
                try {
                    rs.getInt("rating");
                } catch (SQLException e) {
                    int stars = rs.getInt("stars");
                    System.out.println("D08 : " + e.getSQLState() + ", stars " + stars + " wasNull " + rs.wasNull());
                }
            }
        }
    }
}
