package ch15_jdbc.projects.p04_import.solution;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * L'importateur : valide et dedoublonne en Java, puis envoie les INSERT par LOTS (addBatch / executeBatch).
 */
final class Importer {

    private final Connection conn;
    private final int chunk;
    private final List<String> problems = new ArrayList<>();
    private final List<String> duplicates = new ArrayList<>();
    private final List<Integer> batchSizes = new ArrayList<>();
    private final List<Integer> keys = new ArrayList<>();
    private final List<String> rejected = new ArrayList<>();

    Importer(Connection conn, int chunk) throws SQLException {
        this.conn = conn;
        this.chunk = chunk;
        conn.setAutoCommit(false);
    }

    // Une ligne -> un client, ou un probleme note (numero de ligne a partir de 1).
    private Customer parse(int number, String line) {
        String[] f = line.split(";");
        if (f.length != 3) {
            problems.add("ligne " + number + " : " + f.length + " champs");
            return null;
        }
        String email = f[0].trim().toLowerCase();
        String name = f[1].trim();
        if (!email.contains("@")) {
            problems.add("ligne " + number + " : email sans @");
            return null;
        }
        if (name.isEmpty()) {
            problems.add("ligne " + number + " : nom vide");
            return null;
        }
        return new Customer(email, name, f[2].trim());
    }

    // Le dedoublonnage garde la 1re occurrence (LinkedHashMap : l'ordre du fichier est conserve).
    List<Customer> clean(String[] lines) {
        problems.clear();
        duplicates.clear();
        Map<String, Customer> unique = new LinkedHashMap<>();
        for (int i = 0; i < lines.length; i++) {
            Customer c = parse(i + 1, lines[i]);
            if (c != null && unique.putIfAbsent(c.email(), c) != null) {
                duplicates.add(lines[i].split(";")[0].trim());
            }
        }
        return new ArrayList<>(unique.values());
    }

    // Les cles creees par le dernier executeBatch (une par ligne reussie).
    private void collectKeys(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            while (rs.next()) {
                keys.add(rs.getInt(1));
            }
        }
    }

    // Envoie un lot. Si des lignes echouent, H2 execute quand meme les autres : getUpdateCounts() dit lesquelles
    // (Statement.EXECUTE_FAILED). En mode strict, on annule alors TOUT le fichier.
    private boolean flush(PreparedStatement ps, List<Customer> pending, boolean strict) throws SQLException {
        if (pending.isEmpty()) {
            return true;
        }
        try {
            int[] counts = ps.executeBatch();
            batchSizes.add(counts.length);
            collectKeys(ps);
            return true;
        } catch (BatchUpdateException e) {
            int[] counts = e.getUpdateCounts();
            for (int i = 0; i < counts.length; i++) {
                if (counts[i] == Statement.EXECUTE_FAILED) {
                    rejected.add(pending.get(i).email() + " (" + e.getSQLState() + ")");
                }
            }
            batchSizes.add(counts.length);
            if (strict) {
                conn.rollback();
                return false;
            }
            // Les lignes reussies du lot ont quand meme recu une cle.
            collectKeys(ps);
            return true;
        } finally {
            pending.clear();
        }
    }

    /** Rend le compte rendu de l'import ; en mode strict, un seul rejet annule tout. */
    String importFile(String file, String[] lines, boolean strict) throws SQLException {
        batchSizes.clear();
        keys.clear();
        rejected.clear();
        List<Customer> customers = clean(lines);
        List<Customer> pending = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO customers (email, name, city) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            for (Customer c : customers) {
                ps.setString(1, c.email());
                ps.setString(2, c.name());
                ps.setString(3, c.city());
                ps.addBatch();
                pending.add(c);
                if (pending.size() == chunk && !flush(ps, pending, strict)) {
                    return file + " : ANNULE (strict), rejets " + rejected;
                }
            }
            if (!flush(ps, pending, strict)) {
                return file + " : ANNULE (strict), rejets " + rejected;
            }
        }
        int inserted = customers.size() - rejected.size();
        try (PreparedStatement log = conn.prepareStatement("INSERT INTO import_log (file, inserted, rejected) VALUES (?, ?, ?)")) {
            log.setString(1, file);
            log.setInt(2, inserted);
            log.setInt(3, rejected.size());
            log.executeUpdate();
        }
        conn.commit();
        return file + " : " + inserted + " inseres, lots " + batchSizes + ", cles " + keys + ", rejets " + rejected;
    }

    List<String> problems() {
        return problems;
    }

    List<String> duplicates() {
        return duplicates;
    }
}
