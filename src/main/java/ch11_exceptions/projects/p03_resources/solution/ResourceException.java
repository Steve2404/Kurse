package ch11_exceptions.projects.p03_resources.solution;

/**
 * SOLUTION - l'echec d'ouverture ou de fermeture d'une ressource (verifiee).
 */
public class ResourceException extends Exception {

    public ResourceException(String message) {
        super(message);
    }
}
