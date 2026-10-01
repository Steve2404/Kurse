package ch13_concurrency.drills.exercises;

import ch13_concurrency.ExerciseChecker;
import ch13_concurrency.drills.Loans;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * DRILL 01 - Threads et ExecutorService
 * =====================================
 *
 * -- Comment utiliser un DRILL (different d'un exercice) --
 *
 * Un exercice t'APPREND une notion. Un drill te la fait REPETER jusqu'a
 * ce qu'elle sorte toute seule. Chaque TODO tient en quelques lignes et
 * vise UNE forme precise (entre crochets).
 *
 *   1. Chronometre-toi, note ton temps et ton score dans drills/REVISION.md.
 *   2. Ecris SANS regarder la "carte memoire" en bas. Bloque plus d'une
 *      minute : regarde-la, cache-la, reecris.
 *   3. Refais le MEME drill plus tard, a partir de zero (voir REVISION.md).
 *
 * Donnees : ch13_concurrency.drills.Loans (12 emprunts, 134 jours au total).
 * Lance main() PLUSIEURS fois : un code concurrent faux peut passer par chance.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : runInThread(task)          [new Thread + start + join] executer task dans un nouveau thread et attendre sa fin.
 * TODO 2  : totalDaysTwoThreads()      [deux threads + join] chacun additionne une MOITIE de LOANS ; rendre le total -> 134.
 * TODO 3  : loanCount(executor)        [submit(Callable) + get] une tache qui rend LOANS.size() -> 12.
 * TODO 4  : totalDaysInvokeAll(executor) [invokeAll] une Callable par emprunt (elle rend ses jours) ; additionner -> 134.
 * TODO 5  : anyTitle(executor)         [invokeAny] une Callable par emprunt (elle rend son titre) ; rendre le titre obtenu.
 * TODO 6  : countWithExecute(executor) [execute(Runnable) + shutdown + awaitTermination] 12 taches qui incrementent un compteur atomique.
 * TODO 7  : closeProperly(executor)    [shutdown + awaitTermination] rendre true si l'executor est termine dans les 5 s.
 * TODO 8  : reminderIn50ms()           [ScheduledExecutorService.schedule(Callable, delai, unite)] -> "rappel envoye".
 * TODO 9  : failureCause(executor)     [ExecutionException.getCause()] une Callable qui lance IOException ; rendre le nom simple de la cause.
 * TODO 10 : workerNames()              [newFixedThreadPool(2)] 12 taches notent le nom de leur thread dans un Set concurrent ; rendre ce Set.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   new Thread(runnable).start() ; t.join() ; Runnable : void run() ; Callable<T> : T call() throws Exception
 *   Executors.newSingleThreadExecutor() / newFixedThreadPool(n) / newCachedThreadPool() / newScheduledThreadPool(n)
 *   execute(Runnable) -> void ; submit(Runnable | Callable) -> Future ; invokeAll(taches) -> List<Future> (attend tout)
 *   invokeAny(taches) -> T (une tache REUSSIE) ; future.get() -> attend ; ExecutionException emballe l'exception
 *   shutdown() puis awaitTermination(5, TimeUnit.SECONDS) ; shutdownNow() interrompt et rend la file
 *   schedule(tache, delai, unite) ; scheduleAtFixedRate(r, delaiInitial, periode, unite) ; scheduleWithFixedDelay(...)
 *   toujours fermer un executor (finally ou try-with-resources en Java 19+ : PAS en Java 17)
 * ---------------------------------------------------------------------
 */
public class Drill01_ThreadsAndExecutors {

    public static void runInThread(Runnable task) throws InterruptedException {
        throw new UnsupportedOperationException("TODO 1 : implementer runInThread()");
    }

    public static int totalDaysTwoThreads() throws InterruptedException {
        throw new UnsupportedOperationException("TODO 2 : implementer totalDaysTwoThreads()");
    }

    public static int loanCount(ExecutorService executor) throws Exception {
        throw new UnsupportedOperationException("TODO 3 : implementer loanCount()");
    }

    public static int totalDaysInvokeAll(ExecutorService executor) throws Exception {
        throw new UnsupportedOperationException("TODO 4 : implementer totalDaysInvokeAll()");
    }

    public static String anyTitle(ExecutorService executor) throws Exception {
        throw new UnsupportedOperationException("TODO 5 : implementer anyTitle()");
    }

    public static int countWithExecute(ExecutorService executor) throws InterruptedException {
        throw new UnsupportedOperationException("TODO 6 : implementer countWithExecute()");
    }

    public static boolean closeProperly(ExecutorService executor) throws InterruptedException {
        throw new UnsupportedOperationException("TODO 7 : implementer closeProperly()");
    }

    public static String reminderIn50ms() throws Exception {
        throw new UnsupportedOperationException("TODO 8 : implementer reminderIn50ms()");
    }

    public static String failureCause(ExecutorService executor) throws InterruptedException {
        throw new UnsupportedOperationException("TODO 9 : implementer failureCause()");
    }

    public static Set<String> workerNames() throws InterruptedException {
        throw new UnsupportedOperationException("TODO 10 : implementer workerNames()");
    }

    public static void main(String[] args) throws Exception {
        AtomicBoolean ran = new AtomicBoolean();
        runInThread(() -> ran.set(true));
        ExerciseChecker.check("1  runInThread attend la fin", ran.get());
        ExerciseChecker.check("2  totalDaysTwoThreads == 134", totalDaysTwoThreads() == 134);
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try {
            ExerciseChecker.check("3  loanCount == 12", loanCount(pool) == 12);
            ExerciseChecker.check("4  totalDaysInvokeAll == 134", totalDaysInvokeAll(pool) == 134);
            ExerciseChecker.check("5  anyTitle est un des 4 titres", Set.of("Dune", "Fondation", "Hyperion", "Solaris").contains(anyTitle(pool)));
            ExerciseChecker.check("9  failureCause == IOException", "IOException".equals(failureCause(pool)));
        } finally {
            pool.shutdownNow();
        }
        ExerciseChecker.check("6  countWithExecute == 12", countWithExecute(Executors.newFixedThreadPool(3)) == 12);
        ExecutorService toClose = Executors.newSingleThreadExecutor();
        toClose.submit(() -> { });
        ExerciseChecker.check("7  closeProperly == true et l'executor est termine", closeProperly(toClose) && toClose.isTerminated());
        ExerciseChecker.check("8  reminderIn50ms == rappel envoye", "rappel envoye".equals(reminderIn50ms()));
        Set<String> names = workerNames();
        ExerciseChecker.check("10 workerNames : 1 ou 2 threads seulement (pool de 2)", !names.isEmpty() && names.size() <= 2);
        ExerciseChecker.check("   Loans.LOANS intact", Loans.LOANS.size() == 12);

        ExerciseChecker.summary();
    }
}
