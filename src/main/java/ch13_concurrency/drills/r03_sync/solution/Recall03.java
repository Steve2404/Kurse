package ch13_concurrency.drills.r03_sync.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * SOLUTION du drill de rappel 3 - synchronized, classes atomiques, verrous, barriere.
 */
public class Recall03 {

    static int counter;
    static int classCounter;

    static synchronized void incrementStatic() {       // verrou : l'objet Class (Recall03.class)
        classCounter++;
    }

    // Lance n taches identiques sur un pool et attend la fin.
    static void runAll(int n, Runnable task) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try {
            List<Future<?>> fs = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                fs.add(pool.submit(task));
            }
            for (Future<?> f : fs) {
                f.get();
            }
        } finally {
            pool.shutdown();
        }
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws Exception {
        Object lock = new Object();
        runAll(4, () -> {
            for (int i = 0; i < 10_000; i++) {
                synchronized (lock) {
                    counter++;
                }
                incrementStatic();
            }
        });
        System.out.println("D01 : " + counter + " " + classCounter);

        AtomicInteger a = new AtomicInteger(5);
        System.out.println("D02 : " + a.incrementAndGet() + " " + a.getAndIncrement() + " " + a.get() + " " + a.addAndGet(10) + " " + a.getAndSet(0) + " "
                + a.compareAndSet(0, 100) + " " + a.compareAndSet(0, 200) + " " + a.updateAndGet(x -> x / 2) + " " + a.accumulateAndGet(3, Math::max));
        AtomicLong total = new AtomicLong();
        runAll(8, () -> {
            for (int i = 0; i < 1_000; i++) {
                total.addAndGet(2);
            }
        });
        AtomicBoolean flag = new AtomicBoolean();
        System.out.println("D03 : " + total.get() + " " + flag.getAndSet(true) + " " + flag.get());

        Lock rl = new ReentrantLock();
        int[] guarded = {0};
        runAll(4, () -> {
            for (int i = 0; i < 5_000; i++) {
                rl.lock();
                try {
                    guarded[0]++;
                } finally {
                    rl.unlock();
                }
            }
        });
        ReentrantLock held = new ReentrantLock();
        held.lock();
        boolean[] other = new boolean[2];
        Thread t = new Thread(() -> {
            other[0] = held.tryLock();
            try {
                other[1] = held.tryLock(10, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                other[1] = true;
            }
        });
        t.start();
        t.join();
        held.unlock();
        System.out.println("D04 : " + guarded[0] + " " + other[0] + " " + other[1] + " " + held.tryLock() + " " + held.getHoldCount());
        held.unlock();

        ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
        rw.readLock().lock();
        rw.readLock().lock();
        int readers = rw.getReadLockCount();
        boolean[] write = new boolean[1];
        Thread writer = new Thread(() -> write[0] = rw.writeLock().tryLock());
        writer.start();
        writer.join();
        rw.readLock().unlock();
        rw.readLock().unlock();
        System.out.println("D05 : " + readers + " " + write[0] + " " + rw.writeLock().tryLock() + " " + rw.isWriteLocked());
        rw.writeLock().unlock();

        AtomicInteger rounds = new AtomicInteger();
        CyclicBarrier barrier = new CyclicBarrier(3, rounds::incrementAndGet);
        runAll(3, () -> {
            try {
                for (int i = 0; i < 4; i++) {
                    barrier.await();
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
        System.out.println("D06 : " + rounds.get() + " " + barrier.getParties() + " " + barrier.isBroken());
    }
}
