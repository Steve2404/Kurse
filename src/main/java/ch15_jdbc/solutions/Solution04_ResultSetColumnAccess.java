package ch15_jdbc.solutions;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch15_jdbc.exercises.Exercise04_ResultSetColumnAccess.
 */
public class Solution04_ResultSetColumnAccess {

    public static String getNameByIndex(ResultSet rs) throws SQLException {
        // Les colonnes commencent a 1 (pas 0) dans l'ordre du SELECT.
        return rs.getString(2);
    }

    public static String getNameByLabel(ResultSet rs) throws SQLException {
        // Par nom : insensible a l'ordre des colonnes (et a la casse).
        return rs.getString("name");
    }

    public static Object getScoreAsObject(ResultSet rs) throws SQLException {
        // getObject rend le type Java naturel (Integer ici), ou null pour un NULL SQL.
        return rs.getObject("score");
    }
}
