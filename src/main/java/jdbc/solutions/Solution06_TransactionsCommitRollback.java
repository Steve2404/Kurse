package jdbc.solutions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans jdbc.exercises.Exercise06_TransactionsCommitRollback.
 */
public class Solution06_TransactionsCommitRollback {

    public static boolean transfer(Connection conn, int fromId, int toId, int amount) throws SQLException {
        conn.setAutoCommit(false);
        try {
            int rowsDebited;
            try (PreparedStatement debit = conn.prepareStatement(
                    "UPDATE accounts SET balance = balance - ? WHERE id = ? AND balance >= ?")) {
                debit.setInt(1, amount);
                debit.setInt(2, fromId);
                debit.setInt(3, amount);
                rowsDebited = debit.executeUpdate();
            }

            if (rowsDebited == 0) {
                conn.rollback();
                return false;
            }

            try (PreparedStatement credit = conn.prepareStatement(
                    "UPDATE accounts SET balance = balance + ? WHERE id = ?")) {
                credit.setInt(1, amount);
                credit.setInt(2, toId);
                credit.executeUpdate();
            }

            conn.commit();
            return true;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
