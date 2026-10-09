package ch19_final.drills.r04_concurrency.solution;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

/** Un travail en arriere-plan avec un delai maximal, et un resultat toujours lisible : "ok:…", "delai" ou "erreur:…". */
public final class Async {

    private Async() {
    }

    public static CompletableFuture<String> withTimeout(Supplier<String> work, Executor executor, Duration timeout) {
        return CompletableFuture.supplyAsync(work, executor)
                .orTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
                .thenApply(value -> "ok:" + value)
                .exceptionally(Async::describe);
    }

    private static String describe(Throwable e) {
        Throwable cause = e instanceof CompletionException && e.getCause() != null ? e.getCause() : e;
        return cause instanceof TimeoutException ? "delai" : "erreur:" + cause.getMessage();
    }
}
