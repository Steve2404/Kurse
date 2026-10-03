package ch15_jdbc.drills.r03_callable.solution;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * SOLUTION du drill de rappel 3 - CallableStatement : IN, OUT, IN OUT, procedure qui rend un ResultSet, procedure qui modifie.
 * Avec H2, une procedure stockee est une methode Java public static, enregistree par CREATE ALIAS.
 */
public class Recall03 {

    public static int square(int x) {
        return x * x;
    }

    public static String shout(String s) {
        return s.toUpperCase() + "!";
    }

    // Le 1er parametre Connection est fourni par H2 : l'appel SQL ne le passe pas.
    public static ResultSet adults(Connection conn, int minAge) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT name FROM people WHERE age >= ? ORDER BY name");
        ps.setInt(1, minAge);
        return ps.executeQuery();
    }

    public static int birthday(Connection conn, String name) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE people SET age = age + 1 WHERE name = ?")) {
            ps.setString(1, name);
            return ps.executeUpdate();
        }
    }

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:r03")) {
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("CREATE TABLE people (name VARCHAR(10) PRIMARY KEY, age INT NOT NULL)");
                st.executeUpdate("INSERT INTO people VALUES ('Lea', 17), ('Max', 30), ('Ines', 18)");
                String cls = Recall03.class.getName();
                for (String[] alias : new String[][]{{"SQUARE", "square"}, {"SHOUT", "shout"}, {"ADULTS", "adults"}, {"BIRTHDAY", "birthday"}}) {
                    st.execute("CREATE ALIAS " + alias[0] + " FOR \"" + cls + "." + alias[1] + "\"");
                }
                // D01 : une fonction s'utilise aussi dans le SQL.
                List<String> squares = new ArrayList<>();
                try (ResultSet rs = st.executeQuery("SELECT name, SQUARE(age) FROM people ORDER BY name")) {
                    while (rs.next()) {
                        squares.add(rs.getString(1) + "=" + rs.getInt(2));
                    }
                }
                System.out.println("D01 : " + squares);
            }

            // D02 : OUT. Le ? de gauche est le parametre 1 ; on le DECLARE avant execute().
            try (CallableStatement cs = conn.prepareCall("{? = call SQUARE(?)}")) {
                cs.registerOutParameter(1, Types.INTEGER);
                cs.setInt(2, 12);
                cs.execute();
                int first = cs.getInt(1);
                cs.setInt(2, 5);
                cs.execute();
                System.out.println("D02 : " + first + " " + cs.getInt(1));
            }

            // D03 : IN OUT. La meme case : setString pour entrer, registerOutParameter pour sortir.
            try (CallableStatement cs = conn.prepareCall("{call SHOUT(?)}")) {
                cs.setString(1, "salut");
                cs.registerOutParameter(1, Types.VARCHAR);
                cs.execute();
                String once = cs.getString(1);
                cs.setString(1, once);
                cs.execute();
                System.out.println("D03 : " + once + " " + cs.getString(1));
            }

            // D04 : une procedure qui rend un ResultSet : executeQuery().
            try (CallableStatement cs = conn.prepareCall("{call ADULTS(?)}")) {
                cs.setInt(1, 18);
                List<String> names = new ArrayList<>();
                try (ResultSet rs = cs.executeQuery()) {
                    while (rs.next()) {
                        names.add(rs.getString("name"));
                    }
                }
                System.out.println("D04 : " + names);
            }

            // D05 : une procedure qui modifie la base, et rend le nombre de lignes.
            try (CallableStatement cs = conn.prepareCall("{? = call BIRTHDAY(?)}");
                 PreparedStatement ps = conn.prepareStatement("SELECT age FROM people WHERE name = 'Lea'")) {
                cs.registerOutParameter(1, Types.INTEGER);
                cs.setString(2, "Lea");
                cs.execute();
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    System.out.println("D05 : " + cs.getInt(1) + " ligne, Lea a " + rs.getInt(1) + " ans");
                }
            }

            // D06 : le type et la concurrence du curseur, comme pour prepareStatement.
            try (CallableStatement cs = conn.prepareCall("{call ADULTS(?)}", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
                cs.setInt(1, 0);
                try (ResultSet rs = cs.executeQuery()) {
                    System.out.println("D06 : " + (rs.getType() == ResultSet.TYPE_FORWARD_ONLY) + " " + (rs.getConcurrency() == ResultSet.CONCUR_READ_ONLY));
                }
            }

            // D07 : les erreurs d'appel.
            List<String> states = new ArrayList<>();
            try {
                conn.prepareCall("{call NOPE()}");
            } catch (SQLException e) {
                states.add(e.getSQLState());
            }
            try (CallableStatement cs = conn.prepareCall("{? = call SQUARE(?)}")) {
                cs.registerOutParameter(1, Types.INTEGER);
                cs.execute();
            } catch (SQLException e) {
                states.add(e.getSQLState());
            }
            System.out.println("D07 : " + states);
        }
        // D08 : la hierarchie des interfaces.
        System.out.println("D08 : " + PreparedStatement.class.isAssignableFrom(CallableStatement.class) + " " + Statement.class.isAssignableFrom(PreparedStatement.class));
    }
}
