package ch15_jdbc.solutions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 15. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch15_jdbc.exercises.Exercise15_ReportBuilder.
 */
public class Solution15_ReportBuilder {

    public static List<List<String>> rows(Connection conn, String sql, Object... params) throws SQLException {
        // setObject laisse le pilote choisir le type SQL ; les metadonnees donnent colonnes et titres sans les connaitre a l'avance.
        List<List<String>> table = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int n = meta.getColumnCount();
                List<String> header = new ArrayList<>();
                for (int c = 1; c <= n; c++) {
                    header.add(meta.getColumnLabel(c));
                }
                table.add(header);
                while (rs.next()) {
                    List<String> row = new ArrayList<>();
                    for (int c = 1; c <= n; c++) {
                        String value = rs.getString(c);
                        row.add(value == null ? "NULL" : value);
                    }
                    table.add(row);
                }
            }
        }
        return table;
    }

    public static String render(Connection conn, String sql, Object... params) throws SQLException {
        // On mesure d'abord (largeur max par colonne), puis on dessine ; stripTrailing enleve le remplissage de la derniere colonne.
        List<List<String>> table = rows(conn, sql, params);
        int columns = table.get(0).size();
        int[] width = new int[columns];
        for (List<String> row : table) {
            for (int c = 0; c < columns; c++) {
                width[c] = Math.max(width[c], row.get(c).length());
            }
        }
        List<String> lines = new ArrayList<>();
        for (int r = 0; r < table.size(); r++) {
            List<String> cells = new ArrayList<>();
            for (int c = 0; c < columns; c++) {
                cells.add(String.format("%-" + width[c] + "s", table.get(r).get(c)));
            }
            lines.add(String.join(" | ", cells).stripTrailing());
            if (r == 0) {
                List<String> dashes = new ArrayList<>();
                for (int w : width) {
                    dashes.add("-".repeat(w));
                }
                lines.add(String.join("-+-", dashes));
            }
        }
        return String.join("\n", lines);
    }

    public static List<String> columnTypes(Connection conn, String sql) throws SQLException {
        // getColumnTypeName rend le nom du type selon le FOURNISSEUR (H2 : CHARACTER VARYING, INTEGER).
        List<String> types = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            ResultSetMetaData meta = rs.getMetaData();
            for (int c = 1; c <= meta.getColumnCount(); c++) {
                types.add(meta.getColumnLabel(c) + ":" + meta.getColumnTypeName(c));
            }
        }
        return types;
    }
}
