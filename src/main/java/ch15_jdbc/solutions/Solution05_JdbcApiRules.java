package ch15_jdbc.solutions;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch15_jdbc.exercises.Exercise05_JdbcApiRules.
 */
public class Solution05_JdbcApiRules {

    public static String returnOf(String method, String sqlKind) {
        // execute rend "y a-t-il un ResultSet ?" ; executeQuery exige un SELECT ; executeUpdate refuse un SELECT
        // et compte les lignes touchees (0 pour un ordre de structure comme CREATE).
        boolean select = sqlKind.equals("SELECT");
        switch (method) {
            case "execute":
                return select ? "true" : "false";
            case "executeQuery":
                return select ? "ResultSet" : "SQLException";
            default:
                if (select) {
                    return "SQLException";
                }
                return sqlKind.equals("CREATE") ? "0" : "rowCount";
        }
    }

    public static String readOutcome(String situation) {
        // Seuls 4 cas rendent une valeur : NULL lu en int donne 0, en objet null ; un INT se lit en String ;
        // un resultat vide fait juste rendre false a next(). Tout le reste est une SQLException.
        switch (situation) {
            case "intOnNull":
                return "0";
            case "objectOnNull":
                return "null";
            case "stringOnInt":
                return "1";
            case "nextOnEmpty":
                return "false";
            default:
                return "SQLException";
        }
    }

    public static Integer nullableInt(ResultSet rs, String column) throws SQLException {
        // wasNull() doit etre appele JUSTE apres le getInt : il parle de la derniere colonne lue.
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }
}
