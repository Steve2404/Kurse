package ch15_jdbc.exercises;

import ch15_jdbc.ExerciseChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * EXERCICE 8 - L'ordre de fermeture des ressources JDBC, et leurs effets en cascade (niveau : difficile)
 * =========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_JdbcUrlAndDriverManager.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une Connection, un Statement et un ResultSet, c'est comme des
 * poupees russes : le ResultSet vit A L'INTERIEUR d'un Statement, qui
 * vit A L'INTERIEUR d'une Connection. Fermer une poupee EXTERIEURE
 * ferme AUTOMATIQUEMENT tout ce qu'elle contenait - mais l'ordre
 * RECOMMANDE pour fermer explicitement reste "du plus petit au plus
 * grand" : ResultSet, PUIS Statement, PUIS Connection.
 *
 *
 * ==================================================================
 * TODO : closeInOrder(rs, st, conn)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. rs.close().
 *   2. st.close().
 *   3. conn.close().
 *      (dans CET ordre precis, meme si en pratique try-with-resources
 *      fait deja ca tout seul, dans l'ordre inverse de declaration -
 *      cet exercice le fait "a la main" pour bien le memoriser).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : 3 lignes suffisent.
 *
 *
 * ------------------------------------------------------------------
 * Ce qu'on observe en plus dans main() (deja fourni, a lire attentivement)
 * ------------------------------------------------------------------
 *
 * 3 comportements "automatiques" a connaitre par coeur pour l'examen :
 *
 *   A. Fermer un Statement ferme AUTOMATIQUEMENT le ResultSet qu'il a
 *      produit - rs.isClosed() devient true, meme sans jamais avoir
 *      appele rs.close() soi-meme.
 *   B. Executer une NOUVELLE requete sur le MEME Statement ferme
 *      AUTOMATIQUEMENT l'ANCIEN ResultSet de ce Statement (un
 *      Statement ne garde jamais qu'UN SEUL ResultSet "actif" a la
 *      fois).
 *   C. Fermer une Connection ferme en cascade ses Statement (et leurs
 *      ResultSet) - PIEGE : selon le pilote JDBC utilise (ici H2),
 *      isClosed() sur le Statement peut encore repondre false meme
 *      apres la fermeture de la Connection (un detail d'implementation
 *      qui varie d'un fournisseur a l'autre) ! La preuve FIABLE, elle,
 *      est de constater qu'ESSAYER de s'en servir lance alors une
 *      SQLException ("deja ferme") - c'est CA le comportement garanti
 *      par la specification JDBC, pas forcement isClosed().
 *
 * Exemple a verifier : voir main().
 */
public class Exercise08_ResourceClosingOrder {

    public static void closeInOrder(ResultSet rs, Statement st, Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO : implementer closeInOrder()");
    }

    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:exercice08", "sa", "")) {
            try (Statement setup = connection.createStatement()) {
                setup.executeUpdate("CREATE TABLE t (id INT)");
                setup.executeUpdate("INSERT INTO t VALUES (1)");
            }

            // TODO teste ici : fermer dans l'ordre rs -> st -> conn.
            Connection dedicatedConnection = DriverManager.getConnection("jdbc:h2:mem:exercice08", "sa", "");
            Statement dedicatedStatement = dedicatedConnection.createStatement();
            ResultSet dedicatedResultSet = dedicatedStatement.executeQuery("SELECT id FROM t");
            closeInOrder(dedicatedResultSet, dedicatedStatement, dedicatedConnection);
            ExerciseChecker.check("closeInOrder() a bien ferme le ResultSet", dedicatedResultSet.isClosed());
            ExerciseChecker.check("closeInOrder() a bien ferme le Statement", dedicatedStatement.isClosed());
            ExerciseChecker.check("closeInOrder() a bien ferme la Connection", dedicatedConnection.isClosed());

            // A. Fermer le Statement ferme automatiquement son ResultSet.
            Statement stA = connection.createStatement();
            ResultSet rsA = stA.executeQuery("SELECT id FROM t");
            stA.close();
            ExerciseChecker.check("A. fermer le Statement ferme AUSSI son ResultSet", rsA.isClosed());

            // B. Relancer une requete sur le MEME Statement ferme l'ancien ResultSet.
            Statement stB = connection.createStatement();
            ResultSet rsB1 = stB.executeQuery("SELECT id FROM t");
            ResultSet rsB2 = stB.executeQuery("SELECT id FROM t");
            ExerciseChecker.check("B. une NOUVELLE requete sur le meme Statement ferme l'ANCIEN ResultSet",
                    rsB1.isClosed());
            stB.close();
            rsB2.close();

            // C. Fermer la Connection ferme le Statement (et son ResultSet) EN CASCADE.
            Connection secondConnection = DriverManager.getConnection("jdbc:h2:mem:exercice08", "sa", "");
            Statement stC = secondConnection.createStatement();
            ResultSet rsC = stC.executeQuery("SELECT id FROM t");
            secondConnection.close();

            boolean threwOnStatement = false;
            try {
                stC.executeQuery("SELECT id FROM t");
            } catch (SQLException e) {
                threwOnStatement = true;
            }
            ExerciseChecker.check("C. utiliser le Statement APRES la fermeture de sa Connection leve une SQLException",
                    threwOnStatement);

            boolean threwOnResultSet = false;
            try {
                rsC.next();
            } catch (SQLException e) {
                threwOnResultSet = true;
            }
            ExerciseChecker.check("C. utiliser le ResultSet APRES la fermeture de sa Connection leve une SQLException",
                    threwOnResultSet);
        }

        ExerciseChecker.summary();
    }
}
