package ch19_final.drills.r04_concurrency.solution;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Les tests de reference du drill 4 : des verrous, jamais de sleep. */
class Recall04Test {

    private final List<ExecutorService> pools = new ArrayList<>();

    private <T extends ExecutorService> T track(T pool) {
        pools.add(pool);
        return pool;
    }

    @AfterEach
    void stopPools() {
        pools.forEach(ExecutorService::shutdownNow);
        Thread.interrupted();
    }

    @Test
    void d01() {
        List<Duration> sleeps = new ArrayList<>();
        AtomicInteger calls = new AtomicInteger();
        String result = Retry.call(() -> {
            if (calls.incrementAndGet() <= 2) {
                throw new RetryableException("occupe " + calls.get());
            }
            return "SMS-" + calls.get();
        }, 4, Duration.ofMillis(100), sleeps::add);
        assertEquals("SMS-3", result);
        assertEquals(List.of(Duration.ofMillis(100), Duration.ofMillis(200)), sleeps);
        AtomicInteger always = new AtomicInteger();
        RetryableException e = assertThrows(RetryableException.class, () -> Retry.call(() -> {
            throw new RetryableException("occupe " + always.incrementAndGet());
        }, 3, Duration.ofMillis(10), d -> { }));
        assertEquals("occupe 3", e.getMessage());
        assertEquals(List.of("occupe 1", "occupe 2"), Arrays.stream(e.getSuppressed()).map(Throwable::getMessage).toList());
        AtomicInteger once = new AtomicInteger();
        assertThrows(IllegalArgumentException.class, () -> Retry.call(() -> {
            once.incrementAndGet();
            throw new IllegalArgumentException("definitif");
        }, 5, Duration.ofMillis(10), d -> { }));
        assertEquals(1, once.get());
    }

    @Test
    void d02() {
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> Retry.call(() -> {
            throw new RetryableException("occupe");
        }, 3, Duration.ofMillis(10), d -> {
            throw new InterruptedException();
        }));
        assertEquals("interrompu", e.getMessage());
        assertTrue(Thread.currentThread().isInterrupted());
    }

    @Test
    void d03() throws InterruptedException {
        ThreadPoolExecutor pool = track(BoundedPool.create(1, 2));
        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        Runnable blocking = () -> {
            started.countDown();
            try {
                release.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        pool.execute(blocking);
        assertTrue(started.await(5, TimeUnit.SECONDS));
        pool.execute(() -> { });
        pool.execute(() -> { });
        assertThrows(RejectedExecutionException.class, () -> pool.execute(() -> { }));
        release.countDown();
        assertEquals(1, pool.getMaximumPoolSize());
    }

    @Test
    void d04() {
        ExecutorService pool = track(Executors.newFixedThreadPool(2));
        assertEquals("ok:42", Async.withTimeout(() -> "42", pool, Duration.ofSeconds(5)).join());
        assertEquals("erreur:panne", Async.withTimeout(() -> {
            throw new IllegalStateException("panne");
        }, pool, Duration.ofSeconds(5)).join());
        CountDownLatch never = new CountDownLatch(1);
        assertEquals("delai", Async.withTimeout(() -> {
            try {
                never.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "trop tard";
        }, pool, Duration.ofMillis(100)).join());
        never.countDown();
    }

    @Test
    void d05() {
        OnceCache<String, Integer> cache = new OnceCache<>();
        ExecutorService pool = track(Executors.newFixedThreadPool(16));
        CountDownLatch gate = new CountDownLatch(1);
        List<CompletableFuture<Integer>> all = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            all.add(CompletableFuture.supplyAsync(() -> {
                try {
                    gate.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return cache.get("velo", String::length);
            }, pool));
        }
        gate.countDown();
        all.forEach(f -> assertEquals(4, f.join()));
        assertEquals(1, cache.computations());
        assertEquals(3, cache.get("abc", String::length));
        assertEquals(2, cache.computations());
    }

    @Test
    void d06() throws InterruptedException {
        ExecutorService quick = Executors.newFixedThreadPool(2);
        AtomicInteger done = new AtomicInteger();
        for (int i = 0; i < 5; i++) {
            quick.execute(done::incrementAndGet);
        }
        assertEquals(0, Shutdown.stop(quick, Duration.ofSeconds(5)));
        assertEquals(5, done.get());
        ExecutorService slow = Executors.newSingleThreadExecutor();
        CountDownLatch started = new CountDownLatch(1);
        AtomicBoolean interrupted = new AtomicBoolean();
        slow.execute(() -> {
            started.countDown();
            try {
                new CountDownLatch(1).await();
            } catch (InterruptedException e) {
                interrupted.set(true);
            }
        });
        slow.execute(() -> { });
        slow.execute(() -> { });
        assertTrue(started.await(5, TimeUnit.SECONDS));
        assertEquals(2, Shutdown.stop(slow, Duration.ofMillis(100)));
        assertTrue(slow.awaitTermination(5, TimeUnit.SECONDS));
        assertTrue(interrupted.get());
    }
}
