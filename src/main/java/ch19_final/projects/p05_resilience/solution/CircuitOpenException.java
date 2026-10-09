package ch19_final.projects.p05_resilience.solution;

/** Le disjoncteur refuse l'appel SANS deranger le service : il est ouvert, ou un essai est deja en cours. */
public class CircuitOpenException extends RuntimeException {

    public CircuitOpenException(String message) {
        super(message);
    }
}
