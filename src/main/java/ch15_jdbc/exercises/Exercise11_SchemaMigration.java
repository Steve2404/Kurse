package ch15_jdbc.exercises;

import ch15_jdbc.ExerciseChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * EXERCICE 11 - Une migration de donnees en transaction : tout ou rien, et des etapes facultatives avec Savepoint (niveau : avance)
 * =================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_JdbcUrlAndDriverManager.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une migration applique plusieurs ordres SQL. Les etapes OBLIGATOIRES
 * passent toutes, ou aucune (une seule transaction : rollback() si l'une
 * echoue). Les etapes FACULTATIVES peuvent echouer sans tout casser :
 * on pose un Savepoint juste avant chacune, et en cas d'echec on revient
 * a ce Savepoint (rollback(savepoint)) puis on continue.
 *
 * Toujours : setAutoCommit(false) au debut, et REMETTRE l'auto-commit
 * comme avant a la fin (finally), sinon la connexion reste "piegee" pour
 * le code qui la reutilise.
 *
 *
 * ==================================================================
 * TODO 1 : inTransaction(conn, work)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une boite magique reutilisable : executer work dans une transaction.
 * Succes -> commit(). SQLException -> rollback() puis relancer
 * l'exception. Dans TOUS les cas -> remettre l'auto-commit d'origine.
 *
 * -- Le plan --
 *
 *   1. boolean before = conn.getAutoCommit() ; conn.setAutoCommit(false).
 *   2. try { work.run(conn) ; conn.commit() ; } catch (SQLException e) { conn.rollback() ; throw e ; }
 *   3. finally { conn.setAutoCommit(before) ; }
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non, mais inTransaction EST la boite magique du TODO 2.
 *
 *
 * ==================================================================
 * TODO 2 : migrate(conn, mandatory, optional)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Executer les ordres mandatory puis optional dans UNE transaction.
 *   - Un ordre obligatoire echoue -> tout est annule ; rendre "ECHEC etape N" (N = son index).
 *   - Un ordre facultatif echoue -> on revient a son Savepoint, on le note, on continue.
 *   - Fin -> commit ; rendre "OK ignorees=[...]" (les index des facultatives ignorees).
 *
 * -- Essayons a la main --
 *
 *   obligatoires [INSERT 1 sf, UPDATE label en majuscules], facultatives [INSERT 1 doublon, INSERT 2 fantasy]
 *   -> "OK ignorees=[0]" ; la table contient 1 SF et 2 fantasy
 *   obligatoires [INSERT 10 x, INSERT INTO table_absente ...] -> "ECHEC etape 1" ; la ligne 10 n'existe pas
 *
 * -- Le plan --
 *
 *   1. Utiliser inTransaction pour les obligatoires ET les facultatives (une seule transaction).
 *   2. Obligatoire en echec : relancer une SQLException -> inTransaction fait le rollback ; attraper dehors.
 *   3. Facultative : Savepoint sp = conn.setSavepoint() ; try { execute } catch { conn.rollback(sp) ; noter }.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : inTransaction (TODO 1).
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Work est une interface fonctionnelle : inTransaction(conn, c -> { ... })
 *   - Un tableau d'une case (String[] result = {""}) permet a la lambda de rendre un texte.
 *   - Les ordres de DONNEES (INSERT, UPDATE) s'annulent ; attention, dans beaucoup de bases un CREATE TABLE
 *     valide implicitement la transaction : on le fait AVANT la migration.
 */
public class Exercise11_SchemaMigration {

    @FunctionalInterface
    public interface Work {
        void run(Connection conn) throws SQLException;
    }

    public static void inTransaction(Connection conn, Work work) throws SQLException {
        throw new UnsupportedOperationException("TODO 1 : implementer inTransaction()");
    }

    public static String migrate(Connection conn, List<String> mandatory, List<String> optional) throws SQLException {
        throw new UnsupportedOperationException("TODO 2 : implementer migrate()");
    }

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:ex11")) {
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("CREATE TABLE tags (id INT PRIMARY KEY, label VARCHAR(20))");
            }

            boolean rolledBack = false;
            try {
                inTransaction(conn, c -> {
                    try (Statement st = c.createStatement()) {
                        st.executeUpdate("INSERT INTO tags VALUES (99, 'temporaire')");
                        st.executeUpdate("INSERT INTO table_absente VALUES (1)");
                    }
                });
            } catch (SQLException e) {
                rolledBack = true;
            }
            ExerciseChecker.check("inTransaction : l'exception remonte, tout est annule, l'auto-commit est restaure",
                    rolledBack && count(conn, "SELECT COUNT(*) FROM tags") == 0 && conn.getAutoCommit());

            String ok = migrate(conn,
                    List.of("INSERT INTO tags VALUES (1, 'sf')", "UPDATE tags SET label = UPPER(label)"),
                    List.of("INSERT INTO tags VALUES (1, 'doublon')", "INSERT INTO tags VALUES (2, 'fantasy')"));
            ExerciseChecker.check("migrate (succes) == OK ignorees=[0], table = {1 SF, 2 fantasy}",
                    "OK ignorees=[0]".equals(ok) && count(conn, "SELECT COUNT(*) FROM tags") == 2
                            && count(conn, "SELECT COUNT(*) FROM tags WHERE id = 1 AND label = 'SF'") == 1);

            String failed = migrate(conn,
                    List.of("INSERT INTO tags VALUES (10, 'x')", "INSERT INTO table_absente VALUES (1)"),
                    List.of("INSERT INTO tags VALUES (11, 'y')"));
            ExerciseChecker.check("migrate (echec obligatoire) == ECHEC etape 1, rien n'est garde (ni 10 ni 11), auto-commit restaure",
                    "ECHEC etape 1".equals(failed) && count(conn, "SELECT COUNT(*) FROM tags WHERE id IN (10, 11)") == 0 && conn.getAutoCommit());
        }

        ExerciseChecker.summary();
    }

    static int count(Connection conn, String sql) throws SQLException {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return rs.getInt(1);
        }
    }
}
