package ch19_final.drills.r04_concurrency.solution;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/** L'arret propre : plus rien de nouveau, une grace pour finir, puis on interrompt ; rend les jamais commences. */
public final class Shutdown {

    private Shutdown() {
    }

    public static int stop(ExecutorService executor, Duration grace) throws InterruptedException {
        executor.shutdown();
        if (executor.awaitTermination(grace.toMillis(), TimeUnit.MILLISECONDS)) {
            return 0;
        }
        return executor.shutdownNow().size();
    }
}
