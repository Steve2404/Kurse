package ch19_final.projects.p04_jobs.solution;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * L'envoi des rappels en arriere-plan :
 *   - un pool de fils BORNE, avec une file d'attente BORNEE : quand tout est plein, on refuse tout de suite
 *     (sinon la file grandit sans fin et la memoire avec) ;
 *   - un delai maximal par rappel ;
 *   - l'idempotence : le meme rappel demande deux fois n'est envoye qu'une fois ;
 *   - un arret propre : on laisse finir, puis on coupe.
 */
public final class ReminderService implements AutoCloseable {

    private final SmsGateway gateway;
    private final Retrier retrier;
    private final Duration timeout;
    private final Metrics metrics;
    private final ThreadPoolExecutor executor;
    private final Map<Long, CompletableFuture<SendResult>> accepted = new ConcurrentHashMap<>();

    public ReminderService(SmsGateway gateway, Retrier retrier, int threads, int queueCapacity, Duration timeout,
                           Metrics metrics) {
        this.gateway = gateway;
        this.retrier = retrier;
        this.timeout = timeout;
        this.metrics = metrics;
        // AbortPolicy (le choix par defaut) : quand la file est pleine, execute lance RejectedExecutionException.
        this.executor = new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueCapacity), new ThreadPoolExecutor.AbortPolicy());
    }

    /** Envoie le rappel en arriere-plan. Le meme taskId deja accepte rend LE MEME futur : un seul envoi. */
    public CompletableFuture<SendResult> send(Notification n) {
        CompletableFuture<SendResult> result = accepted.computeIfAbsent(n.taskId(), id -> start(n));
        // Un refus n'est pas retenu : le meme rappel pourra etre redemande plus tard.
        if (result.isDone() && result.join().status().equals("refuse")) {
            accepted.remove(n.taskId(), result);
        }
        return result;
    }

    /** Envoie tout, attend tout, et rend les resultats tries par tache. */
    public List<SendResult> sendAll(List<Notification> notifications) {
        List<CompletableFuture<SendResult>> futures = notifications.stream().map(this::send).toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return futures.stream().map(CompletableFuture::join)
                .sorted(Comparator.comparingLong(SendResult::taskId)).toList();
    }

    private CompletableFuture<SendResult> start(Notification n) {
        try {
            return CompletableFuture.supplyAsync(() -> deliver(n), executor)
                    .orTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
                    .exceptionally(e -> failure(n, e));
        } catch (RejectedExecutionException e) {
            metrics.rejected();
            return CompletableFuture.completedFuture(new SendResult(n.taskId(), "refuse", "file pleine"));
        }
    }

    private SendResult deliver(Notification n) {
        String id = retrier.call(() -> gateway.send(n.phone(), n.text()));
        metrics.sent();
        return new SendResult(n.taskId(), "envoye", id);
    }

    // Les erreurs d'un CompletableFuture arrivent enveloppees dans une CompletionException : on deballe.
    private SendResult failure(Notification n, Throwable e) {
        Throwable cause = e instanceof CompletionException && e.getCause() != null ? e.getCause() : e;
        if (cause instanceof TimeoutException) {
            metrics.timedOut();
            return new SendResult(n.taskId(), "delai depasse", timeout.toMillis() + " ms");
        }
        metrics.failed();
        return new SendResult(n.taskId(), "echec", cause.getMessage());
    }

    /**
     * L'arret propre : plus de nouveaux rappels, ceux en cours et en attente ont grace pour finir ; apres,
     * on interrompt. Rend le nombre de rappels qui n'ont jamais commence.
     */
    public int shutdown(Duration grace) throws InterruptedException {
        executor.shutdown();
        if (executor.awaitTermination(grace.toMillis(), TimeUnit.MILLISECONDS)) {
            return 0;
        }
        return executor.shutdownNow().size();
    }

    @Override
    public void close() {
        executor.shutdownNow();
    }
}
