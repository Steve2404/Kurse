package ch11_exceptions.projects.p02_calculator.solution;

/**
 * SOLUTION - un calcul impossible (division par zero, depassement...) : non verifiee, et TOUJOURS avec sa cause.
 */
public class EvaluationException extends RuntimeException {

    public EvaluationException(String message, Throwable cause) {
        super(message, cause);
    }
}
