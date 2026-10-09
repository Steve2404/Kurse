package ch19_final.projects.p03_api.solution;

/** Une erreur qui porte elle-meme son code HTTP (415, 413...) : pour les cas qui ne sont pas des regles metier. */
public class ApiException extends RuntimeException {

    private final int status;

    public ApiException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int status() {
        return status;
    }
}
