package ch19_final.projects.p04_jobs.solution;

/** Le resultat d'un rappel : status vaut "envoye", "echec", "delai depasse" ou "refuse" ; detail precise. */
public record SendResult(long taskId, String status, String detail) {
}
