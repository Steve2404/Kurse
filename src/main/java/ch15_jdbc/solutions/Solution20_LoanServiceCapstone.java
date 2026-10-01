package ch15_jdbc.solutions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 20. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch15_jdbc.exercises.Exercise20_LoanServiceCapstone.
 */
public class Solution20_LoanServiceCapstone {

    public static class LibraryException extends Exception {
        private static final long serialVersionUID = 1L;

        public LibraryException(String message) {
            super(message);
        }

        public LibraryException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static String family(SQLException e) {
        // Seuls les 2 premiers caracteres du SQLState sont communs a tous les fournisseurs (la "classe").
        String state = e.getSQLState();
        if (state == null) {
            return "OTHER";
        }
        if (state.startsWith("23")) {
            return "CONSTRAINT";
        }
        return state.startsWith("42") ? "SYNTAX" : "OTHER";
    }

    public static int borrow(Connection conn, String memberId, String isbn) throws LibraryException, SQLException {
        // Deux ordres lies : une transaction. Le "AND stock > 0" rend la verification et la diminution indivisibles.
        boolean before = conn.getAutoCommit();
        conn.setAutoCommit(false);
        try {
            try (PreparedStatement take = conn.prepareStatement("UPDATE books SET stock = stock - 1 WHERE isbn = ? AND stock > 0")) {
                take.setString(1, isbn);
                if (take.executeUpdate() == 0) {
                    conn.rollback();
                    throw new LibraryException("plus d'exemplaire : " + isbn);
                }
            }
            try (PreparedStatement loan = conn.prepareStatement("INSERT INTO loans (member_id, isbn) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                loan.setString(1, memberId);
                loan.setString(2, isbn);
                loan.executeUpdate();
                try (ResultSet keys = loan.getGeneratedKeys()) {
                    keys.next();
                    int id = keys.getInt(1);
                    conn.commit();
                    return id;
                }
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new LibraryException("emprunt refuse : " + family(e), e);
        } finally {
            conn.setAutoCommit(before);
        }
    }

    public static int registerAll(Connection conn, List<String[]> members) throws LibraryException, SQLException {
        // Un seul aller-retour (batch) et une seule transaction : un doublon fait tout annuler.
        boolean before = conn.getAutoCommit();
        conn.setAutoCommit(false);
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO members VALUES (?, ?)")) {
            for (String[] m : members) {
                ps.setString(1, m[0]);
                ps.setString(2, m[1]);
                ps.addBatch();
            }
            int total = 0;
            for (int n : ps.executeBatch()) {
                total += n;
            }
            conn.commit();
            return total;
        } catch (SQLException e) {
            conn.rollback();
            throw new LibraryException("inscription refusee : " + family(e), e);
        } finally {
            conn.setAutoCommit(before);
        }
    }

    public static List<String> loansReport(Connection conn) throws SQLException {
        // Une jointure SQL fait le travail des deux recherches ; le tri est demande a la base.
        List<String> lines = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT m.name, b.title FROM loans l "
                + "JOIN members m ON m.id = l.member_id JOIN books b ON b.isbn = l.isbn ORDER BY m.name, b.title");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lines.add(rs.getString(1) + ": " + rs.getString(2));
            }
        }
        return lines;
    }
}
