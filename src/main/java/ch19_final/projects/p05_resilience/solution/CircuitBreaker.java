package ch19_final.projects.p05_resilience.solution;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Le disjoncteur, comme celui du tableau electrique :
 *   - FERME (CLOSED) : les appels passent ; threshold echecs DE SUITE l'ouvrent ;
 *   - OUVERT (OPEN) : pendant openFor, tout est refuse tout de suite, sans deranger le service malade ;
 *   - DEMI-OUVERT (HALF_OPEN) : le delai passe, UN seul appel d'essai passe. Reussi : on referme ;
 *     rate : on rouvre pour un nouveau openFor.
 * Les transitions sont synchronized ; l'appel lui-meme ne l'est pas (sinon un appel lent bloquerait tout le monde).
 * Toutes les erreurs ne sont pas des pannes : "reference inconnue" prouve que le service REPOND.
 * isFailure dit lesquelles comptent ; les autres comptent comme une reponse (un succes pour le disjoncteur).
 */
public final class CircuitBreaker {

    public enum State { CLOSED, OPEN, HALF_OPEN }

    private final int threshold;
    private final Duration openFor;
    private final Clock clock;
    private final Predicate<RuntimeException> isFailure;
    private State state = State.CLOSED;
    private int failures;
    private Instant openedAt;
    private boolean trialRunning;

    public CircuitBreaker(int threshold, Duration openFor, Clock clock, Predicate<RuntimeException> isFailure) {
        this.threshold = threshold;
        this.openFor = openFor;
        this.clock = clock;
        this.isFailure = isFailure;
    }

    public <T> T call(Supplier<T> action) {
        beforeCall();
        try {
            T result = action.get();
            onSuccess();
            return result;
        } catch (RuntimeException e) {
            if (isFailure.test(e)) {
                onFailure();
            } else {
                onSuccess();
            }
            throw e;
        }
    }

    public synchronized State state() {
        return state;
    }

    private synchronized void beforeCall() {
        if (state == State.OPEN) {
            Instant reopen = openedAt.plus(openFor);
            if (clock.instant().isBefore(reopen)) {
                long seconds = (Duration.between(clock.instant(), reopen).toMillis() + 999) / 1000;
                throw new CircuitOpenException("circuit ouvert : reessayer dans " + seconds + " s");
            }
            state = State.HALF_OPEN;
        }
        if (state == State.HALF_OPEN) {
            if (trialRunning) {
                throw new CircuitOpenException("circuit ouvert : essai en cours");
            }
            trialRunning = true;
        }
    }

    private synchronized void onSuccess() {
        state = State.CLOSED;
        failures = 0;
        trialRunning = false;
    }

    private synchronized void onFailure() {
        trialRunning = false;
        failures++;
        if (state == State.HALF_OPEN || failures >= threshold) {
            state = State.OPEN;
            openedAt = clock.instant();
            failures = 0;
        }
    }
}
