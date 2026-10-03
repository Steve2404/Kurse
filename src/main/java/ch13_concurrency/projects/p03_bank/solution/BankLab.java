package ch13_concurrency.projects.p03_bank.solution;

import ch13_concurrency.projects.p03_bank.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * SOLUTION du projet 3 - la banque concurrente : synchronized, atomiques, verrous, interblocage evite.
 */
public class BankLab {

    // volatile : l'ecriture d'un thread est VISIBLE tout de suite par les autres (pas de valeur en cache).
    static volatile boolean running = true;

    public static void main(String[] args) throws Exception {
        Bank bank = new Bank(Data.ACCOUNTS, Data.INITIAL);
        ExecutorService pool = Executors.newFixedThreadPool(Data.THREADS);
        try {
            List<Future<?>> done = new ArrayList<>();
            for (int i = 0; i < Data.TRANSFERS; i++) {
                long[] t = Data.transfer(i);
                done.add(pool.submit(() -> bank.transfer((int) t[0], (int) t[1], t[2], Data.FEE)));
            }
            for (Future<?> f : done) {
                f.get();
            }
        } finally {
            pool.shutdown();
        }
        pool.awaitTermination(10, TimeUnit.SECONDS);

        // Les soldes finaux ne dependent PAS de l'ordre des virements (des additions) : on les compare au calcul sequentiel.
        long[] expected = new long[Data.ACCOUNTS];
        Arrays.fill(expected, Data.INITIAL);
        for (int i = 0; i < Data.TRANSFERS; i++) {
            long[] t = Data.transfer(i);
            expected[(int) t[0]] -= t[2] + Data.FEE;
            expected[(int) t[1]] += t[2];
        }
        List<Long> sequential = Arrays.stream(expected).boxed().toList();
        System.out.println("soldes " + bank.balances() + " ; identiques au sequentiel " + bank.balances().equals(sequential));
        System.out.println("conservation : total " + bank.total() + " = " + Data.ACCOUNTS * Data.INITIAL + " " + (bank.total() == Data.ACCOUNTS * Data.INITIAL)
                + " ; " + bank.stats());

        // tryLock : A prend le compte 0 puis veut le 1 (attente courte) ; B prend le 1 puis veut le 0 (attente longue).
        // Sans delai, ce serait un INTERBLOCAGE ; ici A abandonne et relache le 0, donc B termine.
        Account a0 = bank.account(0);
        Account a1 = bank.account(1);
        CyclicBarrier bothHoldFirstLock = new CyclicBarrier(2);
        ExecutorService duo = Executors.newFixedThreadPool(2);
        try {
            Future<String> a = duo.submit(() -> {
                a0.lock().lock();
                try {
                    bothHoldFirstLock.await();
                    boolean ok = a1.lock().tryLock(100, TimeUnit.MILLISECONDS);
                    if (ok) {
                        a1.lock().unlock();
                    }
                    return "A " + (ok ? "reussit" : "abandonne");
                } finally {
                    a0.lock().unlock();
                }
            });
            Future<String> b = duo.submit(() -> {
                a1.lock().lock();
                try {
                    bothHoldFirstLock.await();
                    boolean ok = a0.lock().tryLock(5, TimeUnit.SECONDS);
                    if (ok) {
                        a0.lock().unlock();
                    }
                    return "B " + (ok ? "reussit" : "abandonne");
                } finally {
                    a1.lock().unlock();
                }
            });
            System.out.println("interblocage evite : " + a.get() + ", " + b.get() + " ; virement 2 -> 3 par tryTransfer "
                    + bank.tryTransfer(bank.account(2), bank.account(3), 500, 50));
        } finally {
            duo.shutdown();
        }

        // ReentrantLock : reentrant (le meme thread peut le reprendre), et seul le PROPRIETAIRE peut le rendre.
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        lock.lock();
        int holds = lock.getHoldCount();
        boolean mine = lock.isHeldByCurrentThread();
        lock.unlock();
        lock.unlock();
        String illegal;
        try {
            lock.unlock();
            illegal = "permis";
        } catch (IllegalMonitorStateException e) {
            illegal = e.getClass().getSimpleName();
        }
        System.out.println("reentrance : getHoldCount " + holds + ", isHeldByCurrentThread " + mine + ", isLocked apres 2 unlock " + lock.isLocked()
                + ", unlock de trop " + illegal + ", equitable " + new ReentrantLock(true).isFair());

        // volatile pour arreter proprement une boucle ; AtomicInteger pour compter sans verrou.
        AtomicInteger laps = new AtomicInteger();
        Thread worker = new Thread(() -> {
            while (running) {
                if (laps.incrementAndGet() >= 1_000) {
                    running = false;
                }
            }
        });
        worker.start();
        worker.join();
        System.out.println("volatile : boucle arretee apres " + laps.get() + " tours ; getAndIncrement " + laps.getAndIncrement() + " puis " + laps.get()
                + ", compareAndSet(1001, 0) " + laps.compareAndSet(1001, 0) + " -> " + laps.get() + ", updateAndGet " + laps.updateAndGet(v -> v + 10));
    }
}
