package ch15_jdbc.drills.r04_transactions.solution;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

/**
 * SOLUTION du drill de rappel 4 - transactions : auto-commit, commit, rollback, visibilite, Savepoint.
 */
public class Recall04 {

    // Le stock vu par une connexion donnee.
    static int stock(Connection c) throws SQLException {
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT qty FROM stock WHERE item = 'pomme'")) {
            rs.next();
            return rs.getInt(1);
        }
    }

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:r04");
             Connection other = DriverManager.getConnection("jdbc:h2:mem:r04");
             Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE stock (item VARCHAR(10) PRIMARY KEY, qty INT NOT NULL CHECK (qty >= 0))");
            st.executeUpdate("INSERT INTO stock VALUES ('pomme', 10)");

            // D01 : en auto-commit, chaque ordre est valide aussitot : l'autre connexion le voit.
            st.executeUpdate("UPDATE stock SET qty = 11");
            System.out.println("D01 : autoCommit " + conn.getAutoCommit() + ", l'autre voit " + stock(other));

            // D02 : sans auto-commit, l'autre connexion ne voit rien avant le commit.
            conn.setAutoCommit(false);
            st.executeUpdate("UPDATE stock SET qty = 20");
            int before = stock(other);
            conn.commit();
            System.out.println("D02 : moi " + stock(conn) + ", l'autre avant commit " + before + ", apres " + stock(other));

            // D03 : rollback annule tout ce qui n'est pas valide.
            st.executeUpdate("UPDATE stock SET qty = 0");
            conn.rollback();
            System.out.println("D03 : apres rollback " + stock(conn));

            // D04 : un Savepoint coupe la transaction en deux.
            st.executeUpdate("UPDATE stock SET qty = qty - 5");
            Savepoint half = conn.setSavepoint("moitie");
            st.executeUpdate("UPDATE stock SET qty = qty - 5");
            conn.rollback(half);
            conn.commit();
            System.out.println("D04 : " + stock(conn) + " (nom " + half.getSavepointName() + ")");

            // D05 : revenir a un point anterieur efface aussi ce qui suit un point posterieur.
            Savepoint a = conn.setSavepoint();
            st.executeUpdate("UPDATE stock SET qty = 1");
            conn.setSavepoint();
            st.executeUpdate("UPDATE stock SET qty = 2");
            conn.rollback(a);
            System.out.println("D05 : " + stock(conn));

            // D06 : un point libere n'existe plus ; un point anonyme n'a pas de nom.
            Savepoint b = conn.setSavepoint("b");
            conn.releaseSavepoint(b);
            String released;
            try {
                conn.rollback(b);
                released = "ok";
            } catch (SQLException e) {
                released = e.getSQLState();
            }
            String unnamed;
            try {
                unnamed = a.getSavepointName();
            } catch (SQLException e) {
                unnamed = "SQLException";
            }
            System.out.println("D06 : rollback(libere) " + released + ", getSavepointName(anonyme) " + unnamed);

            // D07 : un ordre qui echoue est annule SEUL ; ce qui precede reste en attente de commit.
            st.executeUpdate("UPDATE stock SET qty = 7");
            try {
                st.executeUpdate("UPDATE stock SET qty = -1");
            } catch (SQLException e) {
                System.out.println("D07 : " + e.getSQLState() + ", toujours " + stock(conn) + " dans la transaction");
            }

            // D08 : repasser en auto-commit VALIDE la transaction en cours.
            conn.setAutoCommit(true);
            System.out.println("D08 : l'autre voit " + stock(other));
        }
    }
}
