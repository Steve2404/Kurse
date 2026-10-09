package ch19_final.drills.r02_jdbc.solution;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Des migrations minimales : le script i est la version i + 1 ; schema_version retient ce qui est fait. */
public final class Schema {

    private Schema() {
    }

    public static List<Integer> migrate(Tx tx, List<String> scripts) {
        Set<Integer> done = done(tx);
        List<Integer> applied = new ArrayList<>();
        for (int i = 0; i < scripts.size(); i++) {
            int version = i + 1;
            if (!done.contains(version)) {
                apply(tx, version, scripts.get(i));
                applied.add(version);
            }
        }
        return applied;
    }

    private static Set<Integer> done(Tx tx) {
        tx.write(c -> {
            try (Statement st = c.createStatement()) {
                st.execute("CREATE TABLE IF NOT EXISTS schema_version (version INT PRIMARY KEY)");
            }
            return null;
        });
        return tx.read(c -> {
            Set<Integer> versions = new HashSet<>();
            try (PreparedStatement ps = c.prepareStatement("SELECT version FROM schema_version"); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    versions.add(rs.getInt(1));
                }
            }
            return versions;
        });
    }

    private static void apply(Tx tx, int version, String script) {
        tx.write(c -> {
            try (Statement st = c.createStatement()) {
                st.execute(script);
            }
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO schema_version VALUES (?)")) {
                ps.setInt(1, version);
                ps.executeUpdate();
            }
            return null;
        });
    }
}
