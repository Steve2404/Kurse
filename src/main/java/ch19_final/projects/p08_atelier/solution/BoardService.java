package ch19_final.projects.p08_atelier.solution;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Le service de l'atelier : il applique les regles du Board, puis demande au depot de faire le travail.
 * Il ne connait que le PORT TaskRepository : on le teste avec la vraie base H2, ou avec n'importe quel autre depot.
 */
public final class BoardService {

    private final TaskRepository tasks;
    private final Board board;

    public BoardService(TaskRepository tasks, Board board) {
        this.tasks = tasks;
        this.board = board;
    }

    public Task create(NewTask task) {
        board.checkNew(task);
        return tasks.create(task);
    }

    public Task get(long id) {
        return tasks.find(id).orElseThrow(() -> notFound(id));
    }

    public List<Task> page(String column, int page, int size) {
        return tasks.page(column, page, size);
    }

    /** Le titre et les points seulement : la colonne ne change que par move, qui applique les regles. */
    public Task update(Task task) {
        Task current = get(task.id());
        if (!current.column().equals(task.column())) {
            throw new IllegalArgumentException("la colonne se change avec /move, pas avec PUT");
        }
        return tasks.update(task);
    }

    /**
     * Verifier la limite PUIS deplacer : entre les deux, un autre fil pourrait deplacer une tache, et la limite
     * serait depassee. synchronized fait des deux un seul geste (dans CE programme ; avec plusieurs serveurs,
     * il faudrait un verrou dans la base).
     */
    public synchronized Task move(long id, String to, int expectedVersion) {
        board.checkMove(get(id), to, tasks.countByColumn());
        return tasks.move(id, to, expectedVersion);
    }

    public List<String> history(long id) {
        get(id);
        return tasks.history(id);
    }

    public void delete(long id) {
        if (!tasks.delete(id)) {
            throw notFound(id);
        }
    }

    /** Toutes les colonnes, dans l'ordre du tableau, meme vides. */
    public Map<String, Integer> board() {
        Map<String, Integer> counts = tasks.countByColumn();
        Map<String, Integer> result = new LinkedHashMap<>();
        Board.COLUMNS.forEach(column -> result.put(column, counts.getOrDefault(column, 0)));
        return result;
    }

    private static NoSuchElementException notFound(long id) {
        return new NoSuchElementException("tache " + id + " introuvable");
    }
}
