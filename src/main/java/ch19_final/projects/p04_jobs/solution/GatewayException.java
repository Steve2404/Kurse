package ch19_final.projects.p04_jobs.solution;

/**
 * Un envoi rate. retryable dit si reessayer a un sens : "operateur occupe" passera peut-etre dans une seconde,
 * "numero inconnu" ne passera jamais (reessayer ne ferait que charger l'operateur pour rien).
 */
public class GatewayException extends RuntimeException {

    private final boolean retryable;

    public GatewayException(String message, boolean retryable) {
        super(message);
        this.retryable = retryable;
    }

    public boolean retryable() {
        return retryable;
    }
}
