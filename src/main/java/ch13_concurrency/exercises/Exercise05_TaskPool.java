package ch13_concurrency.exercises;

import ch13_concurrency.ExerciseChecker;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * EXERCICE 5 - Un pool de taches : invokeAny, invokeAll avec delai, shutdown, shutdownNow (niveau : avance)
 * =========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ThreadBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un ExecutorService, c'est une equipe d'ouvriers avec une file de
 * travail. Les questions d'examen portent sur ce qui se passe AUX
 * BORDS : on veut seulement le premier resultat (invokeAny) ; on veut
 * tout, mais pas au-dela d'un delai (invokeAll avec timeout) ; on a
 * ferme la porte (shutdown) ; on veut tout arreter (shutdownNow).
 *
 *   invokeAny(taches)            le resultat d'UNE tache REUSSIE (les autres sont annulees) ;
 *                                si toutes echouent : ExecutionException
 *   invokeAll(taches, delai, u)  attend toutes les taches OU le delai ; celles pas finies sont ANNULEES
 *                                (future.isCancelled() == true, et get() lancerait CancellationException)
 *   shutdown()                   plus de nouvelles taches (RejectedExecutionException), les anciennes finissent
 *   shutdownNow()                interrompt les taches en cours et REND la liste des taches jamais demarrees
 *   awaitTermination(d, u)       attend la fin, au plus d ; rend true si tout est fini
 *
 *
 * ==================================================================
 * TODO 1 : firstSuccess(executor, tasks)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   [echoue, "ok", echoue] -> "ok"
 *
 * -- Le plan --
 *
 *   1. Une seule ligne : invokeAny.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : sumWithin(executor, tasks, millis)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Certaines taches repondent vite, d'autres jamais (elles attendent un
 * signal qui ne vient pas). Rendre "sum=S cancelled=C" : S la somme des
 * resultats finis, C le nombre de taches annulees par le delai.
 *
 * -- Essayons a la main --
 *
 *   [1, 2, bloquee, 4, bloquee] avec 300 ms -> "sum=7 cancelled=2"
 *
 * -- Le plan --
 *
 *   1. invokeAll(tasks, millis, TimeUnit.MILLISECONDS) -> une liste de Future, dans l'ORDRE des taches.
 *   2. Pour chaque Future : isCancelled() -> compter ; sinon additionner get().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : submitAfterShutdown(executor)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. shutdown(), puis submit d'une tache, dans un try.
 *   2. Rendre le nom simple de l'exception attrapee.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : stopEverything(executor, running, interrupted)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * executor n'a qu'UN ouvrier. On lui donne une tache longue (elle
 * attend un signal qui ne vient jamais ; si on l'interrompt, elle leve
 * le drapeau interrupted), puis 3 petites taches qui restent dans la
 * file. Une fois la longue tache DEMARREE (running.await()), appeler
 * shutdownNow() et rendre le NOMBRE de taches jamais demarrees.
 *
 * -- Essayons a la main --
 *
 *   -> 3 (et interrupted passe a true)
 *
 * -- Le plan --
 *
 *   1. submit de la tache longue (qui fait running.countDown(), puis attend, et leve interrupted si on l'interrompt).
 *   2. submit de 3 taches vides.
 *   3. running.await() ; shutdownNow().size() ; awaitTermination pour laisser la longue tache voir l'interruption.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - executor.invokeAny(tasks)
 *   - for (Future<Integer> f : executor.invokeAll(tasks, millis, TimeUnit.MILLISECONDS)) { ... }
 *   - new CountDownLatch(1).await() bloque jusqu'a interruption -> InterruptedException.
 *   - executor.awaitTermination(5, TimeUnit.SECONDS)
 */
public class Exercise05_TaskPool {

    public static String firstSuccess(ExecutorService executor, List<Callable<String>> tasks) throws Exception {
        throw new UnsupportedOperationException("TODO 1 : implementer firstSuccess()");
    }

    public static String sumWithin(ExecutorService executor, List<Callable<Integer>> tasks, long millis) throws Exception {
        throw new UnsupportedOperationException("TODO 2 : implementer sumWithin()");
    }

    public static String submitAfterShutdown(ExecutorService executor) {
        throw new UnsupportedOperationException("TODO 3 : implementer submitAfterShutdown()");
    }

    public static int stopEverything(ExecutorService executor, CountDownLatch running, AtomicBoolean interrupted) throws InterruptedException {
        throw new UnsupportedOperationException("TODO 4 : implementer stopEverything()");
    }

    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        try {
            List<Callable<String>> mixed = List.of(
                    () -> { throw new IllegalStateException("A"); },
                    () -> "ok",
                    () -> { throw new IllegalStateException("C"); });
            ExerciseChecker.check("firstSuccess([echoue, ok, echoue]) == ok", "ok".equals(firstSuccess(pool, mixed)));
        } finally {
            pool.shutdownNow();
        }

        ExecutorService five = Executors.newFixedThreadPool(5);
        CountDownLatch never = new CountDownLatch(1);
        Callable<Integer> blocked = () -> {
            never.await();
            return 1000;
        };
        try {
            String r = sumWithin(five, List.of(() -> 1, () -> 2, blocked, () -> 4, blocked), 300);
            ExerciseChecker.check("sumWithin([1, 2, bloquee, 4, bloquee], 300 ms) == sum=7 cancelled=2 (obtenu : " + r + ")",
                    "sum=7 cancelled=2".equals(r));
        } finally {
            five.shutdownNow();
        }

        ExerciseChecker.check("submitAfterShutdown -> RejectedExecutionException",
                "RejectedExecutionException".equals(submitAfterShutdown(Executors.newSingleThreadExecutor())));

        CountDownLatch running = new CountDownLatch(1);
        AtomicBoolean interrupted = new AtomicBoolean(false);
        ExecutorService single = Executors.newSingleThreadExecutor();
        int pending = stopEverything(single, running, interrupted);
        ExerciseChecker.check("stopEverything -> 3 taches jamais demarrees", pending == 3);
        ExerciseChecker.check("stopEverything -> la tache en cours a ete interrompue, et l'executor est termine",
                interrupted.get() && single.isTerminated());

        ExerciseChecker.summary();
    }
}
