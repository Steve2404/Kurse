package ch13_concurrency.drills.r02_executors.solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SOLUTION du drill de rappel 2 - ExecutorService, Future, invokeAll / invokeAny, arret, planification.
 */
public class Recall02 {

    public static void main(String[] args) throws Exception {
        ExecutorService single = Executors.newSingleThreadExecutor();
        List<String> order = Collections.synchronizedList(new ArrayList<>());
        try {
            for (String s : List.of("a", "b", "c")) {
                single.execute(() -> order.add(s));
            }
        } finally {
            single.shutdown();
        }
        single.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("D01 : " + order);

        ExecutorService pool = Executors.newFixedThreadPool(3);
        try {
            Future<Integer> f = pool.submit(() -> 6 * 7);
            System.out.println("D02 : " + f.get() + " " + f.isDone());

            List<Callable<Integer>> tasks = List.of(() -> 1, () -> 2, () -> 3);
            int sum = 0;
            for (Future<Integer> r : pool.invokeAll(tasks)) {
                sum += r.get();
            }
            String any = pool.invokeAny(List.of(() -> {
                throw new IllegalStateException();
            }, () -> "seule reussite"));
            System.out.println("D03 : " + sum + " " + any);

            Future<String> slow = pool.submit(() -> {
                Thread.sleep(10_000);
                return "fini";
            });
            String t;
            try {
                t = slow.get(10, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                t = e.getClass().getSimpleName();
            }
            System.out.println("D04 : " + t + " " + slow.cancel(true) + " " + slow.isCancelled());

            Future<?> boom = pool.submit(() -> {
                throw new UnsupportedOperationException("non");
            });
            try {
                boom.get();
            } catch (ExecutionException e) {
                System.out.println("D05 : " + e.getCause().getClass().getSimpleName() + " " + e.getCause().getMessage());
            }
            Future<?> runnable = pool.submit(() -> System.out.print(""));
            System.out.println("D06 : " + runnable.get());
        } finally {
            pool.shutdown();
        }
        boolean terminated = pool.awaitTermination(5, TimeUnit.SECONDS);
        String rejected;
        try {
            pool.execute(() -> { });
            rejected = "ok";
        } catch (RejectedExecutionException e) {
            rejected = e.getClass().getSimpleName();
        }
        System.out.println("D07 : " + pool.isShutdown() + " " + terminated + " " + pool.isTerminated() + " " + rejected);

        // invokeAll avec delai : les taches non finies a temps sont ANNULEES ; newCachedThreadPool cree des threads a la demande.
        ExecutorService cached = Executors.newCachedThreadPool();
        try {
            List<Future<String>> results = cached.invokeAll(List.of(() -> "vite", () -> {
                Thread.sleep(10_000);
                return "lent";
            }), 100, TimeUnit.MILLISECONDS);
            System.out.println("D08 : " + results.get(0).get() + " " + results.get(1).isCancelled() + " " + results.get(0).isDone() + " " + results.get(1).isDone());
        } finally {
            cached.shutdown();
        }

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        try {
            ScheduledFuture<String> later = scheduler.schedule(() -> "plus tard", 10, TimeUnit.MILLISECONDS);
            AtomicInteger ticks = new AtomicInteger();
            CountDownLatch three = new CountDownLatch(3);
            ScheduledFuture<?> rate = scheduler.scheduleAtFixedRate(() -> {
                ticks.incrementAndGet();
                three.countDown();
            }, 0, 2, TimeUnit.MILLISECONDS);
            three.await();
            rate.cancel(false);
            System.out.println("D09 : " + later.get() + " " + (ticks.get() >= 3) + " " + rate.isCancelled());
        } finally {
            scheduler.shutdown();
        }
    }
}
