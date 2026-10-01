package ch13_concurrency.drills.solutions;

import ch13_concurrency.drills.Loans;
import ch13_concurrency.drills.Loans.Loan;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Corrige du drill 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.drills.exercises.Drill02_SynchronizationTools.
 */
public class SolutionDrill02_SynchronizationTools {

    public static class Tally {
        private int total;

        public synchronized void add(int days) {
            // synchronized : un seul thread a la fois dans les methodes synchronized de CET objet.
            total += days;
        }

        public int total() {
            return total;
        }
    }

    public static void hammer(int threads, int times, Runnable action) throws InterruptedException {
        // Donnee du drill : lancer, puis join() de tous.
        Thread[] all = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            all[i] = new Thread(() -> {
                for (int k = 0; k < times; k++) {
                    action.run();
                }
            });
            all[i].start();
        }
        for (Thread t : all) {
            t.join();
        }
    }

    public static int atomicTotal() throws InterruptedException {
        // Une file concurrente distribue les emprunts ; addAndGet additionne sans verrou ni perte.
        ConcurrentLinkedQueue<Loan> todo = new ConcurrentLinkedQueue<>(Loans.LOANS);
        AtomicInteger total = new AtomicInteger();
        hammer(4, 1, () -> {
            Loan loan;
            while ((loan = todo.poll()) != null) {
                total.addAndGet(loan.days());
            }
        });
        return total.get();
    }

    public static String getVersusIncrement() {
        // getAndX rend l'ANCIENNE valeur, XAndGet la NOUVELLE.
        AtomicInteger n = new AtomicInteger(0);
        int first = n.getAndIncrement();
        int second = n.incrementAndGet();
        return first + " " + second;
    }

    public static String compareAndSet() {
        // CAS : ne change la valeur QUE si elle vaut encore celle attendue ; le 2e echoue car elle vaut deja 7.
        AtomicInteger n = new AtomicInteger(5);
        boolean first = n.compareAndSet(5, 7);
        boolean second = n.compareAndSet(5, 9);
        return first + " " + second + " " + n.get();
    }

    public static int longestLoan() throws InterruptedException {
        // accumulateAndGet(x, Math::max) : "garder le max" fait de facon atomique.
        ConcurrentLinkedQueue<Loan> todo = new ConcurrentLinkedQueue<>(Loans.LOANS);
        AtomicInteger max = new AtomicInteger(Integer.MIN_VALUE);
        hammer(4, 1, () -> {
            Loan loan;
            while ((loan = todo.poll()) != null) {
                max.accumulateAndGet(loan.days(), Math::max);
            }
        });
        return max.get();
    }

    public static int lockedTotal() throws InterruptedException {
        // lock() avant le try, unlock() dans finally : le verrou est toujours rendu.
        ReentrantLock lock = new ReentrantLock();
        int[] value = {0};
        hammer(4, 1000, () -> {
            lock.lock();
            try {
                value[0]++;
            } finally {
                lock.unlock();
            }
        });
        return value[0];
    }

    public static boolean tryOnce(ReentrantLock lock) {
        // tryLock() rend tout de suite ; on ne rend le verrou QUE si on l'a obtenu.
        if (lock.tryLock()) {
            try {
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    public static String holdCounts() {
        // Reentrant : le meme thread peut reprendre un verrou qu'il tient ; il faut autant d'unlock que de lock.
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        lock.lock();
        int afterLocks = lock.getHoldCount();
        lock.unlock();
        lock.unlock();
        return afterLocks + " " + lock.getHoldCount();
    }

    public static int barrierTrips() throws InterruptedException {
        // L'action de la barriere tourne une fois par tour complet (3 threads arrives), puis la barriere se rearme.
        AtomicInteger trips = new AtomicInteger();
        CyclicBarrier barrier = new CyclicBarrier(3, trips::incrementAndGet);
        hammer(3, 2, () -> {
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new IllegalStateException(e);
            }
        });
        return trips.get();
    }

    public static String unlockWithoutLock() {
        // Rendre un verrou qu'on ne tient pas est une erreur de programmation : IllegalMonitorStateException.
        try {
            new ReentrantLock().unlock();
            return "aucune";
        } catch (IllegalMonitorStateException e) {
            return e.getClass().getSimpleName();
        }
    }
}
