package ch19_final.projects.p08_atelier.solution;

import java.util.Objects;

/**
 * Une tache telle qu'elle est en base. version sert au verrou optimiste : elle augmente a chaque modification,
 * et une modification faite a partir d'une version perimee est refusee.
 * Les regles (titre, points) sont verifiees ici, dans le domaine, avant d'aller en base : la base garde ses
 * contraintes comme dernier filet, mais un message clair vient du code.
 */
public record Task(long id, String title, String column, int points, int version) {

    public Task {
        title = checkedTitle(title);
        points = checkedPoints(points);
        Objects.requireNonNull(column, "column");
    }

    public Task withColumn(String newColumn) {
        return new Task(id, title, newColumn, points, version);
    }

    public Task withTitle(String newTitle) {
        return new Task(id, newTitle, column, points, version);
    }

    static String checkedTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("titre vide");
        }
        if (title.length() > 200) {
            throw new IllegalArgumentException("titre trop long : " + title.length() + " caracteres (200 au plus)");
        }
        return title.strip();
    }

    static int checkedPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("points negatifs : " + points);
        }
        return points;
    }
}
