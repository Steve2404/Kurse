package ch15_jdbc.solutions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans jdbc.exercises.Exercise08_ResourceClosingOrder.
 */
public class Solution08_ResourceClosingOrder {

    public static void closeInOrder(ResultSet rs, Statement st, Connection conn) throws SQLException {
        rs.close();
        st.close();
        conn.close();
    }
}
