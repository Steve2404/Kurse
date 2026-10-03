package ch13_concurrency.drills.r07_bonus.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.Semaphore;

/**
 * SOLUTION du drill de rappel 7 (bonus) - CompletableFuture, Fork/Join, ThreadLocal, Semaphore, CountDownLatch.
 */
public class Recall07 {

    static final ThreadLocal<StringBuilder> BUFFER = ThreadLocal.withInitial(StringBuilder::new);

    public static void main(String[] args) throws Exception {
        int a = CompletableFuture.supplyAsync(() -> 20).thenApply(n -> n + 1).thenApply(n -> n * 2).join();
        int b = CompletableFuture.supplyAsync(() -> 3).thenCombine(CompletableFuture.supplyAsync(() -> 4), (x, y) -> x * y).join();
        System.out.println("D01 : " + a + " " + b);

        // thenApply avec une fonction qui rend une future donnerait CompletableFuture<CompletableFuture<..>> ; thenCompose aplatit.
        CompletableFuture<String> composed = CompletableFuture.supplyAsync(() -> "id-7").thenCompose(id -> CompletableFuture.supplyAsync(() -> "profil de " + id));
        System.out.println("D02 : " + composed.join());

        CompletableFuture<Integer> broken = CompletableFuture.supplyAsync(() -> Integer.parseInt("x"));
        String recovered = broken.exceptionally(e -> -1).join().toString();
        String handled = broken.handle((v, e) -> e != null ? "erreur " + e.getCause().getClass().getSimpleName() : "ok").join();
        List<String> seen = new ArrayList<>();
        String viaJoin;
        try {
            broken.whenComplete((v, e) -> seen.add(e == null ? "valeur" : "exception")).join();
            viaJoin = "ok";
        } catch (CompletionException e) {
            viaJoin = e.getClass().getSimpleName();
        }
        System.out.println("D03 : " + recovered + " | " + handled + " | " + seen + " " + viaJoin);

        List<CompletableFuture<Integer>> squares = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            int n = i;
            squares.add(CompletableFuture.supplyAsync(() -> n * n));
        }
        CompletableFuture.allOf(squares.toArray(new CompletableFuture[0])).join();
        System.out.println("D04 : " + squares.stream().mapToInt(CompletableFuture::join).sum() + " " + squares.stream().allMatch(CompletableFuture::isDone));

        long sum = ForkJoinPool.commonPool().invoke(new SumTask(1, 1_000_000));
        System.out.println("D05 : " + sum + " " + (sum == 1_000_000L * 1_000_001 / 2));

        BUFFER.get().append("main");
        StringBuilder[] other = new StringBuilder[1];
        Thread t = new Thread(() -> other[0] = BUFFER.get().append("autre"));
        t.start();
        t.join();
        System.out.println("D06 : " + BUFFER.get() + " " + other[0] + " " + (BUFFER.get() != other[0]));

        Semaphore s = new Semaphore(3);
        boolean two = s.tryAcquire(2);
        boolean twoMore = s.tryAcquire(2);
        int left = s.availablePermits();
        s.release(2);
        CountDownLatch latch = new CountDownLatch(2);
        latch.countDown();
        long mid = latch.getCount();
        latch.countDown();
        latch.countDown();                                    // deja a 0 : sans effet
        System.out.println("D07 : " + two + " " + twoMore + " " + left + " " + s.availablePermits() + " | " + mid + " " + latch.getCount());
    }
}

// Somme de [from, to] : on coupe en deux tant que l'intervalle est grand.
class SumTask extends RecursiveTask<Long> {
    private final long from;
    private final long to;

    SumTask(long from, long to) {
        this.from = from;
        this.to = to;
    }

    @Override
    protected Long compute() {
        if (to - from < 10_000) {
            long s = 0;
            for (long i = from; i <= to; i++) {
                s += i;
            }
            return s;
        }
        long mid = (from + to) / 2;
        SumTask left = new SumTask(from, mid);
        left.fork();
        return new SumTask(mid + 1, to).compute() + left.join();
    }
}
