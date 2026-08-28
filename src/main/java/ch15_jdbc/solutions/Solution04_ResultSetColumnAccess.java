package ch15_jdbc.solutions;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans jdbc.exercises.Exercise04_ResultSetColumnAccess.
 */
public class Solution04_ResultSetColumnAccess {

    public static String getNameByIndex(ResultSet rs) throws SQLException {
        return rs.getString(2);
    }

    public static String getNameByLabel(ResultSet rs) throws SQLException {
        return rs.getString("name");
    }

    public static Object getScoreAsObject(ResultSet rs) throws SQLException {
        return rs.getObject("score");
    }
}
