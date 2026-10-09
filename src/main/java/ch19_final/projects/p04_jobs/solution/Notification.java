package ch19_final.projects.p04_jobs.solution;

/** Un rappel a envoyer : la tache concernee, le telephone du client, le texte. */
public record Notification(long taskId, String phone, String text) {
}
