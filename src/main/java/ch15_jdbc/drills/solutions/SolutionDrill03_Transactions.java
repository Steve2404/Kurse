package ch15_jdbc.drills.solutions;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

/**
 * Corrige du drill 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch15_jdbc.drills.exercises.Drill03_Transactions.
 */
public class SolutionDrill03_Transactions {

    public static boolean autoCommitAtStart(Connection conn) throws SQLException {
        // Une connexion neuve valide chaque ordre tout seul.
        return conn.getAutoCommit();
    }

    public static void commitTwo(Connection conn) throws SQLException {
        // Hors auto-commit, rien n'est definitif avant commit().
        conn.setAutoCommit(false);
        insert(conn, "M5");
        insert(conn, "M6");
        conn.commit();
    }

    public static void rollbackOne(Connection conn) throws SQLException {
        // rollback annule tout ce qui n'a pas ete valide.
        conn.setAutoCommit(false);
        insert(conn, "M5");
        conn.rollback();
    }

    public static void keepFirstOnly(Connection conn) throws SQLException {
        // rollback(sp) n'annule que ce qui suit le savepoint, et ne termine pas la transaction : commit garde M5.
        conn.setAutoCommit(false);
        insert(conn, "M5");
        Savepoint sp = conn.setSavepoint();
        insert(conn, "M6");
        conn.rollback(sp);
        conn.commit();
    }

    public static String undoOnError(Connection conn) throws SQLException {
        // Le doublon lance une SQLException : on annule aussi le M5 deja insere.
        conn.setAutoCommit(false);
        try {
            insert(conn, "M5");
            insert(conn, "M1");
            conn.commit();
            return "valide";
        } catch (SQLException e) {
            conn.rollback();
            return "annule";
        }
    }

    public static boolean restoreAutoCommit(Connection conn) throws SQLException {
        // finally rend la connexion dans son etat d'origine, meme si le commit echouait.
        try {
            commitTwo(conn);
        } finally {
            conn.setAutoCommit(true);
        }
        return conn.getAutoCommit();
    }

    public static void twoSavepoints(Connection conn) throws SQLException {
        // Revenir au 1er savepoint annule aussi tout ce qui suit le 2e.
        conn.setAutoCommit(false);
        Savepoint first = conn.setSavepoint("premier");
        insert(conn, "M5");
        conn.setSavepoint("second");
        insert(conn, "M6");
        conn.rollback(first);
        conn.commit();
    }

    public static String useClosed(Connection conn) {
        // Apres close(), chaque appel sur la connexion lance une SQLException.
        try {
            conn.close();
            conn.createStatement();
            return "aucune";
        } catch (SQLException e) {
            return "SQLException";
        }
    }

    private static void insert(Connection conn, String id) throws SQLException {
        // Boite magique : un membre de plus (id, nom = "Nouveau").
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("INSERT INTO members (id, name) VALUES ('" + id + "', 'Nouveau')");
        }
    }
}
