package ch15_jdbc.drills.r02_prepared.solution;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * SOLUTION du drill de rappel 2 - PreparedStatement : parametres, reutilisation, null, dates, pieges des ?.
 */
public class Recall02 {

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:r02");
             Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE films (id INT PRIMARY KEY, title VARCHAR(30) NOT NULL, rating DOUBLE, seen BOOLEAN NOT NULL, released DATE)");

            // D01 : UN PreparedStatement reutilise ; les ? se numerotent a partir de 1.
            String[][] films = {{"1", "Alien", "8.5"}, {"2", "Brazil", ""}, {"3", "Casino", "8.2"}};
            int inserted = 0;
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO films (id, title, rating, seen) VALUES (?, ?, ?, ?)")) {
                for (String[] f : films) {
                    ps.setInt(1, Integer.parseInt(f[0]));
                    ps.setString(2, f[1]);
                    if (f[2].isEmpty()) {
                        ps.setNull(3, Types.DOUBLE);
                    } else {
                        ps.setDouble(3, Double.parseDouble(f[2]));
                    }
                    ps.setBoolean(4, f[0].equals("1"));
                    inserted += ps.executeUpdate();
                }
            }
            System.out.println("D01 : " + inserted);

            // D02 : setObject accepte un LocalDate ; getObject(colonne, LocalDate.class) le relit.
            try (PreparedStatement ps = conn.prepareStatement("UPDATE films SET released = ? WHERE id = ?")) {
                ps.setObject(1, LocalDate.of(1979, 5, 25));
                ps.setInt(2, 1);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT released, seen FROM films WHERE id = ?")) {
                ps.setInt(1, 1);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    LocalDate date = rs.getObject("released", LocalDate.class);
                    System.out.println("D02 : " + date.getYear() + " " + rs.getBoolean("seen") + " " + rs.getDate(1).getClass().getName());
                }
            }

            // D03 : un index hors limites, un ? oublie.
            try (PreparedStatement ps = conn.prepareStatement("SELECT title FROM films WHERE id = ? AND seen = ?")) {
                List<String> states = new ArrayList<>();
                try {
                    ps.setInt(3, 1);
                } catch (SQLException e) {
                    states.add(e.getSQLState());
                }
                ps.setInt(1, 1);
                try {
                    ps.executeQuery();
                } catch (SQLException e) {
                    states.add(e.getSQLState());
                }
                // D04 : les parametres restent poses d'une execution a l'autre... sauf apres clearParameters().
                ps.setBoolean(2, true);
                try (ResultSet rs = ps.executeQuery()) {
                    states.add(rs.next() ? rs.getString(1) : "rien");
                }
                ps.clearParameters();
                try {
                    ps.executeQuery();
                } catch (SQLException e) {
                    states.add(e.getSQLState());
                }
                System.out.println("D03-D04 : " + states);
            }

            // D05 : 0 ligne touchee n'est pas une erreur.
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM films WHERE rating < ?")) {
                ps.setDouble(1, 8.3);
                int first = ps.executeUpdate();
                int second = ps.executeUpdate();
                System.out.println("D05 : " + first + " puis " + second);
            }

            // D06 : un ? entre apostrophes n'est PAS un parametre : c'est un caractere du texte.
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM films WHERE title LIKE '%?%'")) {
                ps.setString(1, "li");
            } catch (SQLException e) {
                System.out.println("D06 : ? entre apostrophes " + e.getSQLState());
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT title FROM films WHERE title LIKE ?")) {
                ps.setString(1, "%li%");
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    System.out.println("D06 : LIKE ? avec \"%li%\" " + rs.getString(1));
                }
            }

            // D07 : la meme requete, deux fois : le 1er ResultSet est ferme par la 2e execution.
            try (PreparedStatement ps = conn.prepareStatement("SELECT title FROM films WHERE id = ?")) {
                ps.setInt(1, 1);
                ResultSet first = ps.executeQuery();
                ps.setInt(1, 2);
                ResultSet second = ps.executeQuery();
                second.next();
                System.out.println("D07 : 1er ferme " + first.isClosed() + ", 2e " + second.getString(1));
            }

            // D08 : la saisie piegee, collee dans le SQL ou passee en parametre.
            String input = "' OR 1=1 --";
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM films WHERE title = '" + input + "'")) {
                rs.next();
                int glued = rs.getInt(1);
                try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM films WHERE title = ?")) {
                    ps.setString(1, input);
                    try (ResultSet safe = ps.executeQuery()) {
                        safe.next();
                        System.out.println("D08 : Statement " + glued + ", PreparedStatement " + safe.getInt(1));
                    }
                }
            }
        }
    }
}
