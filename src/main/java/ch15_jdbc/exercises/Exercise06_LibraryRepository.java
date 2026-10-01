package ch15_jdbc.exercises;

import ch15_jdbc.ExerciseChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * EXERCICE 6 - Un depot (DAO) complet pour la table books : PreparedStatement de bout en bout (niveau : avance)
 * =============================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_JdbcUrlAndDriverManager.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un "depot" (repository, ou DAO) cache le SQL derriere des methodes
 * Java : le reste du programme manipule des Book, jamais des ResultSet.
 * Chaque methode : un PreparedStatement (avec des ?), des setX, puis
 * executeUpdate ou executeQuery, le tout en try-with-resources.
 *
 *   CREATE TABLE books (isbn VARCHAR(10) PRIMARY KEY, title VARCHAR(100), author VARCHAR(50), pub_year INT)
 *
 * (La colonne s'appelle pub_year : YEAR est un mot RESERVE en SQL pour H2 2.x,
 * et "CREATE TABLE ... year INT" y donne une erreur de syntaxe.)
 *
 * Jamais de concatenation de valeurs dans le SQL ("... WHERE isbn = '" + isbn + "'") :
 * c'est la porte ouverte a l'injection SQL. Toujours des ?.
 *
 *
 * ==================================================================
 * TODO 1 : insert(book)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. INSERT INTO books VALUES (?, ?, ?, ?) ; setString / setInt dans l'ordre (index a partir de 1).
 *   2. Rendre executeUpdate() (1 ligne inseree).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : findByIsbn(isbn)   et   TODO 3 : findByAuthor(author)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. SELECT ... WHERE isbn = ? -> Optional.of(livre) si next(), sinon Optional.empty().
 *   2. SELECT ... WHERE author = ? ORDER BY pub_year -> une liste (boucle while (rs.next())).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui (Q2) : "transformer la ligne courante en Book" sert aux deux.
 *
 *
 * ==================================================================
 * TODO 4 : updateTitle(isbn, title)   et   TODO 5 : deleteOlderThan(year)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. UPDATE books SET title = ? WHERE isbn = ? -> true si executeUpdate() == 1.
 *   2. DELETE FROM books WHERE pub_year < ? -> le nombre de lignes supprimees.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : countByAuthor()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   -> {Asimov=2, Herbert=1, Orwell=2, Tolkien=1}
 *
 * -- Le plan --
 *
 *   1. SELECT author, COUNT(*) FROM books GROUP BY author.
 *   2. Remplir une TreeMap (cles triees) avec getString(1) et getInt(2).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - try (PreparedStatement ps = conn.prepareStatement(sql)) { ps.setString(1, isbn); try (ResultSet rs = ps.executeQuery()) { ... } }
 *   - new Book(rs.getString("isbn"), rs.getString("title"), rs.getString("author"), rs.getInt("pub_year"))
 */
public class Exercise06_LibraryRepository {

    public record Book(String isbn, String title, String author, int year) {
    }

    public static class LibraryRepository {
        private final Connection conn;

        public LibraryRepository(Connection conn) {
            this.conn = conn;
        }

        public int insert(Book book) throws SQLException {
            throw new UnsupportedOperationException("TODO 1 : implementer insert()");
        }

        public Optional<Book> findByIsbn(String isbn) throws SQLException {
            throw new UnsupportedOperationException("TODO 2 : implementer findByIsbn()");
        }

        public List<Book> findByAuthor(String author) throws SQLException {
            throw new UnsupportedOperationException("TODO 3 : implementer findByAuthor()");
        }

        public boolean updateTitle(String isbn, String title) throws SQLException {
            throw new UnsupportedOperationException("TODO 4 : implementer updateTitle()");
        }

        public int deleteOlderThan(int year) throws SQLException {
            throw new UnsupportedOperationException("TODO 5 : implementer deleteOlderThan()");
        }

        public Map<String, Integer> countByAuthor() throws SQLException {
            throw new UnsupportedOperationException("TODO 6 : implementer countByAuthor()");
        }
    }

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:ex06")) {
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("CREATE TABLE books (isbn VARCHAR(10) PRIMARY KEY, title VARCHAR(100), author VARCHAR(50), pub_year INT)");
            }
            LibraryRepository repo = new LibraryRepository(conn);
            int inserted = 0;
            for (Book b : List.of(new Book("B1", "Dune", "Herbert", 1965), new Book("B2", "Fondation", "Asimov", 1951),
                    new Book("B3", "Les Robots", "Asimov", 1950), new Book("B5", "1984", "Orwell", 1949),
                    new Book("B6", "La Ferme des animaux", "Orwell", 1945), new Book("B8", "Le Hobbit", "Tolkien", 1937))) {
                inserted += repo.insert(b);
            }
            ExerciseChecker.check("insert : 6 livres inseres (1 ligne chacun)", inserted == 6);
            ExerciseChecker.check("findByIsbn(B1) == Dune ; findByIsbn(B9) == vide",
                    repo.findByIsbn("B1").map(Book::title).equals(Optional.of("Dune")) && repo.findByIsbn("B9").isEmpty());
            ExerciseChecker.check("findByAuthor(Asimov) == [Les Robots (1950), Fondation (1951)]",
                    List.of("Les Robots", "Fondation").equals(repo.findByAuthor("Asimov").stream().map(Book::title).toList()));
            ExerciseChecker.check("injection impossible : findByAuthor(\"' OR '1'='1\") == []", repo.findByAuthor("' OR '1'='1").isEmpty());
            ExerciseChecker.check("countByAuthor == {Asimov=2, Herbert=1, Orwell=2, Tolkien=1}",
                    "{Asimov=2, Herbert=1, Orwell=2, Tolkien=1}".equals(String.valueOf(repo.countByAuthor())));
            ExerciseChecker.check("updateTitle(B8) == true et le titre change ; (B9) == false",
                    repo.updateTitle("B8", "Bilbo le Hobbit") && repo.findByIsbn("B8").map(Book::title).equals(Optional.of("Bilbo le Hobbit"))
                            && !repo.updateTitle("B9", "x"));
            ExerciseChecker.check("deleteOlderThan(1946) == 2 (1937, 1945)", repo.deleteOlderThan(1946) == 2 && repo.findByIsbn("B8").isEmpty());
        }

        ExerciseChecker.summary();
    }
}
