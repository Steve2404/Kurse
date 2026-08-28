package ch15_jdbc.exercises;

import ch15_jdbc.ExerciseChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * EXERCICE 2 - Creer, INSERT et SELECT avec PreparedStatement (niveau : difficile)
 * ================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_JdbcUrlAndDriverManager.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un PreparedStatement est une requete SQL avec des "trous" (les
 * points d'interrogation ?), ecrite UNE FOIS dans TON code Java. Le
 * SQL lui-meme est fige au moment de prepareStatement() (pas au
 * moment de l'execution) - seuls les TROUS changent d'un appel a
 * l'autre, remplis avec setInt()/setString()/... AVANT d'executer.
 *
 * Rappel des 4 operations CRUD du chapitre : CREATE (INSERT), READ
 * (SELECT), UPDATE, DELETE. executeUpdate() est utilisee pour
 * INSERT/UPDATE/DELETE (elle rend le NOMBRE de lignes touchees).
 * executeQuery() est UNIQUEMENT pour SELECT (elle rend un ResultSet).
 *
 *
 * ==================================================================
 * TODO 1 : createPlayersTable(conn)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Fabriquer un Statement simple (conn.createStatement()) - pas
 *      besoin de PreparedStatement ici, il n'y a AUCUN trou a
 *      remplir, le SQL est fige une fois pour toutes.
 *   2. executeUpdate("CREATE TABLE players (id INT PRIMARY KEY, name VARCHAR(50), score INT)").
 *
 *
 * ==================================================================
 * TODO 2 : insertPlayer(conn, id, name, score)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. prepareStatement("INSERT INTO players (id, name, score) VALUES (?, ?, ?)").
 *   2. Remplir CHAQUE trou, DANS L'ORDRE (les index des '?' commencent
 *      a 1, PAS 0) : setInt(1, id), setString(2, name), setInt(3, score).
 *   3. Renvoyer executeUpdate() (le nombre de lignes inserees - 1 ici).
 *
 *
 * ==================================================================
 * TODO 3 : selectAllPlayers(conn)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. prepareStatement("SELECT id, name, score FROM players ORDER BY id").
 *   2. executeQuery() rend un ResultSet - un CURSEUR qui pointe
 *      D'ABORD "avant la premiere ligne" (rs.next() doit etre appele
 *      AVANT de lire quoi que ce soit, meme pour la toute premiere
 *      ligne).
 *   3. Boucler tant que rs.next() renvoie true, et construire pour
 *      chaque ligne une chaine "id:name:score" (via getInt("id"),
 *      getString("name"), getInt("score") - par NOM de colonne ici),
 *      ajoutee a une liste.
 *   4. Renvoyer la liste.
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun est deja sa propre methode.
 *
 * Exemple a verifier : creer la table, inserer Steve (42 points) puis
 * Alice (99 points), relire tout : ["1:Steve:42", "2:Alice:99"],
 * TRIE par id grace au ORDER BY.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Toujours fermer Statement/PreparedStatement/ResultSet
 *     (try-with-resources) - voir Exercise08 pour le detail complet de
 *     l'ordre de fermeture.
 *   - Les index de colonnes ET les index de parametres bind (?)
 *     commencent TOUS LES DEUX a 1, jamais 0 - un piege classique de
 *     l'examen.
 */
public class Exercise02_PreparedStatementInsertSelect {

    public static void createPlayersTable(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 1 : implementer createPlayersTable()");
    }

    public static int insertPlayer(Connection conn, int id, String name, int score) throws SQLException {
        throw new UnsupportedOperationException("TODO 2 : implementer insertPlayer()");
    }

    public static List<String> selectAllPlayers(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO 3 : implementer selectAllPlayers()");
    }

    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:exercice02", "sa", "")) {
            createPlayersTable(connection);

            int inserted1 = insertPlayer(connection, 1, "Steve", 42);
            int inserted2 = insertPlayer(connection, 2, "Alice", 99);
            ExerciseChecker.check("insertPlayer() renvoie 1 ligne inseree a chaque appel",
                    inserted1 == 1 && inserted2 == 1);

            List<String> players = selectAllPlayers(connection);
            ExerciseChecker.check("selectAllPlayers() renvoie les 2 joueurs, tries par id",
                    players.equals(List.of("1:Steve:42", "2:Alice:99")));
        }

        ExerciseChecker.summary();
    }
}
