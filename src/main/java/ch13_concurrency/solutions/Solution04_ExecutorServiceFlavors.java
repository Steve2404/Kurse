package ch13_concurrency.solutions;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.exercises.Exercise04_ExecutorServiceFlavors.
 */
public class Solution04_ExecutorServiceFlavors {

    public static void runInSingleThreadOrder(int taskCount, List<String> trace) throws InterruptedException {
        // Un seul ouvrier = les taches passent dans l'ordre de soumission, meme si les premieres sont les plus lentes.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < taskCount; i++) {
            int index = i;
            executor.submit(() -> {
                try {
                    Thread.sleep((taskCount - index) * 15L);
                } catch (InterruptedException ignored) {
                }
                trace.add("tache-" + index);
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    public static int countScheduledExecutions(int targetCount, long periodMillis) throws InterruptedException {
        // Un latch remplace un sleep devine : on attend exactement targetCount executions, puis on annule la tache periodique.
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(targetCount);
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            counter.incrementAndGet();
            latch.countDown();
        }, 0, periodMillis, TimeUnit.MILLISECONDS);
        latch.await(5, TimeUnit.SECONDS);
        future.cancel(true);
        scheduler.shutdown();
        return counter.get();
    }
}
