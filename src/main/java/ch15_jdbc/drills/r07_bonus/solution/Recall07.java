package ch15_jdbc.drills.r07_bonus.solution;

import java.lang.module.ModuleDescriptor;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

/**
 * SOLUTION du drill de rappel 7 (bonus) - pilotes de plusieurs fournisseurs, Properties, curseurs defilants et modifiables,
 * niveaux d'isolation, limites d'un Statement. Avec l'argument "docker", le meme CRUD tourne sur PostgreSQL et MySQL.
 */
public class Recall07 {

    static final String[] VENDOR_URLS = {"jdbc:h2:mem:r07", "jdbc:postgresql://localhost:15432/kurse", "jdbc:mysql://localhost:13306/kurse"};

    public static void main(String[] args) throws SQLException {
        // D01 : DriverManager choisit le pilote d'apres l'URL, sans se connecter.
        List<String> drivers = new ArrayList<>();
        for (String url : VENDOR_URLS) {
            Driver driver = DriverManager.getDriver(url);
            drivers.add(driver.getClass().getName() + " " + driver.acceptsURL(url));
        }
        System.out.println("D01 : " + drivers);
        try {
            DriverManager.getDriver("jdbc:oracle:thin:@localhost:1521:kurse");
        } catch (SQLException e) {
            System.out.println("D01 : oracle " + e.getSQLState());
        }

        // D02 : les pilotes enregistres (trouves seuls dans le classpath, sans Class.forName).
        List<String> registered = new ArrayList<>(DriverManager.drivers().map(d -> d.getClass().getName()).toList());
        registered.sort(null);
        System.out.println("D02 : " + registered);

        // D03 : user et password passes dans des Properties.
        Properties props = new Properties();
        props.setProperty("user", "sa");
        props.setProperty("password", "");
        try (Connection conn = DriverManager.getConnection(VENDOR_URLS[0], props);
             Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE scores (player VARCHAR(10) PRIMARY KEY, points INT NOT NULL)");
            st.executeUpdate("INSERT INTO scores VALUES ('Ana', 40), ('Ben', 25), ('Cleo', 60), ('Dan', 10)");
            System.out.println("D03 : " + conn.getMetaData().getUserName());

            // D04 : un curseur defilant : on saute ou l'on veut.
            try (Statement scroll = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                 ResultSet rs = scroll.executeQuery("SELECT player FROM scores ORDER BY points DESC")) {
                List<String> moves = new ArrayList<>();
                rs.last();
                moves.add("last " + rs.getString(1) + " row " + rs.getRow());
                rs.absolute(2);
                moves.add("absolute(2) " + rs.getString(1));
                rs.relative(-1);
                moves.add("relative(-1) " + rs.getString(1));
                rs.absolute(-2);
                moves.add("absolute(-2) " + rs.getString(1));
                rs.afterLast();
                rs.previous();
                moves.add("previous " + rs.getString(1));
                rs.beforeFirst();
                moves.add("beforeFirst " + rs.isBeforeFirst());
                System.out.println("D04 : " + moves);
            }

            // D05 : un curseur modifiable : on change la base A TRAVERS le ResultSet.
            try (Statement upd = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                 ResultSet rs = upd.executeQuery("SELECT player, points FROM scores ORDER BY player")) {
                rs.next();
                rs.updateInt("points", 45);
                rs.updateRow();
                rs.moveToInsertRow();
                rs.updateString("player", "Eve");
                rs.updateInt("points", 33);
                rs.insertRow();
                rs.moveToCurrentRow();
                rs.last();
                rs.deleteRow();
            }
            List<String> rows = new ArrayList<>();
            try (ResultSet rs = st.executeQuery("SELECT player, points FROM scores ORDER BY player")) {
                while (rs.next()) {
                    rows.add(rs.getString(1) + "=" + rs.getInt(2));
                }
            }
            System.out.println("D05 : " + rows);

            // D06 : le niveau d'isolation.
            boolean serializable = conn.getMetaData().supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE);
            int before = conn.getTransactionIsolation();
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            System.out.println("D06 : READ_COMMITTED par defaut " + (before == Connection.TRANSACTION_READ_COMMITTED) + ", SERIALIZABLE supporte " + serializable
                    + ", actif " + (conn.getTransactionIsolation() == Connection.TRANSACTION_SERIALIZABLE));

            // D07 : limiter un Statement.
            st.setMaxRows(2);
            st.setQueryTimeout(5);
            int seen = 0;
            try (ResultSet rs = st.executeQuery("SELECT * FROM scores")) {
                while (rs.next()) {
                    seen++;
                }
            }
            System.out.println("D07 : maxRows " + st.getMaxRows() + ", lignes lues " + seen + ", timeout " + st.getQueryTimeout() + " s");

            // D08 : les avertissements (SQLWarning) ne sont pas lances : on les lit.
            System.out.println("D08 : warnings " + (conn.getWarnings() == null) + " " + (st.getWarnings() == null));
        }

        // D09 : JDBC est un MODULE (java.sql). Il CONSOMME le service java.sql.Driver : c'est ainsi que
        // DriverManager trouve les pilotes. Une application modulaire ecrit "requires java.sql;".
        Module sql = Connection.class.getModule();
        ModuleDescriptor descriptor = sql.getDescriptor();
        TreeSet<String> exports = new TreeSet<>(descriptor.exports().stream().map(ModuleDescriptor.Exports::source).toList());
        TreeSet<String> transitive = new TreeSet<>(descriptor.requires().stream()
                .filter(r -> r.modifiers().contains(ModuleDescriptor.Requires.Modifier.TRANSITIVE)).map(ModuleDescriptor.Requires::name).toList());
        System.out.println("D09 : " + sql.getName() + " uses " + descriptor.uses() + " exports " + exports + " requires transitive " + transitive);
        // Lances depuis le classpath, le pilote H2 et ta classe sont dans le module SANS NOM.
        System.out.println("D09 : pilote H2 module nomme " + DriverManager.getDriver(VENDOR_URLS[0]).getClass().getModule().isNamed()
                + ", Recall07 module nomme " + Recall07.class.getModule().isNamed());

        // A la main : docker compose up -d dans ch15_jdbc-lab, puis l'argument "docker".
        if (args.length > 0 && args[0].equals("docker")) {
            for (int i = 1; i < VENDOR_URLS.length; i++) {
                crud(VENDOR_URLS[i]);
            }
        }
    }

    // Le MEME code JDBC sur une autre base : seule l'URL change (et parfois un detail de SQL).
    static void crud(String url) {
        try (Connection conn = DriverManager.getConnection(url, "kurse", "kurse");
             Statement st = conn.createStatement()) {
            st.executeUpdate("DROP TABLE IF EXISTS r07_scores");
            st.executeUpdate("CREATE TABLE r07_scores (player VARCHAR(10) PRIMARY KEY, points INT NOT NULL)");
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO r07_scores VALUES (?, ?)")) {
                ps.setString(1, "Ana");
                ps.setInt(2, 40);
                ps.executeUpdate();
            }
            try (ResultSet rs = st.executeQuery("SELECT player, points FROM r07_scores")) {
                rs.next();
                System.out.println("docker : " + conn.getMetaData().getDatabaseProductName() + " " + rs.getString(1) + "=" + rs.getInt(2));
            }
            try {
                st.executeUpdate("INSERT INTO r07_scores VALUES ('Ana', 1)");
            } catch (SQLException e) {
                System.out.println("docker : doublon " + e.getSQLState() + " code " + e.getErrorCode());
            }
        } catch (SQLException e) {
            System.out.println("docker : " + url + " injoignable (" + e.getSQLState() + ") : le conteneur tourne-t-il ?");
        }
    }
}
