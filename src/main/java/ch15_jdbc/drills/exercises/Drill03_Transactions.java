package ch15_jdbc.drills.exercises;

import ch15_jdbc.ExerciseChecker;
import ch15_jdbc.drills.LibraryDb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DRILL 03 - Transactions : auto-commit, commit, rollback, Savepoint, connexion fermee
 * ====================================================================================
 *
 * Mode d'emploi : voir Drill01_ConnectAndQuery. Chaque TODO recoit une
 * bibliotheque NEUVE. main() compte les membres (4 au depart) pour
 * verifier ce qui a ete garde.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1 : autoCommitAtStart(conn)  [getAutoCommit] la valeur par defaut -> true.
 * TODO 2 : commitTwo(conn)          [setAutoCommit(false) + commit] inserer M5 et M6 puis valider -> 6 membres.
 * TODO 3 : rollbackOne(conn)        [rollback] inserer M5 puis annuler -> 4 membres.
 * TODO 4 : keepFirstOnly(conn)      [setSavepoint + rollback(savepoint)] M5, savepoint, M6, retour au savepoint, commit -> M5 garde, M6 non.
 * TODO 5 : undoOnError(conn)        [rollback dans un catch] M5 puis un INSERT en double (M1) qui echoue -> rien n'est garde ; rendre "annule".
 * TODO 6 : restoreAutoCommit(conn)  [finally] faire TODO 2 en remettant l'auto-commit dans un finally ; rendre getAutoCommit() apres -> true.
 * TODO 7 : twoSavepoints(conn)      [rollback au 1er savepoint] sp1, M5, sp2, M6, rollback(sp1), commit -> aucun des deux.
 * TODO 8 : useClosed(conn)          [connexion fermee] fermer conn puis createStatement() ; rendre "SQLException" si c'en est une.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   par defaut auto-commit = true : chaque ordre est valide tout seul
 *   conn.setAutoCommit(false) ; ... ; conn.commit()  /  conn.rollback()
 *   Savepoint sp = conn.setSavepoint() [ou setSavepoint("nom")] ; conn.rollback(sp) annule ce qui suit sp
 *   rollback(sp) ne termine PAS la transaction : il faut encore commit (ou rollback)
 *   repasser setAutoCommit(true) valide ce qui est en attente
 *   remettre l'etat dans un finally ; une Connection fermee -> SQLException a chaque appel
 * ---------------------------------------------------------------------
 */
public class Drill03_Transactions {

    public static boolean autoCommitAtStart(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 1 : implementer autoCommitAtStart()");
    }

    public static void commitTwo(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 2 : implementer commitTwo()");
    }

    public static void rollbackOne(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 3 : implementer rollbackOne()");
    }

    public static void keepFirstOnly(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 4 : implementer keepFirstOnly()");
    }

    public static String undoOnError(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 5 : implementer undoOnError()");
    }

    public static boolean restoreAutoCommit(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 6 : implementer restoreAutoCommit()");
    }

    public static void twoSavepoints(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 7 : implementer twoSavepoints()");
    }

    public static String useClosed(Connection conn) {
        throw new UnsupportedOperationException("TODO 8 : implementer useClosed()");
    }

    public static void main(String[] args) throws SQLException {
        try (Connection conn = LibraryDb.open()) {
            ExerciseChecker.check("1  autoCommitAtStart == true", autoCommitAtStart(conn));
        }
        try (Connection conn = LibraryDb.open()) {
            commitTwo(conn);
            ExerciseChecker.check("2  commitTwo -> 6 membres", members(conn) == 6);
        }
        try (Connection conn = LibraryDb.open()) {
            rollbackOne(conn);
            ExerciseChecker.check("3  rollbackOne -> 4 membres", members(conn) == 4);
        }
        try (Connection conn = LibraryDb.open()) {
            keepFirstOnly(conn);
            ExerciseChecker.check("4  keepFirstOnly -> 5 membres, dont M5 et pas M6", members(conn) == 5 && has(conn, "M5") && !has(conn, "M6"));
        }
        try (Connection conn = LibraryDb.open()) {
            ExerciseChecker.check("5  undoOnError == annule, 4 membres", "annule".equals(undoOnError(conn)) && members(conn) == 4);
        }
        try (Connection conn = LibraryDb.open()) {
            ExerciseChecker.check("6  restoreAutoCommit == true, 6 membres", restoreAutoCommit(conn) && members(conn) == 6);
        }
        try (Connection conn = LibraryDb.open()) {
            twoSavepoints(conn);
            ExerciseChecker.check("7  twoSavepoints -> 4 membres", members(conn) == 4);
        }
        ExerciseChecker.check("8  useClosed == SQLException", "SQLException".equals(useClosed(LibraryDb.open())));

        ExerciseChecker.summary();
    }

    static int members(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM members")) {
            rs.next();
            return rs.getInt(1);
        }
    }

    static boolean has(Connection conn, String id) throws SQLException {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM members WHERE id = '" + id + "'")) {
            rs.next();
            return rs.getInt(1) == 1;
        }
    }
}
