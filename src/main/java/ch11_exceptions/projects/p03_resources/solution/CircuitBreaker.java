package ch11_exceptions.projects.p03_resources.solution;

/**
 * SOLUTION - un disjoncteur : apres THRESHOLD echecs consecutifs il s'OUVRE et refuse PAUSE appels,
 * puis laisse passer UN appel d'essai (HALF_OPEN) : succes -> CLOSED, echec -> OPEN de nouveau.
 */
public class CircuitBreaker {

    public enum State { CLOSED, OPEN, HALF_OPEN }

    private final int threshold;
    private final int pause;
    private State state = State.CLOSED;
    private int failures;
    private int cooldown;

    public CircuitBreaker(int threshold, int pause) {
        this.threshold = threshold;
        this.pause = pause;
    }

    public State state() {
        return state;
    }

    // outcome simule la reponse du service distant ("ok" ou "ko").
    public String call(String outcome) throws ServiceException {
        if (state == State.OPEN) {
            if (cooldown > 0) {
                cooldown--;
                throw new CircuitOpenException("circuit ouvert");   // non verifiee : pas dans le throws
            }
            state = State.HALF_OPEN;
        }
        if (outcome.equals("ko")) {
            failures++;
            if (state == State.HALF_OPEN || failures >= threshold) {
                state = State.OPEN;
                cooldown = pause;
            }
            throw new ServiceException("ko");
        }
        failures = 0;
        state = State.CLOSED;
        return outcome;
    }
}
