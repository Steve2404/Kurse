package ch15_jdbc.drills.solutions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Corrige du drill 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch15_jdbc.drills.exercises.Drill01_ConnectAndQuery.
 */
public class SolutionDrill01_ConnectAndQuery {

    public static Connection connect(String name) throws SQLException {
        // L'URL choisit le pilote : "jdbc:h2:" -> H2 ; "mem:" -> en memoire.
        return DriverManager.getConnection("jdbc:h2:mem:" + name);
    }

    public static int bookCount(Connection conn) throws SQLException {
        // Pas de parametre : un Statement suffit ; COUNT(*) est dans la 1re colonne.
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM books")) {
            rs.next();
            return rs.getInt(1);
        }
    }

    public static String titleOf(Connection conn, String isbn) throws SQLException {
        // Une valeur venue de l'exterieur passe TOUJOURS par un ? (pas de concatenation).
        try (PreparedStatement ps = conn.prepareStatement("SELECT title FROM books WHERE isbn = ?")) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("title") : null;
            }
        }
    }

    public static List<String> sfTitles(Connection conn) throws SQLException {
        // while (rs.next()) parcourt toutes les lignes, dans l'ordre demande par ORDER BY.
        List<String> titles = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT title FROM books WHERE genre = ? ORDER BY pub_year")) {
            ps.setString(1, "SF");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    titles.add(rs.getString(1));
                }
            }
        }
        return titles;
    }

    public static double priceOf(Connection conn, String isbn) throws SQLException {
        // getDouble convertit le DECIMAL SQL (getBigDecimal le garderait exact).
        try (PreparedStatement ps = conn.prepareStatement("SELECT price FROM books WHERE isbn = ?")) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getDouble("price");
            }
        }
    }

    public static String emailOf(Connection conn, String id) throws SQLException {
        // getString rend null pour un NULL SQL (contrairement a getInt qui rendrait 0).
        try (PreparedStatement ps = conn.prepareStatement("SELECT email FROM members WHERE id = ?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                String email = rs.getString("email");
                return email == null ? "aucun" : email;
            }
        }
    }

    public static boolean memberExists(Connection conn, String id) throws SQLException {
        // Un resultat vide n'est pas une erreur : next() rend simplement false.
        try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM members WHERE id = ?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static int bookColumns(Connection conn) throws SQLException {
        // Les metadonnees decrivent le resultat sans le parcourir.
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM books")) {
            return rs.getMetaData().getColumnCount();
        }
    }

    public static String executeSays(Connection conn) throws SQLException {
        // execute rend true s'il y a un ResultSet (SELECT), false s'il y a un compte de lignes (UPDATE).
        try (Statement st = conn.createStatement()) {
            boolean select = st.execute("SELECT * FROM books");
            boolean update = st.execute("UPDATE books SET pages = pages WHERE isbn = 'B1'");
            return select + " " + update;
        }
    }

    public static int lateLoans(Connection conn) throws SQLException {
        // Le seuil est un parametre : le meme ordre sert pour n'importe quelle valeur.
        try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM loans WHERE days_late > ?")) {
            ps.setInt(1, 0);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}
