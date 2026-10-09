package ch19_final.projects.p08_atelier.solution;

import java.util.Objects;

/** Une tache pas encore enregistree : pas encore d'identifiant ni de version, c'est la base qui les donne. */
public record NewTask(String title, String column, int points) {

    public NewTask {
        title = Task.checkedTitle(title);
        points = Task.checkedPoints(points);
        Objects.requireNonNull(column, "column");
    }
}
