package ch15_jdbc.solutions;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch15_jdbc.exercises.Exercise11_SchemaMigration.
 */
public class Solution11_SchemaMigration {

    @FunctionalInterface
    public interface Work {
        void run(Connection conn) throws SQLException;
    }

    public static void inTransaction(Connection conn, Work work) throws SQLException {
        // commit si tout va bien, rollback puis on relance sinon ; le finally rend la connexion dans son etat d'origine.
        boolean before = conn.getAutoCommit();
        conn.setAutoCommit(false);
        try {
            work.run(conn);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(before);
        }
    }

    public static String migrate(Connection conn, List<String> mandatory, List<String> optional) throws SQLException {
        // Une seule transaction : une obligatoire en echec fait tout annuler (via inTransaction) ;
        // une facultative en echec ne fait revenir qu'a SON savepoint.
        List<Integer> skipped = new ArrayList<>();
        int[] failedStep = {-1};
        try {
            inTransaction(conn, c -> {
                try (Statement st = c.createStatement()) {
                    for (int i = 0; i < mandatory.size(); i++) {
                        failedStep[0] = i;
                        st.executeUpdate(mandatory.get(i));
                    }
                    failedStep[0] = -1;
                    for (int i = 0; i < optional.size(); i++) {
                        Savepoint sp = c.setSavepoint();
                        try {
                            st.executeUpdate(optional.get(i));
                        } catch (SQLException e) {
                            c.rollback(sp);
                            skipped.add(i);
                        }
                    }
                }
            });
        } catch (SQLException e) {
            return "ECHEC etape " + failedStep[0];
        }
        return "OK ignorees=" + skipped;
    }
}
