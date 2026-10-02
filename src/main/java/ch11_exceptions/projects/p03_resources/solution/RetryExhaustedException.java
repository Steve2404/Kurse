package ch11_exceptions.projects.p03_resources.solution;

/**
 * SOLUTION - tous les essais ont echoue : la CAUSE est le dernier echec, les precedents sont SUPPRIMES.
 */
public class RetryExhaustedException extends Exception {

    public RetryExhaustedException(String message, Throwable cause) {
        super(message, cause);
    }
}
