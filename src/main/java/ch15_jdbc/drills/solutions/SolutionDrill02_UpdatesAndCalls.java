package ch15_jdbc.drills.solutions;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/**
 * Corrige du drill 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch15_jdbc.drills.exercises.Drill02_UpdatesAndCalls.
 */
public class SolutionDrill02_UpdatesAndCalls {

    public static int addMember(Connection conn) throws SQLException {
        // Un INSERT rend le nombre de lignes inserees ; setNull pour une colonne sans valeur.
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO members VALUES (?, ?, ?, ?)")) {
            ps.setString(1, "M5");
            ps.setString(2, "Zoe");
            ps.setInt(3, 29);
            ps.setNull(4, Types.VARCHAR);
            return ps.executeUpdate();
        }
    }

    public static int raiseSfPrices(Connection conn) throws SQLException {
        // Un UPDATE peut toucher plusieurs lignes d'un coup.
        try (PreparedStatement ps = conn.prepareStatement("UPDATE books SET price = price + 1 WHERE genre = ?")) {
            ps.setString(1, "SF");
            return ps.executeUpdate();
        }
    }

    public static int forgetLea(Connection conn) throws SQLException {
        // DELETE rend aussi un compte de lignes.
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM loans WHERE member_id = ?")) {
            ps.setString(1, "M1");
            return ps.executeUpdate();
        }
    }

    public static int createNotes(Connection conn) throws SQLException {
        // Un ordre de structure (CREATE) ne touche aucune ligne : executeUpdate rend 0.
        try (Statement st = conn.createStatement()) {
            return st.executeUpdate("CREATE TABLE notes (id INT AUTO_INCREMENT PRIMARY KEY, txt VARCHAR(50))");
        }
    }

    public static boolean clearEmail(Connection conn) throws SQLException {
        // setNull a besoin du type SQL de la colonne ; on relit pour verifier.
        try (PreparedStatement ps = conn.prepareStatement("UPDATE members SET email = ? WHERE id = ?")) {
            ps.setNull(1, Types.VARCHAR);
            ps.setString(2, "M1");
            ps.executeUpdate();
        }
        try (PreparedStatement ps = conn.prepareStatement("SELECT email FROM members WHERE id = ?")) {
            ps.setString(1, "M1");
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getString(1) == null;
            }
        }
    }

    public static int twoLoans(Connection conn) throws SQLException {
        // Le SQL est prepare une fois ; on change seulement les valeurs avant chaque execution.
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO loans VALUES (?, ?, ?, ?)")) {
            ps.setString(1, "M4");
            ps.setString(2, "B3");
            ps.setInt(3, 4);
            ps.setInt(4, 0);
            int total = ps.executeUpdate();
            ps.setString(2, "B7");
            total += ps.executeUpdate();
            return total;
        }
    }

    public static int batchMembers(Connection conn) throws SQLException {
        // Les 3 jeux de valeurs partent ensemble ; le tableau rendu a une case par ordre.
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO members (id, name) VALUES (?, ?)")) {
            String[][] members = {{"M6", "Ana"}, {"M7", "Bob"}, {"M8", "Cid"}};
            for (String[] m : members) {
                ps.setString(1, m[0]);
                ps.setString(2, m[1]);
                ps.addBatch();
            }
            return ps.executeBatch().length;
        }
    }

    public static int insertNote(Connection conn) throws SQLException {
        // Il faut DEMANDER les cles a la preparation, puis les lire dans un ResultSet a part.
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO notes (txt) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "rappel");
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    public static int lateFee(Connection conn, int days) throws SQLException {
        // Le 1er ? est le resultat : il doit etre enregistre comme parametre de sortie avant execute.
        try (CallableStatement cs = conn.prepareCall("{? = call LATE_FEE(?)}")) {
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, days);
            cs.execute();
            return cs.getInt(1);
        }
    }

    public static int updateNobody(Connection conn) throws SQLException {
        // Aucune ligne ne correspond : 0, sans exception.
        try (Statement st = conn.createStatement()) {
            return st.executeUpdate("UPDATE books SET price = 0 WHERE isbn = 'B99'");
        }
    }
}
