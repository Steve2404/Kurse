package concurrency.exercises;

import concurrency.ExerciseChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * EXERCICE 2 - Callable, ExecutorService et Future (niveau : difficile)
 * ==================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ThreadBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Runnable (Exercise01) fait un travail SANS jamais rien rendre, et ne
 * peut jamais lancer d'exception checked. Callable<T> est son cousin
 * plus puissant : il REND un resultat de type T, ET peut lancer une
 * exception checked. Un ExecutorService est une "equipe d'ouvriers"
 * qui recoit des taches et les distribue toute seule aux ouvriers
 * disponibles - tu n'as jamais besoin de fabriquer les Thread toi-
 * meme. submit(callable) rend IMMEDIATEMENT un Future<T> : "un recu"
 * qui promet le resultat FUTUR, pas encore forcement pret. future.get()
 * attend que le resultat soit VRAIMENT pret, puis le rend.
 *
 *
 * ==================================================================
 * TODO 1 : computeWithExecutor(executor, task)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Soumettre 'task' a 'executor' (submit()), recuperer le Future
 *      rendu.
 *   2. Renvoyer future.get() (bloque jusqu'a ce que le resultat soit
 *      pret).
 *
 *
 * ==================================================================
 * TODO 2 : computeAllAndSum(executor, tasks)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Si tu appelles future.get() JUSTE APRES chaque submit(), tu attends
 * chaque ouvrier UN PAR UN, dans l'ordre - autant dire qu'ils ne
 * travaillent plus VRAIMENT en meme temps ! La bonne methode : soumettre
 * TOUTES les taches D'ABORD (chaque ouvrier se met au travail tout de
 * suite), et attendre les resultats SEULEMENT APRES, une fois que
 * tout le monde a deja commence.
 *
 * -- Le plan --
 *
 *   1. Soumettre CHAQUE tache de 'tasks' a 'executor', et garder
 *      CHAQUE Future rendu dans une liste - AVANT de lire le moindre
 *      resultat.
 *   2. Une fois TOUTES les taches soumises, parcourir la liste de
 *      Future et additionner tous les future.get().
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en quelques lignes.
 *
 * Exemple a verifier : computeWithExecutor(executor, () -> 42) == 42.
 * computeAllAndSum(executor, [() -> 1, () -> 2, () -> 3]) == 6.
 * Une tache qui lance une IOException checked, une fois soumise et
 * "get()", fait remonter une ExecutionException dont getCause() EST
 * exactement cette IOException (jamais l'IOException directement -
 * Future.get() emballe TOUJOURS l'exception d'origine dans une
 * ExecutionException).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Future<Integer> f = executor.submit(task); ... f.get();
 *   - future.get() peut lancer InterruptedException (checked) ET
 *     ExecutionException (checked, qui EMBALLE la vraie cause).
 *   - N'appelle JAMAIS get() avant d'avoir soumis TOUTES les taches
 *     dans computeAllAndSum() - sinon plus de parallelisme reel.
 */
public class Exercise02_CallableAndFutures {

    public static int computeWithExecutor(ExecutorService executor, Callable<Integer> task)
            throws ExecutionException, InterruptedException {
        throw new UnsupportedOperationException("TODO 1 : implementer computeWithExecutor()");
    }

    public static int computeAllAndSum(ExecutorService executor, List<Callable<Integer>> tasks)
            throws ExecutionException, InterruptedException {
        throw new UnsupportedOperationException("TODO 2 : implementer computeAllAndSum()");
    }

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        ExerciseChecker.check("computeWithExecutor(() -> 42) == 42",
                computeWithExecutor(executor, () -> 42) == 42);

        List<Callable<Integer>> tasks = new ArrayList<>();
        tasks.add(() -> 1);
        tasks.add(() -> 2);
        tasks.add(() -> 3);
        ExerciseChecker.check("computeAllAndSum([1,2,3]) == 6", computeAllAndSum(executor, tasks) == 6);

        Future<Integer> failingFuture = executor.submit(() -> {
            throw new IOException("panne simulee");
        });
        boolean wrapped = false;
        try {
            failingFuture.get();
        } catch (ExecutionException e) {
            wrapped = e.getCause() instanceof IOException && "panne simulee".equals(e.getCause().getMessage());
        }
        ExerciseChecker.check("ExecutionException.getCause() est bien l'IOException d'origine", wrapped);

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        ExerciseChecker.summary();
    }
}
