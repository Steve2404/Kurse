package ch19_final.projects.p08_atelier.solution;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Le PORT des taches : ce dont le metier a besoin, sans un mot de SQL. Le depot JDBC du projet 2 l'implemente ;
 * demain, un autre depot (une autre base, un service distant) pourra le remplacer sans toucher au metier.
 */
public interface TaskRepository {

    Task create(NewTask task);

    Optional<Task> find(long id);

    List<Task> page(String column, int page, int size);

    Task update(Task task);

    Task move(long id, String toColumn, int expectedVersion);

    List<String> history(long id);

    boolean delete(long id);

    Map<String, Integer> countByColumn();
}
