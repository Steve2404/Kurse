package ch19_final.projects.p04_jobs.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Reessayer un appel qui echoue pour une raison passagere. Trois regles :
 *   - on ne reessaie que ce qui a une chance de passer (GatewayException.retryable) ;
 *   - on attend de plus en plus longtemps entre deux essais (RetryPolicy) ;
 *   - a la fin, l'appelant recoit la DERNIERE erreur, avec les precedentes en "suppressed" (rien ne se perd).
 */
public final class Retrier {

    private final RetryPolicy policy;
    private final Sleeper sleeper;
    private final Metrics metrics;

    public Retrier(RetryPolicy policy, Sleeper sleeper, Metrics metrics) {
        this.policy = policy;
        this.sleeper = sleeper;
        this.metrics = metrics;
    }

    public <T> T call(Supplier<T> action) {
        List<GatewayException> earlier = new ArrayList<>();
        for (int attempt = 1; ; attempt++) {
            try {
                return action.get();
            } catch (GatewayException e) {
                if (!e.retryable() || attempt == policy.maxAttempts()) {
                    earlier.forEach(e::addSuppressed);
                    throw e;
                }
                earlier.add(e);
            }
            pause(attempt, earlier);
        }
    }

    private void pause(int failures, List<GatewayException> earlier) {
        metrics.retry();
        try {
            sleeper.sleep(policy.delayAfter(failures));
        } catch (InterruptedException e) {
            // On remet le drapeau : celui qui nous a interrompus (l'arret du programme) doit pouvoir le voir.
            Thread.currentThread().interrupt();
            GatewayException stopped = new GatewayException("interrompu pendant l'attente", false);
            earlier.forEach(stopped::addSuppressed);
            throw stopped;
        }
    }
}
