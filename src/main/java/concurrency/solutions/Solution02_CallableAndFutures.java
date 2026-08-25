package concurrency.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Corrige de l'exercice 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans concurrency.exercises.Exercise02_CallableAndFutures.
 */
public class Solution02_CallableAndFutures {

    public static int computeWithExecutor(ExecutorService executor, Callable<Integer> task)
            throws ExecutionException, InterruptedException {
        Future<Integer> future = executor.submit(task);
        return future.get();
    }

    public static int computeAllAndSum(ExecutorService executor, List<Callable<Integer>> tasks)
            throws ExecutionException, InterruptedException {
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
