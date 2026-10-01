package ch13_concurrency.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Corrige de l'exercice 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.exercises.Exercise03_CallableAndFutures.
 */
public class Solution03_CallableAndFutures {

    public static int computeWithExecutor(ExecutorService executor, Callable<Integer> task)
            throws ExecutionException, InterruptedException {
        // Callable rend une valeur et peut lancer une checked ; get() attend, et emballe toute exception dans ExecutionException.
        Future<Integer> future = executor.submit(task);
        return future.get();
    }

    public static int computeAllAndSum(ExecutorService executor, List<Callable<Integer>> tasks)
            throws ExecutionException, InterruptedException {
        // On soumet TOUT d'abord (les taches tournent en parallele), puis on collecte : un get() dans la 1re boucle serialiserait tout.
        List<Future<Integer>> futures = new ArrayList<>();
        for (Callable<Integer> task : tasks) {
            futures.add(executor.submit(task));
        }
        int sum = 0;
        for (Future<Integer> future : futures) {
            sum += future.get();
        }
        return sum;
    }
}
