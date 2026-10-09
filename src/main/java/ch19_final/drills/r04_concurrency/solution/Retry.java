package ch19_final.drills.r04_concurrency.solution;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/** Reessayer les seuls echecs passagers, en doublant l'attente ; la derniere erreur porte les precedentes. */
public final class Retry {

    private Retry() {
    }

    public static <T> T call(Supplier<T> action, int attempts, Duration first, Sleeper sleeper) {
        List<RetryableException> earlier = new ArrayList<>();
        Duration delay = first;
        for (int attempt = 1; ; attempt++) {
            try {
                return action.get();
            } catch (RetryableException e) {
                if (attempt >= attempts) {
                    earlier.forEach(e::addSuppressed);
                    throw e;
                }
                earlier.add(e);
            }
            pause(sleeper, delay);
            delay = delay.multipliedBy(2);
        }
    }

    private static void pause(Sleeper sleeper, Duration delay) {
        try {
            sleeper.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("interrompu");
        }
    }
}
