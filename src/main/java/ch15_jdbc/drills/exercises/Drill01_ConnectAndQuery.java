package ch15_jdbc.drills.exercises;

import ch15_jdbc.ExerciseChecker;
import ch15_jdbc.drills.LibraryDb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * DRILL 01 - Se connecter et lire : DriverManager, Statement, PreparedStatement, ResultSet
 * ========================================================================================
 *
 * -- Comment utiliser un DRILL (different d'un exercice) --
 *
 * Un exercice t'APPREND une notion. Un drill te la fait REPETER jusqu'a
 * ce qu'elle sorte toute seule. Chaque TODO tient en quelques lignes et
 * vise UNE forme precise (entre crochets).
 *
 *   1. Chronometre-toi, note ton temps et ton score dans drills/REVISION.md.
 *   2. Ecris SANS regarder la "carte memoire" en bas. Bloque plus d'une
 *      minute : regarde-la, cache-la, reecris.
 *   3. Refais le MEME drill plus tard, a partir de zero (voir REVISION.md).
 *
 * Donnees : ch15_jdbc.drills.LibraryDb (la bibliotheque du chapitre 10 en
 * tables SQL, sur H2 en memoire). Toujours try-with-resources.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : connect(name)          [DriverManager.getConnection] une base H2 en memoire nommee name ("jdbc:h2:mem:" + name).
 * TODO 2  : bookCount(conn)        [Statement + executeQuery + COUNT(*) + getInt(1)] -> 8.
 * TODO 3  : titleOf(conn, isbn)    [PreparedStatement + setString + getString] B1 -> Dune.
 * TODO 4  : sfTitles(conn)         [WHERE genre = ? ORDER BY pub_year + boucle next()] -> [Les Robots, Fondation, Dune, Neuromancien].
 * TODO 5  : priceOf(conn, isbn)    [getDouble] B8 -> 10.0.
 * TODO 6  : emailOf(conn, id)      [getString rend null pour un NULL SQL] M2 -> "aucun", M1 -> lea@mail.fr.
 * TODO 7  : memberExists(conn, id) [next() rend false sans ligne] M9 -> false, M1 -> true.
 * TODO 8  : bookColumns(conn)      [ResultSetMetaData.getColumnCount] SELECT * FROM books -> 7.
 * TODO 9  : executeSays(conn)      [execute : true pour un SELECT, false sinon] "SELECT ..." puis "UPDATE ..." -> "true false".
 * TODO 10 : lateLoans(conn)        [COUNT avec WHERE days_late > ?] seuil 0 -> 4.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Connection c = DriverManager.getConnection("jdbc:h2:mem:nom" [, user, motDePasse]);
 *   jdbc:<fournisseur>:<le reste>   ex. jdbc:postgresql://hote:5432/base ; jdbc:mysql://hote:3306/base
 *   try (PreparedStatement ps = c.prepareStatement("... WHERE x = ?")) { ps.setString(1, v); try (ResultSet rs = ps.executeQuery()) { ... } }
 *   rs.next() avance (false a la fin) ; getInt / getString / getDouble / getObject (index a partir de 1, ou nom de colonne)
 *   getInt sur NULL -> 0 (wasNull()) ; getString sur NULL -> null
 *   execute -> boolean ; executeQuery -> ResultSet ; executeUpdate -> int
 *   rs.getMetaData() : getColumnCount, getColumnLabel(i), getColumnTypeName(i)
 * ---------------------------------------------------------------------
 */
public class Drill01_ConnectAndQuery {

    public static Connection connect(String name) throws SQLException {
        throw new UnsupportedOperationException("TODO 1 : implementer connect()");
    }

    public static int bookCount(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 2 : implementer bookCount()");
    }

    public static String titleOf(Connection conn, String isbn) throws SQLException {
        throw new UnsupportedOperationException("TODO 3 : implementer titleOf()");
    }

    public static List<String> sfTitles(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 4 : implementer sfTitles()");
    }

    public static double priceOf(Connection conn, String isbn) throws SQLException {
        throw new UnsupportedOperationException("TODO 5 : implementer priceOf()");
    }

    public static String emailOf(Connection conn, String id) throws SQLException {
        throw new UnsupportedOperationException("TODO 6 : implementer emailOf()");
    }

    public static boolean memberExists(Connection conn, String id) throws SQLException {
        throw new UnsupportedOperationException("TODO 7 : implementer memberExists()");
    }

    public static int bookColumns(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 8 : implementer bookColumns()");
    }

    public static String executeSays(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 9 : implementer executeSays()");
    }

    public static int lateLoans(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 10 : implementer lateLoans()");
    }

    public static void main(String[] args) throws SQLException {
        try (Connection own = connect("drill01")) {
            ExerciseChecker.check("1  connect : connexion valide", own != null && own.isValid(1));
        }
        try (Connection conn = LibraryDb.open()) {
            ExerciseChecker.check("2  bookCount == 8", bookCount(conn) == 8);
            ExerciseChecker.check("3  titleOf(B1) == Dune", "Dune".equals(titleOf(conn, "B1")));
            ExerciseChecker.check("4  sfTitles == [Les Robots, Fondation, Dune, Neuromancien]",
                    List.of("Les Robots", "Fondation", "Dune", "Neuromancien").equals(sfTitles(conn)));
            ExerciseChecker.check("5  priceOf(B8) == 10.0", priceOf(conn, "B8") == 10.0);
            ExerciseChecker.check("6  emailOf(M2) == aucun ; (M1) == lea@mail.fr", "aucun".equals(emailOf(conn, "M2")) && "lea@mail.fr".equals(emailOf(conn, "M1")));
            ExerciseChecker.check("7  memberExists(M9) == false ; (M1) == true", !memberExists(conn, "M9") && memberExists(conn, "M1"));
            ExerciseChecker.check("8  bookColumns == 7", bookColumns(conn) == 7);
            ExerciseChecker.check("9  executeSays == true false", "true false".equals(executeSays(conn)));
            ExerciseChecker.check("10 lateLoans == 4", lateLoans(conn) == 4);
        }

        ExerciseChecker.summary();
    }
}
