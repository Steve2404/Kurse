package ch19_final.projects.p03_api.solution;

import java.util.List;
import java.util.Optional;

/**
 * Le PORT des taches (chapitre 18) : l'API ne connait que cette interface. Ici, une version en memoire ;
 * dans le projet final, le depot JDBC du projet 2 la remplira sans que l'API change.
 */
public interface TaskRepository {

    Task create(NewTask task);

    Optional<Task> find(long id);

    /** Une page triee par id ; column null : toutes. Taille de 1 a 100, sinon IllegalArgumentException. */
    List<Task> page(String column, int page, int size);

    /** La tache avec la version suivante ; ConflictException si la version est perimee, NoSuchElementException si absente. */
    Task update(Task task);

    boolean delete(long id);

    int count();
}
