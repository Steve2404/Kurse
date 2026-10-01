package ch15_jdbc.solutions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch15_jdbc.exercises.Exercise10_ResourceClosingOrder.
 */
public class Solution10_ResourceClosingOrder {

    public static void closeInOrder(ResultSet rs, Statement st, Connection conn) throws SQLException {
        // L'ordre inverse de l'ouverture : ResultSet, Statement, Connection (fermer le parent fermerait aussi les enfants).
        rs.close();
        st.close();
        conn.close();
    }
}
