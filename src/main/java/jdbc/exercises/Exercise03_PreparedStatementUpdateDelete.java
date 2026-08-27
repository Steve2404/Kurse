package jdbc.exercises;

import jdbc.ExerciseChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * EXERCICE 3 - UPDATE et DELETE avec PreparedStatement (niveau : moyen/difficile)
 * ===============================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_JdbcUrlAndDriverManager.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * UPDATE et DELETE, comme INSERT (Exercise02), passent TOUS LES DEUX
 * par executeUpdate() (pas executeQuery(), reservee a SELECT), et
 * rendent le NOMBRE DE LIGNES REELLEMENT touchees - PAS une exception
 * si aucune ligne ne correspond, juste 0. C'est un point important de
 * l'examen : "aucune ligne modifiee" n'est JAMAIS une erreur en soi.
 *
 *
 * ==================================================================
 * TODO 1 : updateScore(conn, id, newScore)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. prepareStatement("UPDATE players SET score = ? WHERE id = ?").
 *   2. setInt(1, newScore), setInt(2, id) (l'ordre des '?' dans le
 *      SQL DICTE l'ordre des index - le premier '?' rencontre est le
 *      1, peu importe sa signification).
 *   3. Renvoyer executeUpdate().
 *
 *
 * ==================================================================
 * TODO 2 : deletePlayer(conn, id)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. prepareStatement("DELETE FROM players WHERE id = ?").
 *   2. setInt(1, id).
 *   3. Renvoyer executeUpdate().
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient dans un seul try-with-resources.
 *
 * Exemple a verifier : sur 3 joueurs deja en base, updateScore(id=2,
 * 150) touche EXACTEMENT 1 ligne. updateScore(id=999, 50) - un id qui
 * n'existe pas - touche 0 ligne, SANS lancer d'exception.
 * deletePlayer(id=1) touche 1 ligne la premiere fois, puis 0 la
 * deuxieme fois (deja supprime).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - executeUpdate() rend un int (jamais un ResultSet) pour
 *     INSERT/UPDATE/DELETE - c'est executeQuery() qui rend un
 *     ResultSet, uniquement pour SELECT.
 */
public class Exercise03_PreparedStatementUpdateDelete {

    public static int updateScore(Connection conn, int id, int newScore) throws SQLException {
        throw new UnsupportedOperationException("TODO 1 : implementer updateScore()");
    }

    public static int deletePlayer(Connection conn, int id) throws SQLException {
        throw new UnsupportedOperationException("TODO 2 : implementer deletePlayer()");
    }

    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:exercice03", "sa", "")) {
            try (Statement setup = connection.createStatement()) {
                setup.executeUpdate("CREATE TABLE players (id INT PRIMARY KEY, name VARCHAR(50), score INT)");
                setup.executeUpdate("INSERT INTO players VALUES (1, 'Steve', 42)");
                setup.executeUpdate("INSERT INTO players VALUES (2, 'Alice', 99)");
                setup.executeUpdate("INSERT INTO players VALUES (3, 'Bob', 10)");
            }

            int updated = updateScore(connection, 2, 150);
            ExerciseChecker.check("updateScore() sur un id existant touche 1 ligne", updated == 1);

            try (PreparedStatement check = connection.prepareStatement("SELECT score FROM players WHERE id = 2");
                 java.sql.ResultSet rs = check.executeQuery()) {
                rs.next();
                ExerciseChecker.check("le score a bien ete mis a jour a 150", rs.getInt("score") == 150);
            }

            int updatedMissing = updateScore(connection, 999, 50);
            ExerciseChecker.check("updateScore() sur un id INEXISTANT touche 0 ligne, SANS exception",
                    updatedMissing == 0);

            int deletedFirst = deletePlayer(connection, 1);
            ExerciseChecker.check("deletePlayer() la premiere fois touche 1 ligne", deletedFirst == 1);

            int deletedSecond = deletePlayer(connection, 1);
            ExerciseChecker.check("deletePlayer() une deuxieme fois (deja supprime) touche 0 ligne",
                    deletedSecond == 0);
        }

        ExerciseChecker.summary();
    }
}
