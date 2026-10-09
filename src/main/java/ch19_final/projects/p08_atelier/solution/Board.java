package ch19_final.projects.p08_atelier.solution;

import java.util.List;
import java.util.Map;

/**
 * Les regles du tableau Kanban : le COEUR du metier, sans base, sans HTTP, sans horloge. Il se teste en
 * quelques microsecondes, et c'est lui qui dit ce qui est permis :
 *   - une nouvelle tache commence dans "A faire" ;
 *   - une tache avance ou recule d'UNE colonne a la fois ;
 *   - "En cours" a une limite (WIP, "work in progress") : on finit avant de commencer autre chose.
 */
public final class Board {

    public static final List<String> COLUMNS = List.of("A faire", "En cours", "Fini");
    public static final String IN_PROGRESS = "En cours";

    private final int wipLimit;

    public Board(int wipLimit) {
        if (wipLimit < 1) {
            throw new IllegalArgumentException("limite En cours invalide : " + wipLimit);
        }
        this.wipLimit = wipLimit;
    }

    public void checkNew(NewTask task) {
        if (!task.column().equals(COLUMNS.get(0))) {
            throw new IllegalArgumentException("une nouvelle tache commence dans A faire, pas dans " + task.column());
        }
    }

    /** counts : le nombre de taches par colonne, AVANT le deplacement. */
    public void checkMove(Task task, String to, Map<String, Integer> counts) {
        int from = COLUMNS.indexOf(task.column());
        int target = column(to);
        if (from == target) {
            throw new IllegalArgumentException("la tache " + task.id() + " est deja dans " + to);
        }
        if (Math.abs(target - from) != 1) {
            throw new IllegalArgumentException("deplacement impossible : " + task.column() + " -> " + to + " (une colonne a la fois)");
        }
        if (to.equals(IN_PROGRESS) && counts.getOrDefault(IN_PROGRESS, 0) >= wipLimit) {
            throw new ApiException(409, "limite atteinte : En cours (" + wipLimit + " taches au plus)");
        }
    }

    private static int column(String name) {
        int index = COLUMNS.indexOf(name);
        if (index < 0) {
            throw new IllegalArgumentException("colonne inconnue : " + name);
        }
        return index;
    }
}
