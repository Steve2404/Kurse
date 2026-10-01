package ch15_jdbc.drills.exercises;

import ch15_jdbc.ExerciseChecker;
import ch15_jdbc.drills.LibraryDb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * DRILL 04 - Kata melange : les questions de la bibliotheque, mais en SQL et JDBC
 * ===============================================================================
 *
 * Mode d'emploi : voir Drill01_ConnectAndQuery. Ici, PAS de crochet : a
 * toi d'ecrire le SQL et de choisir la methode JDBC (comme le Drill12 du
 * chapitre 10, mais la base fait le travail des streams). Chaque TODO
 * recoit une bibliotheque NEUVE.
 *
 *
 * -- Les TODO --
 *
 * TODO 1 : booksPerAuthor(conn)       auteur -> nombre de livres, cles triees -> {Asimov=2, Gibson=1, Herbert=1, Orwell=2, Saint-Exupery=1, Tolkien=1}.
 * TODO 2 : titlesBorrowedBy(conn, id) les titres empruntes par un membre, tries -> M2 : [1984, Dune, La Ferme des animaux].
 * TODO 3 : totalLateDays(conn)        la somme des jours de retard -> 19.
 * TODO 4 : mostBorrowed(conn)         le titre le plus emprunte -> Dune (2 emprunts).
 * TODO 5 : withoutLoans(conn)         les noms des membres sans aucun emprunt -> [Tom].
 * TODO 6 : averagePrice(conn, genre)  le prix moyen d'un genre -> SF : 8.5.
 * TODO 7 : monthReport(conn, month)   "nom: titre" des emprunts d'un mois, tries par nom puis titre.
 * TODO 8 : tryAddBook(conn, isbn)     inserer un livre ; rendre "OK", ou la classe de SQLState (2 caracteres) si ca echoue -> B1 : "23".
 */
public class Drill04_MixedKata {

    public static Map<String, Integer> booksPerAuthor(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 1 : implementer booksPerAuthor()");
    }

    public static List<String> titlesBorrowedBy(Connection conn, String memberId) throws SQLException {
        throw new UnsupportedOperationException("TODO 2 : implementer titlesBorrowedBy()");
    }

    public static int totalLateDays(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 3 : implementer totalLateDays()");
    }

    public static String mostBorrowed(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 4 : implementer mostBorrowed()");
    }

    public static List<String> withoutLoans(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 5 : implementer withoutLoans()");
    }

    public static double averagePrice(Connection conn, String genre) throws SQLException {
        throw new UnsupportedOperationException("TODO 6 : implementer averagePrice()");
    }

    public static List<String> monthReport(Connection conn, int month) throws SQLException {
        throw new UnsupportedOperationException("TODO 7 : implementer monthReport()");
    }

    public static String tryAddBook(Connection conn, String isbn) {
        throw new UnsupportedOperationException("TODO 8 : implementer tryAddBook()");
    }

    public static void main(String[] args) throws SQLException {
        try (Connection conn = LibraryDb.open()) {
            ExerciseChecker.check("1  booksPerAuthor", "{Asimov=2, Gibson=1, Herbert=1, Orwell=2, Saint-Exupery=1, Tolkien=1}".equals(String.valueOf(booksPerAuthor(conn))));
            ExerciseChecker.check("2  titlesBorrowedBy(M2) == [1984, Dune, La Ferme des animaux]",
                    List.of("1984", "Dune", "La Ferme des animaux").equals(titlesBorrowedBy(conn, "M2")));
            ExerciseChecker.check("3  totalLateDays == 19", totalLateDays(conn) == 19);
            ExerciseChecker.check("4  mostBorrowed == Dune", "Dune".equals(mostBorrowed(conn)));
            ExerciseChecker.check("5  withoutLoans == [Tom]", List.of("Tom").equals(withoutLoans(conn)));
            ExerciseChecker.check("6  averagePrice(SF) == 8.5", Math.abs(averagePrice(conn, "SF") - 8.5) < 1e-9);
            ExerciseChecker.check("7  monthReport(3) == [Hugo: Dune, Hugo: La Ferme des animaux, Ines: Neuromancien, Lea: Le Petit Prince]",
                    List.of("Hugo: Dune", "Hugo: La Ferme des animaux", "Ines: Neuromancien", "Lea: Le Petit Prince").equals(monthReport(conn, 3)));
            ExerciseChecker.check("8  tryAddBook(B9) == OK ; tryAddBook(B1) == 23", "OK".equals(tryAddBook(conn, "B9")) && "23".equals(tryAddBook(conn, "B1")));
        }

        ExerciseChecker.summary();
    }
}
