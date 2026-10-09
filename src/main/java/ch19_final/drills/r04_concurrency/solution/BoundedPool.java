package ch19_final.drills.r04_concurrency.solution;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/** Un pool borne avec une file bornee : plein, il refuse tout de suite (RejectedExecutionException). */
public final class BoundedPool {

    private BoundedPool() {
    }

    public static ThreadPoolExecutor create(int threads, int capacity) {
        return new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(capacity),
                new ThreadPoolExecutor.AbortPolicy());
    }
}
