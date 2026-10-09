package ch19_final.drills.r04_concurrency.solution;

/** Un echec passager : reessayer a un sens. */
public class RetryableException extends RuntimeException {

    public RetryableException(String message) {
        super(message);
    }
}
