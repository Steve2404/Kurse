package ch15_jdbc.projects.p01_library.solution;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Le catalogue : toutes les requetes SQL du projet. Il ne ferme pas la connexion (ce n'est pas lui qui l'a ouverte).
 */
final class Catalog {

    private final Connection conn;

    Catalog(Connection conn) {
        this.conn = conn;
    }

    // Une ligne du ResultSet -> un Book. getInt rend 0 pour un NULL : wasNull() le distingue d'un vrai 0.
    private static Book read(ResultSet rs) throws SQLException {
        int pages = rs.getInt("pages");
        Integer nullablePages = rs.wasNull() ? null : pages;
        return new Book(rs.getString("isbn"), rs.getString(2), rs.getString("author"), rs.getInt("pub_year"), nullablePages);
    }

    // Les ? sont numerotes a partir de 1. setNull a besoin du TYPE SQL de la colonne.
    int add(Book b) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO books (isbn, title, author, pub_year, pages) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, b.isbn());
            ps.setString(2, b.title());
            ps.setString(3, b.author());
            ps.setInt(4, b.pubYear());
            if (b.pages() == null) {
                ps.setNull(5, Types.INTEGER);
            } else {
                ps.setInt(5, b.pages());
            }
            return ps.executeUpdate();
        }
    }

    List<Book> byAuthor(String author) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM books WHERE author = ? ORDER BY pub_year")) {
            ps.setString(1, author);
            try (ResultSet rs = ps.executeQuery()) {
                List<Book> found = new ArrayList<>();
                while (rs.next()) {
                    found.add(read(rs));
                }
                return found;
            }
        }
    }

    // Une cle primaire : 0 ou 1 ligne, donc un if (rs.next()) suffit.
    Optional<Book> find(String isbn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM books WHERE isbn = ?")) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(read(rs)) : Optional.empty();
            }
        }
    }

    // La pagination : LIMIT (taille) et OFFSET (lignes sautees) sont aussi des parametres.
    List<String> search(String word, int page, int size) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT title FROM books WHERE LOWER(title) LIKE ? ORDER BY title LIMIT ? OFFSET ?")) {
            ps.setString(1, "%" + word.toLowerCase() + "%");
            ps.setInt(2, size);
            ps.setInt(3, (page - 1) * size);
            try (ResultSet rs = ps.executeQuery()) {
                List<String> titles = new ArrayList<>();
                while (rs.next()) {
                    titles.add(rs.getString(1));
                }
                return titles;
            }
        }
    }

    // Une colonne calculee porte le nom de son alias (AS century).
    Map<Integer, Integer> countByCentury() throws SQLException {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT pub_year / 100 * 100 AS century, COUNT(*) AS n FROM books GROUP BY pub_year / 100 * 100")) {
            Map<Integer, Integer> counts = new TreeMap<>();
            while (rs.next()) {
                counts.put(rs.getInt("century"), rs.getInt("n"));
            }
            return counts;
        }
    }

    // executeUpdate rend le nombre de lignes touchees (0 si aucune ne correspond : ce n'est pas une erreur).
    int addPages(String author, int extra) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE books SET pages = pages + ? WHERE author = ?")) {
            ps.setInt(1, extra);
            ps.setString(2, author);
            return ps.executeUpdate();
        }
    }

    int deleteBefore(int year) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM books WHERE pub_year < ?")) {
            ps.setInt(1, year);
            return ps.executeUpdate();
        }
    }

    int count() throws SQLException {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM books")) {
            rs.next();
            return rs.getInt(1);
        }
    }

    // DANGER : la saisie est collee dans le SQL. Une apostrophe change le sens de la requete (injection).
    int countByTitleUnsafe(String title) throws SQLException {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM books WHERE title = '" + title + "'")) {
            rs.next();
            return rs.getInt(1);
        }
    }

    // Le ? transporte une VALEUR, jamais du SQL : l'apostrophe reste une simple lettre.
    int countByTitle(String title) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM books WHERE title = ?")) {
            ps.setString(1, title);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}
