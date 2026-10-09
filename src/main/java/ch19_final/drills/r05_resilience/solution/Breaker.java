package ch19_final.drills.r05_resilience.solution;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

/** Le disjoncteur : ferme, ouvert apres threshold echecs de suite, demi-ouvert apres openFor (un essai). */
public final class Breaker {

    public enum State { CLOSED, OPEN, HALF_OPEN }

    private final int threshold;
    private final Duration openFor;
    private final Clock clock;
    private State state = State.CLOSED;
    private int failures;
    private Instant openedAt;

    public Breaker(int threshold, Duration openFor, Clock clock) {
        this.threshold = threshold;
        this.openFor = openFor;
        this.clock = clock;
    }

    public <T> T call(Supplier<T> action) {
        before();
        try {
            T result = action.get();
            success();
            return result;
        } catch (RuntimeException e) {
            failure();
            throw e;
        }
    }

    public synchronized State state() {
        return state;
    }

    private synchronized void before() {
        if (state != State.OPEN) {
            return;
        }
        Instant reopen = openedAt.plus(openFor);
        if (clock.instant().isBefore(reopen)) {
            throw new BreakerOpenException((Duration.between(clock.instant(), reopen).toMillis() + 999) / 1000);
        }
        state = State.HALF_OPEN;
    }

    private synchronized void success() {
        state = State.CLOSED;
        failures = 0;
    }

    private synchronized void failure() {
        failures++;
        if (state == State.HALF_OPEN || failures >= threshold) {
            state = State.OPEN;
            openedAt = clock.instant();
            failures = 0;
        }
    }
}
