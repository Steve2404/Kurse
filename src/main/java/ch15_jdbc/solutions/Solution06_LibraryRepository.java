package ch15_jdbc.solutions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch15_jdbc.exercises.Exercise06_LibraryRepository.
 */
public class Solution06_LibraryRepository {

    public record Book(String isbn, String title, String author, int year) {
    }

    public static class LibraryRepository {
        private final Connection conn;

        public LibraryRepository(Connection conn) {
            this.conn = conn;
        }

        public int insert(Book book) throws SQLException {
            // Les ? sont numerotes a partir de 1, dans l'ordre ou ils apparaissent dans le SQL.
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO books VALUES (?, ?, ?, ?)")) {
                ps.setString(1, book.isbn());
                ps.setString(2, book.title());
                ps.setString(3, book.author());
                ps.setInt(4, book.year());
                return ps.executeUpdate();
            }
        }

        public Optional<Book> findByIsbn(String isbn) throws SQLException {
            // next() rend false s'il n'y a aucune ligne : Optional.empty(), jamais null.
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM books WHERE isbn = ?")) {
                ps.setString(1, isbn);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? Optional.of(toBook(rs)) : Optional.empty();
                }
            }
        }

        public List<Book> findByAuthor(String author) throws SQLException {
            // Le ? est une VALEUR, jamais du SQL : "' OR '1'='1" est cherche tel quel (injection impossible).
            List<Book> books = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM books WHERE author = ? ORDER BY pub_year")) {
                ps.setString(1, author);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        books.add(toBook(rs));
                    }
                }
            }
            return books;
        }

        public boolean updateTitle(String isbn, String title) throws SQLException {
            // executeUpdate rend le nombre de lignes touchees : 0 si l'isbn n'existe pas.
            try (PreparedStatement ps = conn.prepareStatement("UPDATE books SET title = ? WHERE isbn = ?")) {
                ps.setString(1, title);
                ps.setString(2, isbn);
                return ps.executeUpdate() == 1;
            }
        }

        public int deleteOlderThan(int year) throws SQLException {
            // Un seul ordre peut supprimer plusieurs lignes ; le compte vient d'executeUpdate.
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM books WHERE pub_year < ?")) {
                ps.setInt(1, year);
                return ps.executeUpdate();
            }
        }

        public Map<String, Integer> countByAuthor() throws SQLException {
            // Le regroupement est fait par la base (GROUP BY) ; on lit les colonnes par position.
            Map<String, Integer> counts = new TreeMap<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT author, COUNT(*) FROM books GROUP BY author");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    counts.put(rs.getString(1), rs.getInt(2));
                }
            }
            return counts;
        }

        private static Book toBook(ResultSet rs) throws SQLException {
            // Boite magique : la ligne COURANTE du ResultSet devient un Book (lecture par nom de colonne).
            return new Book(rs.getString("isbn"), rs.getString("title"), rs.getString("author"), rs.getInt("pub_year"));
        }
    }
}
