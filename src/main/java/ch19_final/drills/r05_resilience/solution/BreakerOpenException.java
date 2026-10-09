package ch19_final.drills.r05_resilience.solution;

public class BreakerOpenException extends RuntimeException {

    public BreakerOpenException(long seconds) {
        super("ouvert : encore " + seconds + " s");
    }
}
