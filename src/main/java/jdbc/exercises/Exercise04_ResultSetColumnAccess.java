package jdbc.exercises;

import jdbc.ExerciseChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * EXERCICE 4 - Lire un ResultSet : par index, par nom, et getObject() (niveau : moyen)
 * =====================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_JdbcUrlAndDriverManager.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une ligne de ResultSet se lit de 2 facons EQUIVALENTES : par
 * POSITION (getString(2) : "la 2e colonne de la requete SELECT, peu
 * importe son nom") ou par NOM (getString("name") : "la colonne
 * appelee 'name', peu importe sa position") - les 2 rendent la MEME
 * valeur, tant que la requete correspond bien.
 *
 * PIEGE CLASSIQUE DE L'EXAMEN : les index de colonnes commencent a 1,
 * PAS 0 (contrairement a un tableau Java classique). getString(0)
 * lancerait une SQLException.
 *
 * getObject(...) est la methode "passe-partout" : elle rend le TYPE
 * JAVA le plus naturel pour la colonne (un Integer pour une colonne
 * INT, un String pour du texte...), sans avoir a connaitre a l'avance
 * le type exact - pratique quand on ecrit du code generique.
 *
 *
 * ==================================================================
 * TODO 1 : getNameByIndex(rs)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer rs.getString(2) (en supposant que la requete SELECT
 *      demande les colonnes dans l'ordre id, name, score - "name" est
 *      donc la 2e).
 *
 *
 * ==================================================================
 * TODO 2 : getNameByLabel(rs)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer rs.getString("name").
 *
 *
 * ==================================================================
 * TODO 3 : getScoreAsObject(rs)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer rs.getObject("score").
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne, c'est la COMPREHENSION de
 * l'equivalence index/nom, et du role de getObject(), qui compte.
 *
 * Exemple a verifier : sur une ligne (1, "Steve", 42),
 * getNameByIndex() et getNameByLabel() rendent TOUS LES DEUX
 * "Steve". getScoreAsObject() rend un Integer valant 42 (le type
 * Java naturel pour une colonne INT).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - rs.next() DOIT etre appele avant de lire quoi que ce soit -
 *     sans ca, le curseur est "avant la premiere ligne", et toute
 *     lecture leve une SQLException.
 */
public class Exercise04_ResultSetColumnAccess {

    public static String getNameByIndex(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("TODO 1 : implementer getNameByIndex()");
    }

    public static String getNameByLabel(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("TODO 2 : implementer getNameByLabel()");
    }

    public static Object getScoreAsObject(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("TODO 3 : implementer getScoreAsObject()");
    }

    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:exercice04", "sa", "")) {
            try (Statement setup = connection.createStatement()) {
                setup.executeUpdate("CREATE TABLE players (id INT PRIMARY KEY, name VARCHAR(50), score INT)");
                setup.executeUpdate("INSERT INTO players VALUES (1, 'Steve', 42)");
            }

            try (PreparedStatement ps = connection.prepareStatement("SELECT id, name, score FROM players");
                 ResultSet rs = ps.executeQuery()) {
                rs.next();

                ExerciseChecker.check("getNameByIndex() lit bien 'Steve' par position",
                        getNameByIndex(rs).equals("Steve"));
                ExerciseChecker.check("getNameByLabel() lit bien 'Steve' par nom",
                        getNameByLabel(rs).equals("Steve"));

                Object score = getScoreAsObject(rs);
                ExerciseChecker.check("getScoreAsObject() rend un Integer valant 42",
                        score instanceof Integer && score.equals(42));
            }
        }

        ExerciseChecker.summary();
    }
}
