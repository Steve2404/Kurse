package ch15_jdbc.drills.exercises;

import ch15_jdbc.ExerciseChecker;
import ch15_jdbc.drills.LibraryDb;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * DRILL 02 - Ecrire : executeUpdate, setNull, reutiliser un PreparedStatement, batch, cle generee, CallableStatement
 * ==================================================================================================================
 *
 * Mode d'emploi : voir Drill01_ConnectAndQuery. Chaque TODO recoit une
 * bibliotheque NEUVE (LibraryDb.open()).
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : addMember(conn)          [INSERT + executeUpdate] M5 Zoe 29 (email NULL) -> 1.
 * TODO 2  : raiseSfPrices(conn)      [UPDATE ... WHERE genre = ?] +1 sur les prix SF -> 4 lignes.
 * TODO 3  : forgetLea(conn)          [DELETE ... WHERE member_id = ?] les emprunts de M1 -> 3.
 * TODO 4  : createNotes(conn)        [executeUpdate d'un CREATE TABLE] notes (id INT AUTO_INCREMENT PRIMARY KEY, txt VARCHAR(50)) -> 0.
 * TODO 5  : clearEmail(conn)         [setNull(i, Types.VARCHAR)] mettre l'email de M1 a NULL ; rendre true s'il est bien NULL apres.
 * TODO 6  : twoLoans(conn)           [reutiliser UN PreparedStatement] deux INSERT dans loans avec le meme ps ; rendre la somme des comptes -> 2.
 * TODO 7  : batchMembers(conn)       [addBatch + executeBatch] M6, M7, M8 en un voyage ; rendre la longueur du tableau -> 3.
 * TODO 8  : insertNote(conn)         [RETURN_GENERATED_KEYS + getGeneratedKeys] (apres TODO 4) inserer "rappel" ; rendre l'id -> 1.
 * TODO 9  : lateFee(conn, days)      [CallableStatement "{? = call LATE_FEE(?)}"] 10 jours -> 500.
 * TODO 10 : updateNobody(conn)       [executeUpdate sans ligne touchee] UPDATE books ... WHERE isbn = 'B99' -> 0.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   executeUpdate -> int (lignes touchees ; 0 pour un CREATE / DROP) ; executeQuery sur un INSERT -> SQLException
 *   ps.setNull(i, java.sql.Types.VARCHAR) ; setObject(i, valeur) ; ps.clearParameters()
 *   un PreparedStatement se reutilise : nouveaux setX puis executeUpdate a nouveau
 *   addBatch() ... int[] comptes = ps.executeBatch()
 *   conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) ; ps.getGeneratedKeys() -> ResultSet
 *   CallableStatement cs = conn.prepareCall("{? = call F(?)}") ; cs.registerOutParameter(1, Types.INTEGER) ; cs.execute() ; cs.getInt(1)
 *   procedure avec parametre OUT : "{call P(?, ?)}" + registerOutParameter(2, ...) ; INOUT : setX ET registerOutParameter
 * ---------------------------------------------------------------------
 */
public class Drill02_UpdatesAndCalls {

    public static int addMember(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 1 : implementer addMember()");
    }

    public static int raiseSfPrices(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 2 : implementer raiseSfPrices()");
    }

    public static int forgetLea(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 3 : implementer forgetLea()");
    }

    public static int createNotes(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 4 : implementer createNotes()");
    }

    public static boolean clearEmail(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 5 : implementer clearEmail()");
    }

    public static int twoLoans(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 6 : implementer twoLoans()");
    }

    public static int batchMembers(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 7 : implementer batchMembers()");
    }

    public static int insertNote(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 8 : implementer insertNote()");
    }

    public static int lateFee(Connection conn, int days) throws SQLException {
        throw new UnsupportedOperationException("TODO 9 : implementer lateFee()");
    }

    public static int updateNobody(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 10 : implementer updateNobody()");
    }

    public static void main(String[] args) throws SQLException {
        try (Connection conn = LibraryDb.open()) {
            ExerciseChecker.check("1  addMember == 1", addMember(conn) == 1);
        }
        try (Connection conn = LibraryDb.open()) {
            ExerciseChecker.check("2  raiseSfPrices == 4", raiseSfPrices(conn) == 4);
        }
        try (Connection conn = LibraryDb.open()) {
            ExerciseChecker.check("3  forgetLea == 3", forgetLea(conn) == 3);
        }
        try (Connection conn = LibraryDb.open()) {
            ExerciseChecker.check("4  createNotes == 0", createNotes(conn) == 0);
            ExerciseChecker.check("8  insertNote == 1 (cle generee)", insertNote(conn) == 1);
        }
        try (Connection conn = LibraryDb.open()) {
            ExerciseChecker.check("5  clearEmail == true", clearEmail(conn));
        }
        try (Connection conn = LibraryDb.open()) {
            ExerciseChecker.check("6  twoLoans == 2", twoLoans(conn) == 2);
        }
        try (Connection conn = LibraryDb.open()) {
            ExerciseChecker.check("7  batchMembers == 3", batchMembers(conn) == 3);
        }
        try (Connection conn = LibraryDb.open()) {
            ExerciseChecker.check("9  lateFee(10) == 500", lateFee(conn, 10) == 500);
            ExerciseChecker.check("10 updateNobody == 0", updateNobody(conn) == 0);
        }

        ExerciseChecker.summary();
    }
}
