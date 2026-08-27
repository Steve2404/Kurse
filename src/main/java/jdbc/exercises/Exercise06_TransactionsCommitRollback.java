package jdbc.exercises;

import jdbc.ExerciseChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * EXERCICE 6 - Transactions : commit() et rollback() (niveau : difficile)
 * ========================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_JdbcUrlAndDriverManager.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Par defaut, JDBC est en "autocommit" : CHAQUE instruction SQL est
 * validee TOUTE SEULE, immediatement. Pour un virement bancaire (2
 * operations : DEBITER un compte, CREDITER un autre), c'est
 * DANGEREUX : si le programme plante ENTRE les 2 operations, l'argent
 * disparait sans jamais reapparaitre ailleurs ! setAutoCommit(false)
 * dit "attends mon signal avant de VRAIMENT valider quoi que ce
 * soit" - commit() valide TOUT ce qui a ete fait depuis, ROLLBACK()
 * annule TOUT, comme si rien ne s'etait jamais passe.
 *
 *
 * ==================================================================
 * TODO : transfer(conn, fromId, toId, amount)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Compte 1 a 100, compte 2 a 50. transfer(1, 2, 30) : debite 30 du
 * compte 1 (reste 70), credite 30 au compte 2 (devient 80), PUIS
 * commit() - les 2 changements deviennent definitifs ENSEMBLE.
 *
 * transfer(1, 2, 1000) : le compte 1 n'a que 70, pas assez pour
 * debiter 1000 - AUCUNE des 2 operations ne doit avoir d'effet
 * definitif : rollback() annule tout, les 2 comptes restent
 * EXACTEMENT comme avant.
 *
 * -- Le plan --
 *
 *   1. conn.setAutoCommit(false) (desactiver la validation
 *      automatique).
 *   2. Dans un bloc try : debiter 'fromId' de 'amount', MAIS
 *      seulement SI son solde est suffisant (ajouter "AND balance >= ?"
 *      dans le WHERE du UPDATE - executeUpdate() renverra alors 0 si
 *      le solde etait insuffisant, sans jamais lancer d'exception).
 *   3. Si le debit a touche 0 ligne (solde insuffisant) :
 *      conn.rollback(), renvoyer false.
 *   4. Sinon : crediter 'toId' de 'amount', PUIS conn.commit(),
 *      renvoyer true.
 *   5. Dans un bloc finally : conn.setAutoCommit(true) (remettre le
 *      mode normal, pour ne pas "polluer" les appels suivants sur la
 *      MEME connexion).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule methode suffit, meme si elle est un peu plus longue
 * que les precedentes.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "UPDATE accounts SET balance = balance - ? WHERE id = ? AND balance >= ?"
 *     avec setInt(1, amount), setInt(2, fromId), setInt(3, amount) :
 *     le AND balance >= ? est la garde de securite qui empeche un
 *     solde negatif, DIRECTEMENT dans le SQL.
 */
public class Exercise06_TransactionsCommitRollback {

    public static boolean transfer(Connection conn, int fromId, int toId, int amount) throws SQLException {
        throw new UnsupportedOperationException("TODO : implementer transfer()");
    }

    private static int balanceOf(Connection conn, int id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT balance FROM accounts WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("balance");
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:exercice06", "sa", "")) {
            try (Statement setup = connection.createStatement()) {
                setup.executeUpdate("CREATE TABLE accounts (id INT PRIMARY KEY, balance INT)");
                setup.executeUpdate("INSERT INTO accounts VALUES (1, 100)");
                setup.executeUpdate("INSERT INTO accounts VALUES (2, 50)");
            }

            boolean success = transfer(connection, 1, 2, 30);
            ExerciseChecker.check("transfer(30) reussit et commit()", success);
            ExerciseChecker.check("apres commit, compte 1 == 70", balanceOf(connection, 1) == 70);
            ExerciseChecker.check("apres commit, compte 2 == 80", balanceOf(connection, 2) == 80);

            boolean failure = transfer(connection, 1, 2, 1000);
            ExerciseChecker.check("transfer(1000) echoue (solde insuffisant) et rollback()", !failure);
            ExerciseChecker.check("apres rollback, compte 1 INCHANGE (70)", balanceOf(connection, 1) == 70);
            ExerciseChecker.check("apres rollback, compte 2 INCHANGE (80)", balanceOf(connection, 2) == 80);
        }

        ExerciseChecker.summary();
    }
}
