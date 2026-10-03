package ch13_concurrency.projects.p08_async.solution;

import ch13_concurrency.projects.p08_async.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SOLUTION du projet 8 (bonus) - CompletableFuture, Fork/Join, ThreadLocal, Semaphore, CountDownLatch.
 */
public class AsyncLab {

    // Une valeur PAR THREAD : chaque thread voit sa propre copie, initialisee a 0.
    static final ThreadLocal<Integer> PER_THREAD = ThreadLocal.withInitial(() -> 0);

    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try {
            TravelAgency agency = new TravelAgency(pool);
            // On lance TOUT, puis on attend tout (allOf) ; join() lit chaque resultat dans l'ordre des voyages.
            List<CompletableFuture<String>> quotes = new ArrayList<>();
            for (String city : Data.TRIPS) {
                quotes.add(agency.quote(city));
            }
            CompletableFuture.allOf(quotes.toArray(new CompletableFuture[0])).join();
            for (CompletableFuture<String> q : quotes) {
                System.out.println("devis : " + q.join());
            }

            // handle : recoit le resultat OU l'exception (l'un des deux est null).
            String handled = agency.flight("Atlantis").handle((price, e) -> e == null ? "prix " + price : "erreur " + e.getCause().getClass().getSimpleName()).join();
            // join() enveloppe dans CompletionException (non verifiee) ; get() dans ExecutionException (verifiee).
            String viaJoin;
            try {
                agency.flight("Atlantis").join();
                viaJoin = "ok";
            } catch (CompletionException e) {
                viaJoin = e.getClass().getSimpleName() + " <- " + e.getCause().getClass().getSimpleName();
            }
            String viaGet;
            try {
                agency.flight("Atlantis").get();
                viaGet = "ok";
            } catch (ExecutionException e) {
                viaGet = e.getClass().getSimpleName();
            }
            System.out.println("erreurs : handle " + handled + " ; join " + viaJoin + " ; get " + viaGet);

            // Delais (Java 9) : completeOnTimeout donne une valeur par defaut, orTimeout echoue.
            CompletableFuture<String> never = new CompletableFuture<>();
            String fallback = never.completeOnTimeout("valeur par defaut", 20, TimeUnit.MILLISECONDS).join();
            String timeout;
            try {
                new CompletableFuture<String>().orTimeout(20, TimeUnit.MILLISECONDS).join();
                timeout = "ok";
            } catch (CompletionException e) {
                timeout = e.getCause().getClass().getSimpleName();
            }
            // anyOf : le premier termine ; une future deja completee gagne forcement.
            Object first = CompletableFuture.anyOf(CompletableFuture.completedFuture("cache"), new CompletableFuture<String>()).join();
            List<String> events = Collections.synchronizedList(new ArrayList<>());
            CompletableFuture.supplyAsync(() -> 21, pool).thenApply(n -> n * 2).thenAccept(n -> events.add("recu " + n)).thenRun(() -> events.add("fini")).join();
            CompletableFuture<String> manual = new CompletableFuture<>();
            manual.complete("a la main");
            System.out.println("delais : " + fallback + ", orTimeout " + timeout + " ; anyOf " + first
                    + " ; chaine " + events + " ; complete " + manual.join() + " isDone " + manual.isDone());

            // ThreadLocal : chaque thread du pool cumule dans SA copie ; main garde la sienne a 0.
            AtomicInteger total = new AtomicInteger();
            List<Future<?>> fs = new ArrayList<>();
            for (int t = 0; t < 8; t++) {
                fs.add(pool.submit(() -> {
                    for (int i = 0; i < 100; i++) {
                        PER_THREAD.set(PER_THREAD.get() + 1);
                    }
                    total.addAndGet(100);
                }));
            }
            for (Future<?> f : fs) {
                f.get();
            }
            PER_THREAD.set(42);
            int mine = PER_THREAD.get();
            PER_THREAD.remove();                              // remove : retour a la valeur initiale
            System.out.println("ThreadLocal : total des increments " + total.get() + ", valeur de main " + mine + " puis apres remove " + PER_THREAD.get());

            // Semaphore : au plus 2 taches a la fois dans la section critique ; CountDownLatch : un depart commun.
            Semaphore permits = new Semaphore(2);
            AtomicInteger inside = new AtomicInteger();
            AtomicInteger maxInside = new AtomicInteger();
            CountDownLatch startGate = new CountDownLatch(1);
            CountDownLatch done = new CountDownLatch(6);
            ExecutorService six = Executors.newFixedThreadPool(6);
            try {
                for (int t = 0; t < 6; t++) {
                    six.execute(() -> {
                        try {
                            startGate.await();
                            permits.acquire();
                            try {
                                maxInside.accumulateAndGet(inside.incrementAndGet(), Math::max);
                                Thread.sleep(10);
                                inside.decrementAndGet();
                            } finally {
                                permits.release();
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } finally {
                            done.countDown();
                        }
                    });
                }
                long before = done.getCount();
                startGate.countDown();                        // tous les ouvriers partent en meme temps
                done.await();
                System.out.println("Semaphore(2) : au plus 2 a la fois " + (maxInside.get() <= 2) + ", permis disponibles " + permits.availablePermits()
                        + " ; CountDownLatch " + before + " -> " + done.getCount() + ", tryAcquire(3) " + permits.tryAcquire(3));
            } finally {
                six.shutdown();
            }
        } finally {
            pool.shutdown();
        }
        pool.awaitTermination(5, TimeUnit.SECONDS);

        // Fork/Join : un pool qui pratique le "vol de travail" ; invoke() lance la tache racine et attend.
        ForkJoinPool fj = new ForkJoinPool(4);
        try {
            MaxSubarrayTask.Summary s = fj.invoke(new MaxSubarrayTask(0, Data.SIZE));
            System.out.println("Fork/Join : sous-tableau maximal " + s.best() + ", total " + s.total() + " ; Kadane sequentiel " + MaxSubarrayTask.kadane(Data.SIZE)
                    + " identique " + (s.best() == MaxSubarrayTask.kadane(Data.SIZE)));
            int[] values = new int[Data.SIZE];
            Arrays.setAll(values, Data::delta);
            fj.invoke(new ClampAction(values, 0, values.length));
            System.out.println("RecursiveAction : min " + Arrays.stream(values).min().orElseThrow() + ", max " + Arrays.stream(values).max().orElseThrow()
                    + " ; parallelisme du pool " + fj.getParallelism());
        } finally {
            fj.shutdown();
        }
    }
}
