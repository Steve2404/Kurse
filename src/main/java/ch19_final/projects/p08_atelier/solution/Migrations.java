package ch19_final.projects.p08_atelier.solution;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Les migrations versionnees (l'idee de Flyway et Liquibase) : la base retient dans schema_version les
 * migrations deja faites, et chaque demarrage applique seulement les nouvelles, dans l'ordre.
 * Une migration appliquee ne se modifie JAMAIS : on en ajoute une nouvelle.
 */
public final class Migrations {

    private Migrations() {
    }

    /** Applique les migrations manquantes et rend leurs numeros (vide si la base est a jour). */
    public static List<Integer> apply(Transactions tx, List<Migration> migrations) {
        checkOrder(migrations);
        tx.write(Migrations::createHistoryTable);
        Map<Integer, String> applied = tx.read(Migrations::applied);
        checkUnchanged(migrations, applied);
        List<Integer> done = new ArrayList<>();
        for (Migration m : migrations) {
            if (!applied.containsKey(m.version())) {
                tx.write(connection -> run(connection, m));
                done.add(m.version());
            }
        }
        return done;
    }

    private static void checkOrder(List<Migration> migrations) {
        for (int i = 1; i < migrations.size(); i++) {
            int previous = migrations.get(i - 1).version();
            if (migrations.get(i).version() <= previous) {
                throw new IllegalArgumentException("versions non croissantes : " + migrations.get(i).version()
                        + " apres " + previous);
            }
        }
    }

    // On verifie tout AVANT de changer quoi que ce soit.
    private static void checkUnchanged(List<Migration> migrations, Map<Integer, String> applied) {
        for (Migration m : migrations) {
            String known = applied.get(m.version());
            if (known != null && !known.equals(m.description())) {
                throw new IllegalStateException("migration " + m.version() + " modifiee apres application : \""
                        + known + "\" devient \"" + m.description() + "\"");
            }
        }
    }

    private static Void createHistoryTable(Connection connection) throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS schema_version (version INT PRIMARY KEY, description VARCHAR(200) NOT NULL)");
        }
        return null;
    }

    private static Map<Integer, String> applied(Connection connection) throws SQLException {
        Map<Integer, String> applied = new TreeMap<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT version, description FROM schema_version");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                applied.put(rs.getInt(1), rs.getString(2));
            }
        }
        return applied;
    }

    // Les instructions de la migration ET sa ligne dans schema_version, dans la meme transaction.
    // Attention : avec H2 (et MySQL), une instruction DDL comme CREATE TABLE valide la transaction toute seule.
    private static Void run(Connection connection, Migration m) throws SQLException {
        try (Statement st = connection.createStatement()) {
            for (String sql : m.statements()) {
                st.execute(sql);
            }
        }
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO schema_version (version, description) VALUES (?, ?)")) {
            ps.setInt(1, m.version());
            ps.setString(2, m.description());
            ps.executeUpdate();
        }
        return null;
    }
}
