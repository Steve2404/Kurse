package ch13_concurrency.solutions;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.exercises.Exercise05_TaskPool.
 */
public class Solution05_TaskPool {

    public static String firstSuccess(ExecutorService executor, List<Callable<String>> tasks) throws Exception {
        // invokeAny ignore les taches en echec et rend le resultat d'une tache REUSSIE ; les autres sont annulees.
        return executor.invokeAny(tasks);
    }

    public static String sumWithin(ExecutorService executor, List<Callable<Integer>> tasks, long millis) throws Exception {
        // Apres le delai, invokeAll ANNULE les taches pas finies : on teste isCancelled() avant get(),
        // sinon get() lancerait CancellationException.
        int sum = 0;
        int cancelled = 0;
        for (Future<Integer> f : executor.invokeAll(tasks, millis, TimeUnit.MILLISECONDS)) {
            if (f.isCancelled()) {
                cancelled++;
            } else {
                sum += f.get();
            }
        }
        return "sum=" + sum + " cancelled=" + cancelled;
    }

    public static String submitAfterShutdown(ExecutorService executor) {
        // shutdown ferme la porte : toute nouvelle tache est refusee (RejectedExecutionException, unchecked).
        executor.shutdown();
        try {
            executor.submit(() -> { });
            return "aucune";
        } catch (RejectedExecutionException e) {
            return e.getClass().getSimpleName();
        }
    }

    public static int stopEverything(ExecutorService executor, CountDownLatch running, AtomicBoolean interrupted) throws InterruptedException {
        // shutdownNow interrompt la tache EN COURS et rend les taches de la file qui n'ont jamais demarre.
        executor.submit(() -> {
            running.countDown();
            try {
                new CountDownLatch(1).await();
            } catch (InterruptedException e) {
                interrupted.set(true);
            }
        });
        for (int i = 0; i < 3; i++) {
            executor.submit(() -> { });
        }
        running.await();
        int pending = executor.shutdownNow().size();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        return pending;
    }
}
