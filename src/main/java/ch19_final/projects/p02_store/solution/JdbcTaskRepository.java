package ch19_final.projects.p02_store.solution;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Le depot JDBC des taches. Trois regles partout :
 *   - seulement des PreparedStatement : une valeur ne devient JAMAIS du texte SQL (pas d'injection) ;
 *   - chaque ressource dans un try-with-resources ;
 *   - les ecritures en plusieurs etapes dans UNE transaction (Transactions.write).
 */
public final class JdbcTaskRepository {

    static final int MAX_PAGE_SIZE = 100;
    private static final String COLUMNS = "id, title, col, points, version";

    private final Transactions tx;
    private final Clock clock;

    public JdbcTaskRepository(Transactions tx, Clock clock) {
        this.tx = tx;
        this.clock = clock;
    }

    public Task create(NewTask task) {
        return tx.write(c -> {
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO task (title, col, points) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, task.title());
                ps.setString(2, task.column());
                ps.setInt(3, task.points());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    return new Task(keys.getLong(1), task.title(), task.column(), task.points(), 0);
                }
            }
        });
    }

    public Optional<Task> find(long id) {
        return tx.read(c -> find(c, id));
    }

    /** Une page, triee par id. column null : toutes les colonnes. */
    public List<Task> page(String column, int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("page negative : " + page);
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("taille de page invalide : " + size + " (1 a " + MAX_PAGE_SIZE + ")");
        }
        return tx.read(c -> list(c, "SELECT " + COLUMNS + " FROM task WHERE ? IS NULL OR col = ? ORDER BY id LIMIT ? OFFSET ?",
                column, column, size, page * size));
    }

    /** Les taches dont le titre contient text, sans tenir compte de la casse. % et _ sont cherches tels quels. */
    public List<Task> search(String text) {
        String pattern = "%" + escapeLike(text.toLowerCase()) + "%";
        return tx.read(c -> list(c, "SELECT " + COLUMNS + " FROM task WHERE LOWER(title) LIKE ? ESCAPE '!' ORDER BY id", pattern));
    }

    // Dans LIKE, % et _ sont des jokers : on les precede du caractere d'echappement choisi (!), et lui aussi.
    static String escapeLike(String text) {
        StringBuilder sb = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (ch == '!' || ch == '%' || ch == '_') {
                sb.append('!');
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    /** Enregistre le titre, la colonne et les points de task, si sa version est toujours celle de la base. */
    public Task update(Task task) {
        return tx.write(c -> updateChecked(c, task));
    }

    /** Deplace la tache ET note le deplacement dans l'historique : les deux, ou aucun. */
    public Task move(long id, String toColumn, int expectedVersion) {
        return tx.write(c -> {
            Task current = find(c, id).orElseThrow(() -> notFound(id));
            Task moved = updateChecked(c, new Task(id, current.title(), toColumn, current.points(), expectedVersion));
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO task_event (task_id, from_col, to_col, at) VALUES (?, ?, ?, ?)")) {
                ps.setLong(1, id);
                ps.setString(2, current.column());
                ps.setString(3, toColumn);
                ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now(clock)));
                ps.executeUpdate();
            }
            return moved;
        });
    }

    /** L'historique : "2026-10-09T10:15 A faire -> En cours", du plus ancien au plus recent. */
    public List<String> history(long id) {
        return tx.read(c -> {
            List<String> lines = new ArrayList<>();
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT at, from_col, to_col FROM task_event WHERE task_id = ? ORDER BY id")) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lines.add(rs.getTimestamp(1).toLocalDateTime() + " " + rs.getString(2) + " -> " + rs.getString(3));
                    }
                }
            }
            return lines;
        });
    }

    /** Efface la tache et son historique (la cle etrangere interdit l'inverse). Faux si elle n'existait pas. */
    public boolean delete(long id) {
        return tx.write(c -> {
            try (PreparedStatement events = c.prepareStatement("DELETE FROM task_event WHERE task_id = ?");
                 PreparedStatement task = c.prepareStatement("DELETE FROM task WHERE id = ?")) {
                events.setLong(1, id);
                events.executeUpdate();
                task.setLong(1, id);
                return task.executeUpdate() == 1;
            }
        });
    }

    /** Le nombre de taches par colonne, colonnes triees. */
    public Map<String, Integer> countByColumn() {
        return tx.read(c -> {
            Map<String, Integer> counts = new TreeMap<>();
            try (PreparedStatement ps = c.prepareStatement("SELECT col, COUNT(*) FROM task GROUP BY col");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    counts.put(rs.getString(1), rs.getInt(2));
                }
            }
            return counts;
        });
    }

    // ------------------------------------------------------------------ aides

    // Le verrou optimiste : WHERE version = ? ne touche aucune ligne si quelqu'un est passe avant nous.
    private Task updateChecked(Connection c, Task task) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "UPDATE task SET title = ?, col = ?, points = ?, version = version + 1 WHERE id = ? AND version = ?")) {
            ps.setString(1, task.title());
            ps.setString(2, task.column());
            ps.setInt(3, task.points());
            ps.setLong(4, task.id());
            ps.setInt(5, task.version());
            if (ps.executeUpdate() == 0) {
                throw find(c, task.id()).isPresent() ? new ConflictException(task.id(), task.version()) : notFound(task.id());
            }
        }
        return new Task(task.id(), task.title(), task.column(), task.points(), task.version() + 1);
    }

    private static NoSuchElementException notFound(long id) {
        return new NoSuchElementException("tache " + id + " introuvable");
    }

    private static Optional<Task> find(Connection c, long id) throws SQLException {
        List<Task> found = list(c, "SELECT " + COLUMNS + " FROM task WHERE id = ?", id);
        return found.stream().findFirst();
    }

    private static List<Task> list(Connection c, String sql, Object... parameters) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }
            List<Task> tasks = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(new Task(rs.getLong("id"), rs.getString("title"), rs.getString("col"),
                            rs.getInt("points"), rs.getInt("version")));
                }
            }
            return tasks;
        }
    }
}
