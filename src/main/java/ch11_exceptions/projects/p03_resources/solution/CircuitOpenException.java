package ch11_exceptions.projects.p03_resources.solution;

/**
 * SOLUTION - appel refuse sans meme essayer, car le disjoncteur est ouvert (non verifiee).
 */
public class CircuitOpenException extends RuntimeException {

    public CircuitOpenException(String message) {
        super(message);
    }
}
