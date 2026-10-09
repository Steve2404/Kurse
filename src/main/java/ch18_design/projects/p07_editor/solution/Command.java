package ch18_design.projects.p07_editor.solution;

/**
 * La COMMANDE : une action devenue un OBJET. On peut donc la garder, la rejouer, l'annuler, la grouper.
 * Chaque commande sait se defaire elle-meme.
 */
public interface Command {

    void execute();

    void undo();

    String label();
}
