package ch15_jdbc.drills.solutions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Corrige du drill 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch15_jdbc.drills.exercises.Drill04_MixedKata.
 */
public class SolutionDrill04_MixedKata {

    public static Map<String, Integer> booksPerAuthor(Connection conn) throws SQLException {
        // GROUP BY fait le groupingBy ; TreeMap trie les cles.
        Map<String, Integer> counts = new TreeMap<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT author, COUNT(*) FROM books GROUP BY author");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                counts.put(rs.getString(1), rs.getInt(2));
            }
        }
        return counts;
    }

    public static List<String> titlesBorrowedBy(Connection conn, String memberId) throws SQLException {
        // Une jointure relie l'emprunt au titre du livre.
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT b.title FROM loans l JOIN books b ON b.isbn = l.isbn WHERE l.member_id = ? ORDER BY b.title")) {
            ps.setString(1, memberId);
            return strings(ps);
        }
    }

    public static int totalLateDays(Connection conn) throws SQLException {
        // SUM est calcule par la base ; une seule ligne, une seule colonne.
        try (PreparedStatement ps = conn.prepareStatement("SELECT SUM(days_late) FROM loans"); ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    public static String mostBorrowed(Connection conn) throws SQLException {
        // Trier les groupes par nombre decroissant (puis titre pour departager) et garder le premier.
        try (PreparedStatement ps = conn.prepareStatement("SELECT b.title FROM loans l JOIN books b ON b.isbn = l.isbn "
                + "GROUP BY b.title ORDER BY COUNT(*) DESC, b.title FETCH FIRST 1 ROWS ONLY");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getString(1) : null;
        }
    }

    public static List<String> withoutLoans(Connection conn) throws SQLException {
        // NOT EXISTS : les membres pour qui aucun emprunt ne correspond.
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT name FROM members m WHERE NOT EXISTS (SELECT 1 FROM loans l WHERE l.member_id = m.id) ORDER BY name")) {
            return strings(ps);
        }
    }

    public static double averagePrice(Connection conn, String genre) throws SQLException {
        // AVG sur une colonne DECIMAL ; getDouble pour l'avoir en double.
        try (PreparedStatement ps = conn.prepareStatement("SELECT AVG(price) FROM books WHERE genre = ?")) {
            ps.setString(1, genre);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getDouble(1);
            }
        }
    }

    public static List<String> monthReport(Connection conn, int month) throws SQLException {
        // Double jointure et concatenation faite en Java a partir de deux colonnes.
        List<String> lines = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT m.name, b.title FROM loans l JOIN members m ON m.id = l.member_id "
                + "JOIN books b ON b.isbn = l.isbn WHERE l.loan_month = ? ORDER BY m.name, b.title")) {
            ps.setInt(1, month);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lines.add(rs.getString(1) + ": " + rs.getString(2));
                }
            }
        }
        return lines;
    }

    public static String tryAddBook(Connection conn, String isbn) {
        // La classe du SQLState (2 caracteres) est la seule partie commune a tous les fournisseurs : "23" = contrainte violee.
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO books (isbn, title) VALUES (?, ?)")) {
            ps.setString(1, isbn);
            ps.setString(2, "Nouveau");
            ps.executeUpdate();
            return "OK";
        } catch (SQLException e) {
            return e.getSQLState().substring(0, 2);
        }
    }

    private static List<String> strings(PreparedStatement ps) throws SQLException {
        // Boite magique : la 1re colonne de chaque ligne.
        List<String> values = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                values.add(rs.getString(1));
            }
        }
        return values;
    }
}
