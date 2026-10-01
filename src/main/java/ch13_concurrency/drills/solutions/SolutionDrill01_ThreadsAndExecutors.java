package ch13_concurrency.drills.solutions;

import ch13_concurrency.drills.Loans;
import ch13_concurrency.drills.Loans.Loan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Corrige du drill 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.drills.exercises.Drill01_ThreadsAndExecutors.
 */
public class SolutionDrill01_ThreadsAndExecutors {

    public static void runInThread(Runnable task) throws InterruptedException {
        // start() cree le thread ; join() attend qu'il ait fini.
        Thread t = new Thread(task);
        t.start();
        t.join();
    }

    public static int totalDaysTwoThreads() throws InterruptedException {
        // Chaque thread ecrit dans SA case du tableau : aucune donnee partagee en ecriture ; join() rend les resultats visibles.
        int[] partial = new int[2];
        int half = Loans.LOANS.size() / 2;
        Thread first = new Thread(() -> partial[0] = sum(Loans.LOANS.subList(0, half)));
        Thread second = new Thread(() -> partial[1] = sum(Loans.LOANS.subList(half, Loans.LOANS.size())));
        first.start();
        second.start();
        first.join();
        second.join();
        return partial[0] + partial[1];
    }

    public static int loanCount(ExecutorService executor) throws Exception {
        // Une lambda qui RENDS une valeur est une Callable : submit rend un Future, get() attend le resultat.
        return executor.submit(() -> Loans.LOANS.size()).get();
    }

    public static int totalDaysInvokeAll(ExecutorService executor) throws Exception {
        // invokeAll attend que TOUTES les taches soient finies ; les Future sont dans l'ordre des taches.
        List<Callable<Integer>> tasks = new ArrayList<>();
        for (Loan loan : Loans.LOANS) {
            tasks.add(loan::days);
        }
        int total = 0;
        for (Future<Integer> f : executor.invokeAll(tasks)) {
            total += f.get();
        }
        return total;
    }

    public static String anyTitle(ExecutorService executor) throws Exception {
        // invokeAny rend le resultat d'UNE tache reussie (laquelle : non garanti) et annule les autres.
        List<Callable<String>> tasks = new ArrayList<>();
        for (Loan loan : Loans.LOANS) {
            tasks.add(loan::title);
        }
        return executor.invokeAny(tasks);
    }

    public static int countWithExecute(ExecutorService executor) throws InterruptedException {
        // execute ne rend rien : pour savoir que tout est fini, shutdown puis awaitTermination.
        AtomicInteger counter = new AtomicInteger();
        for (int i = 0; i < 12; i++) {
            executor.execute(counter::incrementAndGet);
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        return counter.get();
    }

    public static boolean closeProperly(ExecutorService executor) throws InterruptedException {
        // shutdown refuse les nouvelles taches ; awaitTermination rend true si tout a fini a temps.
        executor.shutdown();
        return executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    public static String reminderIn50ms() throws Exception {
        // schedule(Callable, ...) rend un ScheduledFuture : get() attend le delai puis le resultat.
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        try {
            return scheduler.schedule(() -> "rappel envoye", 50, TimeUnit.MILLISECONDS).get();
        } finally {
            scheduler.shutdown();
        }
    }

    public static String failureCause(ExecutorService executor) throws InterruptedException {
        // L'exception de la tache n'est pas lancee telle quelle : get() lance ExecutionException, l'originale est la cause.
        Future<String> f = executor.submit(() -> {
            throw new IOException("disque plein");
        });
        try {
            f.get();
            return "aucune";
        } catch (ExecutionException e) {
            return e.getCause().getClass().getSimpleName();
        }
    }

    public static Set<String> workerNames() throws InterruptedException {
        // Un pool FIXE de 2 reutilise ses 2 threads : jamais plus de 2 noms differents.
        Set<String> names = ConcurrentHashMap.newKeySet();
        ExecutorService pool = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 12; i++) {
            pool.submit(() -> names.add(Thread.currentThread().getName()));
        }
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
        return names;
    }

    private static int sum(List<Loan> loans) {
        // Boite magique : total des jours d'une liste d'emprunts.
        int total = 0;
        for (Loan loan : loans) {
            total += loan.days();
        }
        return total;
    }
}
