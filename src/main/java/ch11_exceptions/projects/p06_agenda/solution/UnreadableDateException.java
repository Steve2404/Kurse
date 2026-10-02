package ch11_exceptions.projects.p06_agenda.solution;

/**
 * SOLUTION - aucun format n'a su lire la date. Chaque tentative ratee est attachee en exception SUPPRIMEE.
 */
public class UnreadableDateException extends Exception {

    public UnreadableDateException(String text) {
        super("date illisible : " + text);
    }
}
