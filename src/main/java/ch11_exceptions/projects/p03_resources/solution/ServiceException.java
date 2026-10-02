package ch11_exceptions.projects.p03_resources.solution;

/**
 * SOLUTION - un appel de service rate (verifiee).
 */
public class ServiceException extends Exception {

    public ServiceException(String message) {
        super(message);
    }
}
