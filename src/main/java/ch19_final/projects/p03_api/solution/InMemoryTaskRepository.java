package ch19_final.projects.p03_api.solution;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Le depot en memoire. Le serveur HTTP traite plusieurs requetes EN MEME TEMPS, sur plusieurs fils :
 * chaque methode est synchronized, sinon deux POST simultanes pourraient recevoir le meme identifiant (chapitre 13).
 */
public final class InMemoryTaskRepository implements TaskRepository {

    private final TreeMap<Long, Task> tasks = new TreeMap<>();
    private long lastId;

    @Override
    public synchronized Task create(NewTask task) {
        lastId++;
        Task created = new Task(lastId, task.title(), task.column(), task.points(), 0);
        tasks.put(lastId, created);
        return created;
    }

    @Override
    public synchronized Optional<Task> find(long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public synchronized List<Task> page(String column, int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("page negative : " + page);
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("taille de page invalide : " + size + " (1 a 100)");
        }
        return tasks.values().stream()
                .filter(t -> column == null || t.column().equals(column))
                .skip((long) page * size)
                .limit(size)
                .toList();
    }

    @Override
    public synchronized Task update(Task task) {
        Task current = tasks.get(task.id());
        if (current == null) {
            throw new NoSuchElementException("tache " + task.id() + " introuvable");
        }
        if (current.version() != task.version()) {
            throw new ConflictException(task.id(), task.version());
        }
        Task saved = new Task(task.id(), task.title(), task.column(), task.points(), task.version() + 1);
        tasks.put(task.id(), saved);
        return saved;
    }

    @Override
    public synchronized boolean delete(long id) {
        return tasks.remove(id) != null;
    }

    @Override
    public synchronized int count() {
        return tasks.size();
    }
}
