package ch15_jdbc.projects.p06_report.solution;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * L'explorateur de schema : DatabaseMetaData decrit les tables, leurs colonnes, leurs cles. Avec cela, on
 * calcule l'ordre de creation des tables et on ecrit une copie (un "dump") qui se recharge ailleurs.
 */
final class Schema {

    private final Connection conn;
    private final DatabaseMetaData meta;

    Schema(Connection conn) throws SQLException {
        this.conn = conn;
        this.meta = conn.getMetaData();
    }

    // Les noms sont en MAJUSCULES chez H2 (sans guillemets, SQL range les identifiants en majuscules).
    List<String> tables() throws SQLException {
        List<String> names = new ArrayList<>();
        try (ResultSet rs = meta.getTables(null, "PUBLIC", "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                names.add(rs.getString("TABLE_NAME"));
            }
        }
        names.sort(null);
        return names;
    }

    // Une colonne : son nom, son type SQL (avec la taille pour VARCHAR et DECIMAL) et NOT NULL.
    List<String> columns(String table) throws SQLException {
        List<String> cols = new ArrayList<>();
        try (ResultSet rs = meta.getColumns(null, "PUBLIC", table, "%")) {
            while (rs.next()) {
                String type = rs.getString("TYPE_NAME");
                int dataType = rs.getInt("DATA_TYPE");
                if (dataType == Types.VARCHAR) {
                    type += "(" + rs.getInt("COLUMN_SIZE") + ")";
                } else if (dataType == Types.DECIMAL || dataType == Types.NUMERIC) {
                    type += "(" + rs.getInt("COLUMN_SIZE") + "," + rs.getInt("DECIMAL_DIGITS") + ")";
                }
                boolean notNull = rs.getInt("NULLABLE") == DatabaseMetaData.columnNoNulls;
                cols.add(rs.getString("COLUMN_NAME") + " " + type + (notNull ? " NOT NULL" : ""));
            }
        }
        return cols;
    }

    String primaryKey(String table) throws SQLException {
        try (ResultSet rs = meta.getPrimaryKeys(null, "PUBLIC", table)) {
            return rs.next() ? rs.getString("COLUMN_NAME") : null;
        }
    }

    // Les cles etrangeres de cette table : colonne -> table referencee.
    Map<String, String> foreignKeys(String table) throws SQLException {
        Map<String, String> fks = new TreeMap<>();
        try (ResultSet rs = meta.getImportedKeys(null, "PUBLIC", table)) {
            while (rs.next()) {
                fks.put(rs.getString("FKCOLUMN_NAME"), rs.getString("PKTABLE_NAME"));
            }
        }
        return fks;
    }

    // Le tri topologique (Kahn) : une table vient APRES toutes celles qu'elle reference.
    // A egalite, l'ordre alphabetique (TreeSet) rend le resultat unique.
    List<String> creationOrder() throws SQLException {
        Map<String, Set<String>> needs = new TreeMap<>();
        for (String t : tables()) {
            needs.put(t, new TreeSet<>(foreignKeys(t).values()));
            needs.get(t).remove(t);
        }
        List<String> order = new ArrayList<>();
        TreeSet<String> ready = new TreeSet<>();
        needs.forEach((t, deps) -> {
            if (deps.isEmpty()) {
                ready.add(t);
            }
        });
        while (!ready.isEmpty()) {
            String t = ready.pollFirst();
            order.add(t);
            for (Map.Entry<String, Set<String>> e : needs.entrySet()) {
                if (e.getValue().remove(t) && e.getValue().isEmpty()) {
                    ready.add(e.getKey());
                }
            }
        }
        if (order.size() != needs.size()) {
            throw new IllegalStateException("cycle de cles etrangeres");
        }
        return order;
    }

    // Une valeur en SQL : NULL, un nombre tel quel, sinon un texte entre apostrophes (doublees a l'interieur).
    private static String literal(Object value, int sqlType) {
        if (value == null) {
            return "NULL";
        }
        if (sqlType == Types.DATE) {
            return "DATE '" + value + "'";
        }
        if (value instanceof Number) {
            return value.toString();
        }
        return "'" + value.toString().replace("'", "''") + "'";
    }

    // Le dump : un CREATE TABLE puis des INSERT par table, dans l'ordre de creation.
    List<String> dump() throws SQLException {
        List<String> script = new ArrayList<>();
        for (String table : creationOrder()) {
            List<String> parts = new ArrayList<>(columns(table));
            parts.add("PRIMARY KEY (" + primaryKey(table) + ")");
            foreignKeys(table).forEach((col, ref) -> parts.add("FOREIGN KEY (" + col + ") REFERENCES " + ref));
            script.add("CREATE TABLE " + table + " (" + String.join(", ", parts) + ")");
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM " + table + " ORDER BY 1")) {
                ResultSetMetaData rsm = rs.getMetaData();
                while (rs.next()) {
                    List<String> values = new ArrayList<>();
                    for (int i = 1; i <= rsm.getColumnCount(); i++) {
                        values.add(literal(rs.getObject(i), rsm.getColumnType(i)));
                    }
                    script.add("INSERT INTO " + table + " VALUES (" + String.join(", ", values) + ")");
                }
            }
        }
        return script;
    }
}
