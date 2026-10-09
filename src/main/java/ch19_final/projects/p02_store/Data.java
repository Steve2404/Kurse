package ch19_final.projects.p02_store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Les donnees FOURNIES du projet 2 (ne pas modifier).
 *
 * LegacyTaskDao est l'ancien acces aux taches de l'atelier : il marche dans la demo... et il contient
 * a peu pres toutes les erreurs qu'on trouve dans du vrai code JDBC. Lance main pour voir la pire.
 */
public final class Data {

    private Data() {
    }

    /** Les taches de depart : titre, colonne, points. */
    public static final List<String[]> SEED = List.of(
            new String[]{"Changer la chaine", "Fini", "2"},
            new String[]{"Regler les freins", "En cours", "3"},
            new String[]{"Commander 12 chambres a air", "A faire", "1"},
            new String[]{"Facture Dupont", "A faire", "1"},
            new String[]{"Remise 50% sur les antivols", "A faire", "2"},
            new String[]{"Remise 500 euros velo cargo", "En cours", "5"},
            new String[]{"Graisser le pedalier", "Fini", "1"});

    /** L'ancien code : ne l'imite pas. */
    public static final class LegacyTaskDao {

        static Connection connection; // une connexion statique, partagee par tout le programme, jamais fermee

        public static void open(String url) throws SQLException {
            connection = DriverManager.getConnection(url);
            connection.createStatement().execute("CREATE TABLE task (id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                    + "title VARCHAR(200) NOT NULL, col VARCHAR(20) NOT NULL, points INT NOT NULL)");
        }

        public static void add(String title, String column, int points) {
            try {
                connection.createStatement().execute("INSERT INTO task(title, col, points) VALUES ('"
                        + title + "', '" + column + "', " + points + ")");
            } catch (SQLException e) {
                e.printStackTrace(); // l'erreur est affichee... et l'appelant croit que tout s'est bien passe
            }
        }

        public static List<String> search(String text) {
            List<String> titles = new ArrayList<>();
            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("SELECT title FROM task WHERE title LIKE '%" + text + "%'");
                while (rs.next()) {
                    titles.add(rs.getString(1));
                }
            } catch (SQLException e) {
                System.out.println("erreur : " + e.getMessage());
            }
            return titles;
        }

        public static int count() throws SQLException {
            ResultSet rs = connection.createStatement().executeQuery("SELECT COUNT(*) FROM task");
            rs.next();
            return rs.getInt(1);
        }
    }

    public static void main(String[] args) throws SQLException {
        LegacyTaskDao.open("jdbc:h2:mem:legacy");
        for (String[] row : SEED) {
            LegacyTaskDao.add(row[0], row[1], Integer.parseInt(row[2]));
        }
        System.out.println("taches : " + LegacyTaskDao.count());
        System.out.println("recherche 'Remise' : " + LegacyTaskDao.search("Remise"));
        System.out.println("recherche '50%' : " + LegacyTaskDao.search("50%"));
        System.out.println("recherche ''' : " + LegacyTaskDao.search("'"));
        System.out.println("recherche \"x' OR 1=1 --\" : " + LegacyTaskDao.search("x' OR 1=1 --"));
        System.out.println("recherche \"x'; DROP TABLE task; --\" : " + LegacyTaskDao.search("x'; DROP TABLE task; --"));
        try {
            System.out.println("taches : " + LegacyTaskDao.count());
        } catch (SQLException e) {
            System.out.println("taches : impossible de compter (SQLState " + e.getSQLState() + ")");
        }
    }
}
