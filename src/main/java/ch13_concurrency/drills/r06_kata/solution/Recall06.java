package ch13_concurrency.drills.r06_kata.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * SOLUTION du drill de rappel 6 - kata : les problemes de concurrence et leurs remedes.
 */
public class Recall06 {

    static int unsafe;

    public static void main(String[] args) throws Exception {
        // Course (race condition) : ++ sans protection perd des increments ; le resultat n'est JAMAIS superieur a l'attendu.
        AtomicInteger safe = new AtomicInteger();
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try {
            List<Future<?>> fs = new ArrayList<>();
            for (int t = 0; t < 4; t++) {
                fs.add(pool.submit(() -> {
                    for (int i = 0; i < 50_000; i++) {
                        unsafe++;
                        safe.incrementAndGet();
                    }
                }));
            }
            for (Future<?> f : fs) {
                f.get();
            }
        } finally {
            pool.shutdown();
        }
        pool.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("D01 : atomique " + safe.get() + ", sans protection <= 200000 " + (unsafe <= 200_000));

        // Interblocage : chaque thread tient un verrou et attend celui de l'autre. tryLock avec delai rompt l'attente.
        ReentrantLock left = new ReentrantLock();
        ReentrantLock right = new ReentrantLock();
        CyclicBarrier both = new CyclicBarrier(2);
        ExecutorService duo = Executors.newFixedThreadPool(2);
        try {
            Future<Boolean> a = duo.submit(() -> grab(left, right, both, 30));
            Future<Boolean> b = duo.submit(() -> grab(right, left, both, 3_000));
            System.out.println("D02 : premier " + a.get() + ", second " + b.get());
        } finally {
            duo.shutdown();
        }

        // Ordre GLOBAL des verrous : plus d'interblocage possible, meme sans delai.
        AtomicInteger moves = new AtomicInteger();
        ExecutorService many = Executors.newFixedThreadPool(4);
        try {
            List<Future<?>> fs = new ArrayList<>();
            for (int t = 0; t < 8; t++) {
                boolean reverse = t % 2 == 0;
                fs.add(many.submit(() -> {
                    for (int i = 0; i < 1_000; i++) {
                        ReentrantLock first = reverse ? right : left;
                        ReentrantLock second = reverse ? left : right;
                        ReentrantLock lo = System.identityHashCode(first) < System.identityHashCode(second) ? first : second;
                        ReentrantLock hi = lo == first ? second : first;
                        lo.lock();
                        try {
                            hi.lock();
                            try {
                                moves.incrementAndGet();
                            } finally {
                                hi.unlock();
                            }
                        } finally {
                            lo.unlock();
                        }
                    }
                }));
            }
            for (Future<?> f : fs) {
                f.get();
            }
        } finally {
            many.shutdown();
        }
        System.out.println("D03 : " + moves.get() + " deplacements, termine " + many.awaitTermination(5, TimeUnit.SECONDS));
    }

    static boolean grab(ReentrantLock mine, ReentrantLock theirs, CyclicBarrier both, long waitMs) throws Exception {
        mine.lock();
        try {
            both.await();
            boolean ok = theirs.tryLock(waitMs, TimeUnit.MILLISECONDS);
            if (ok) {
                theirs.unlock();
            }
            return ok;
        } finally {
            mine.unlock();
        }
    }
}
