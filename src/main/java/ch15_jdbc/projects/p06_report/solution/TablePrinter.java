package ch15_jdbc.projects.p06_report.solution;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Un moteur de rapport GENERIQUE : il ne connait aucune requete a l'avance, ResultSetMetaData lui dit tout.
 */
final class TablePrinter {

    private static final Set<Integer> NUMERIC = Set.of(Types.INTEGER, Types.SMALLINT, Types.BIGINT, Types.DECIMAL, Types.NUMERIC, Types.DOUBLE, Types.REAL);

    private TablePrinter() {
    }

    static List<String> render(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int n = meta.getColumnCount();
        String[] labels = new String[n];
        boolean[] right = new boolean[n];
        int[] widths = new int[n];
        for (int i = 1; i <= n; i++) {
            // getColumnLabel rend l'alias (AS titre) ; getColumnName rendrait la colonne d'origine.
            labels[i - 1] = meta.getColumnLabel(i).toLowerCase();
            right[i - 1] = NUMERIC.contains(meta.getColumnType(i));
            widths[i - 1] = labels[i - 1].length();
        }
        // 1er passage : on lit tout pour connaitre la largeur de chaque colonne.
        List<String[]> rows = new ArrayList<>();
        while (rs.next()) {
            String[] row = new String[n];
            for (int i = 1; i <= n; i++) {
                Object value = rs.getObject(i);
                row[i - 1] = value == null ? "-" : value.toString();
                widths[i - 1] = Math.max(widths[i - 1], row[i - 1].length());
            }
            rows.add(row);
        }
        // 2e passage : la mise en forme.
        List<String> lines = new ArrayList<>();
        lines.add(line(labels, widths, new boolean[n]));
        StringBuilder sep = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sep.append(i == 0 ? "" : "-+-").append("-".repeat(widths[i]));
        }
        lines.add(sep.toString());
        for (String[] row : rows) {
            lines.add(line(row, widths, right));
        }
        lines.add("(" + rows.size() + " lignes)");
        return lines;
    }

    private static String line(String[] cells, int[] widths, boolean[] right) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cells.length; i++) {
            String pad = " ".repeat(widths[i] - cells[i].length());
            sb.append(i == 0 ? "" : " | ").append(right[i] ? pad + cells[i] : cells[i] + pad);
        }
        return sb.toString().stripTrailing();
    }
}
