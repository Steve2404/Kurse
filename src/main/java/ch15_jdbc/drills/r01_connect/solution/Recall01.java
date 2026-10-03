package ch15_jdbc.drills.r01_connect.solution;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * SOLUTION du drill de rappel 1 - connexion, Statement, les trois execute, ResultSet, SQLState, fermeture.
 */
public class Recall01 {

    public static void main(String[] args) throws SQLException {
        Connection kept;
        Statement st;
        ResultSet rs;
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:r01", "sa", "")) {
            kept = conn;
            // D01 : l'URL relue par la base, l'auto-commit par defaut.
            System.out.println("D01 : " + conn.getMetaData().getURL() + " autoCommit " + conn.getAutoCommit() + " closed " + conn.isClosed());

            // D02 : execute rend un boolean (ResultSet ou non), executeUpdate un nombre de lignes.
            st = conn.createStatement();
            boolean ddl = st.execute("CREATE TABLE pets (id INT PRIMARY KEY, name VARCHAR(20) NOT NULL, age INT)");
            int rows = st.executeUpdate("INSERT INTO pets VALUES (1, 'Rex', 7), (2, 'Tom', 3), (3, 'Kiki', NULL)");
            System.out.println("D02 : " + ddl + " " + rows);

            // D03 : le curseur part AVANT la 1re ligne ; getInt d'un NULL rend 0, wasNull le dit.
            List<String> pets = new ArrayList<>();
            rs = st.executeQuery("SELECT name, age FROM pets ORDER BY id");
            while (rs.next()) {
                int age = rs.getInt("age");
                // wasNull parle de la DERNIERE colonne lue : a appeler tout de suite, avant un autre get.
                boolean unknown = rs.wasNull();
                pets.add(rs.getString(1) + " " + (unknown ? "?" : age));
            }
            System.out.println("D03 : " + pets);

            // D04 : getObject rend le type Java de la colonne (null pour NULL).
            rs = st.executeQuery("SELECT * FROM pets WHERE id = 3");
            rs.next();
            System.out.println("D04 : " + rs.getObject("name").getClass().getSimpleName() + " " + rs.getObject(1).getClass().getSimpleName() + " " + rs.getObject("age"));

            // D05 : execute, puis getResultSet ou getUpdateCount selon le cas (-1 = pas un nombre de lignes).
            boolean query = st.execute("SELECT COUNT(*) FROM pets");
            int countAfterQuery = st.getUpdateCount();
            rs = st.getResultSet();
            rs.next();
            int count = rs.getInt(1);
            boolean update = st.execute("UPDATE pets SET age = age + 1 WHERE age IS NOT NULL");
            System.out.println("D05 : " + query + " " + countAfterQuery + " " + count + " / " + update + " " + st.getUpdateCount());

            // D06 : les erreurs les plus classiques, et leur SQLState.
            List<String> states = new ArrayList<>();
            rs = st.executeQuery("SELECT name FROM pets");
            try {
                rs.getString(1);
            } catch (SQLException e) {
                states.add(e.getSQLState());
            }
            rs.next();
            try {
                rs.getString(2);
            } catch (SQLException e) {
                states.add(e.getSQLState());
            }
            try {
                st.executeQuery("SELECT * FROM cats");
            } catch (SQLException e) {
                states.add(e.getSQLState());
            }
            try {
                st.executeUpdate("INSERT INTO pets VALUES (1, 'Rex', 1)");
            } catch (SQLException e) {
                states.add(e.getSQLState());
            }
            System.out.println("D06 : " + states);

            // D07 : une URL qu'aucun pilote n'accepte.
            try {
                DriverManager.getConnection("jdbc:nope:r01");
            } catch (SQLException e) {
                System.out.println("D07 : " + e.getSQLState());
            }
        }
        // D08 : fermer la connexion ferme tout ce qui en depend.
        System.out.println("D08 : connexion fermee " + kept.isClosed() + ", ResultSet ferme " + rs.isClosed());
        try {
            st.executeQuery("SELECT 1");
        } catch (SQLException e) {
            System.out.println("D08 : Statement apres fermeture " + e.getSQLState());
        }
    }
}
