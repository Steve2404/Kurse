package ch15_jdbc.exercises;

import ch15_jdbc.ExerciseChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/**
 * EXERCICE 5 - CallableStatement : appeler une procedure stockee (niveau : difficile)
 * ====================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_JdbcUrlAndDriverManager.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * PreparedStatement (Exercice 2/3) : le SQL est ECRIT DANS TON CODE
 * JAVA. CallableStatement : le "SQL" est en realite une PROCEDURE deja
 * enregistree DANS la base, et tu te contentes de l'APPELER par son
 * nom, avec une syntaxe speciale entre accolades : { call
 * ma_procedure(?) }. Si la procedure RENVOIE une valeur, la syntaxe
 * devient { ? = call ma_procedure(?) } (le tout premier '?', AVANT le
 * signe =, represente ce qui sera RENVOYE).
 *
 * Ce projet utilise une astuce d'H2 (ALIAS) pour transformer une
 * simple methode Java statique en "procedure stockee" appelable en
 * SQL - deja fait pour toi plus bas (pas un TODO), pour se concentrer
 * sur l'utilisation de CallableStatement lui-meme.
 *
 * -- Le piege des parametres OUT --
 *
 * Pour TOUT parametre qui doit RESSORTIR une valeur (OUT ou INOUT,
 * y compris le "resultat" special du "? = call ..."), il faut
 * OBLIGATOIREMENT appeler registerOutParameter(index, typeSql) AVANT
 * d'executer - sans ca, JDBC ne saurait pas dans quel type relire la
 * valeur de sortie.
 *
 *
 * ==================================================================
 * TODO : callApplyBonus(conn, baseScore, bonusPercent)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * applyBonus(100, 20) (deja implementee plus bas, cote Java) renvoie
 * 100 + (100*20/100) = 120 - le meme calcul, mais appele DEPUIS du
 * SQL via CallableStatement.
 *
 * -- Le plan --
 *
 *   1. conn.prepareCall("{? = call APPLY_BONUS(?, ?)}").
 *   2. registerOutParameter(1, Types.INTEGER) (le tout premier '?',
 *      celui AVANT le signe =, est le resultat renvoye).
 *   3. setInt(2, baseScore), setInt(3, bonusPercent) (les 2 parametres
 *      IN, dans l'ordre ou ils apparaissent APRES le signe =).
 *   4. execute() (pas executeQuery() ni executeUpdate() - execute()
 *      convient pour un CallableStatement, qu'il renvoie ou non des
 *      lignes).
 *   5. Renvoyer getInt(1) (relire le resultat, au MEME index que celui
 *      enregistre a l'etape 2).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule methode suffit.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Types.INTEGER vient de java.sql.Types - une liste de constantes
 *     representant les types SQL, independamment du driver utilise.
 */
public class Exercise05_CallableStatementProcedure {

    // Deja fournie (pas un TODO) : c'est CETTE methode que l'ALIAS SQL
    // "APPLY_BONUS" appelle reellement, en coulisses.
    public static int applyBonus(int baseScore, int bonusPercent) {
        return baseScore + (baseScore * bonusPercent / 100);
    }

    public static int callApplyBonus(Connection conn, int baseScore, int bonusPercent) throws SQLException {
        throw new UnsupportedOperationException("TODO : implementer callApplyBonus()");
    }

    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:exercice05", "sa", "")) {
            try (Statement setup = connection.createStatement()) {
                setup.executeUpdate("CREATE ALIAS APPLY_BONUS FOR "
                        + "\"jdbc.exercises.Exercise05_CallableStatementProcedure.applyBonus\"");
            }

            int result = callApplyBonus(connection, 100, 20);
            ExerciseChecker.check("callApplyBonus(100, 20) == 120 (calcule PAR LA PROCEDURE SQL)", result == 120);

            int resultZero = callApplyBonus(connection, 50, 0);
            ExerciseChecker.check("callApplyBonus(50, 0) == 50 (bonus nul)", resultZero == 50);
        }

        ExerciseChecker.summary();
    }
}
