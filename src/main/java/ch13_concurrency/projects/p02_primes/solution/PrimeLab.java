package ch13_concurrency.projects.p02_primes.solution;

import ch13_concurrency.projects.p02_primes.Data;

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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * SOLUTION du projet 2 - les executeurs : soumettre, recuperer, combiner, annuler, arreter.
 */
public class PrimeLab {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int[] base = SieveTask.primesUpTo((int) Math.sqrt(Data.HIGH) + 1);
        int width = (Data.HIGH - Data.LOW) / Data.SEGMENTS;
        List<SieveTask> tasks = new ArrayList<>();
        for (int i = 0; i < Data.SEGMENTS; i++) {
            tasks.add(new SieveTask(Data.LOW + i * width, i == Data.SEGMENTS - 1 ? Data.HIGH : Data.LOW + (i + 1) * width, base));
        }

        ExecutorService pool = Executors.newFixedThreadPool(Data.THREADS);
        try {
            // submit rend tout de suite un Future ; get() bloque jusqu'au resultat. On lit dans l'ORDRE des segments.
            List<Future<Segment>> futures = new ArrayList<>();
            for (SieveTask t : tasks) {
                futures.add(pool.submit(t));
            }
            Segment total = null;
            StringBuilder counts = new StringBuilder();
            for (Future<Segment> f : futures) {
                Segment s = f.get();
                counts.append(' ').append(s.count());
                total = total == null ? s : total.merge(s);
            }
            System.out.println("premiers dans [" + Data.LOW + ", " + Data.HIGH + ") par segment :" + counts);
            System.out.println("total " + total.count() + ", premier " + total.first() + ", dernier " + total.last() + ", plus grand ecart " + total.maxGap());

            // invokeAll : attend TOUTES les taches ; les Future sont dans l'ordre de la liste.
            int viaInvokeAll = 0;
            for (Future<Segment> f : pool.invokeAll(tasks)) {
                viaInvokeAll += f.get().count();
            }
            System.out.println("invokeAll : " + viaInvokeAll + " (identique " + (viaInvokeAll == total.count()) + ")");

            // invokeAny : rend le resultat d'UNE tache reussie (les autres sont annulees) ; ici une seule reussit.
            List<Callable<String>> mirrors = new ArrayList<>();
            for (String m : Data.MIRRORS) {
                mirrors.add(() -> {
                    if (m.endsWith("panne")) {
                        throw new IllegalStateException("miroir " + m.charAt(0) + " en panne");
                    }
                    return "miroir " + m.charAt(0);
                });
            }
            System.out.println("invokeAny : " + pool.invokeAny(mirrors));

            // get avec delai : TimeoutException ; puis cancel(true) interrompt la tache.
            Future<String> slow = pool.submit(() -> {
                Thread.sleep(10_000);
                return "trop tard";
            });
            String timeout;
            try {
                timeout = slow.get(50, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                timeout = e.getClass().getSimpleName();
            }
            boolean cancelled = slow.cancel(true);
            System.out.println("get(50 ms) : " + timeout + ", cancel " + cancelled + ", isCancelled " + slow.isCancelled() + ", isDone " + slow.isDone());

            // Une exception dans la tache revient ENVELOPPEE dans ExecutionException.
            Future<Integer> failing = pool.submit(() -> 10 / (Data.SEGMENTS - Data.SEGMENTS));
            try {
                failing.get();
            } catch (ExecutionException e) {
                System.out.println("tache en echec : " + e.getClass().getSimpleName() + " <- " + e.getCause().getClass().getSimpleName() + ": " + e.getCause().getMessage());
            }

            // submit(Runnable) rend un Future<?> dont get() vaut null ; execute ne rend rien.
            Future<?> noResult = pool.submit(() -> System.out.print(""));
            System.out.println("submit(Runnable).get() = " + noResult.get());
        } finally {
            pool.shutdown();                              // TOUJOURS arreter un executeur (sinon la JVM ne s'arrete pas)
        }
        System.out.println("pool : isShutdown " + pool.isShutdown() + ", awaitTermination " + pool.awaitTermination(5, TimeUnit.SECONDS) + ", isTerminated "
                + pool.isTerminated());
        String rejected;
        try {
            pool.submit(() -> 1);
            rejected = "accepte";
        } catch (RejectedExecutionException e) {
            rejected = e.getClass().getSimpleName();
        }
        System.out.println("soumission apres shutdown : " + rejected);

        // Un executeur a UN thread execute les taches dans l'ordre ; shutdownNow rend celles jamais commencees.
        ExecutorService single = Executors.newSingleThreadExecutor();
        List<String> order = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch started = new CountDownLatch(1);
        single.execute(() -> {
            order.add("bloquante");
            started.countDown();
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                order.add("bloquante interrompue");
            }
        });
        for (String name : List.of("t1", "t2", "t3")) {
            single.execute(() -> order.add(name));
        }
        started.await();
        List<Runnable> neverRun = single.shutdownNow();
        single.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("shutdownNow : " + neverRun.size() + " taches jamais lancees, journal " + order);
    }
}
