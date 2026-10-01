package ch13_concurrency.drills.exercises;

import ch13_concurrency.ExerciseChecker;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * DRILL 02 - synchronized, classes atomiques, ReentrantLock, CyclicBarrier
 * ========================================================================
 *
 * Mode d'emploi : voir Drill01_ThreadsAndExecutors. hammer(threads, fois, action),
 * deja ecrit plus bas, lance threads threads qui executent chacun fois fois l'action,
 * et attend la fin.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : Tally.add(days)         [methode synchronized] ajouter days au total (4 threads x 1000 x 1 -> 4000).
 * TODO 2  : atomicTotal()           [AtomicInteger.addAndGet] ajouter les jours de LOANS depuis 4 threads (un emprunt par appel) -> 134.
 * TODO 3  : getVersusIncrement()    [getAndIncrement contre incrementAndGet] sur un AtomicInteger(0) : "valeurRendue1 valeurRendue2" -> "0 2".
 * TODO 4  : compareAndSet()         [compareAndSet] AtomicInteger(5) : CAS(5, 7), puis CAS(5, 9) ; "resultat1 resultat2 valeurFinale" -> "true false 7".
 * TODO 5  : longestLoan()           [accumulateAndGet] le maximum des jours de LOANS, depuis 4 threads -> 30.
 * TODO 6  : lockedTotal()           [lock() / try / finally unlock()] 4 threads x 1000 increments d'un int protege -> 4000.
 * TODO 7  : tryOnce(lock)           [tryLock() sans attente] rendre true si le verrou est obtenu (et le rendre aussitot), sinon false.
 * TODO 8  : holdCounts()            [reentrance] un meme thread prend le verrou 2 fois : "holdCount apres 2 lock, puis apres 2 unlock" -> "2 0".
 * TODO 9  : barrierTrips()          [CyclicBarrier(3, action)] 3 threads font 2 tours d'await() ; rendre combien de fois l'action a tourne -> 2.
 * TODO 10 : unlockWithoutLock()     [IllegalMonitorStateException] unlock() d'un verrou jamais pris ; rendre le nom simple de l'exception.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   synchronized void m() {...}  /  synchronized (verrou) {...}   (static synchronized : verrou = la classe)
 *   AtomicInteger : get, set, incrementAndGet, getAndIncrement, addAndGet, getAndAdd, compareAndSet(attendu, nouveau),
 *                   updateAndGet(f), accumulateAndGet(x, f) ; AtomicLong, AtomicBoolean pareil
 *   Lock lock = new ReentrantLock(); lock.lock(); try { ... } finally { lock.unlock(); }
 *   tryLock() (immediat) / tryLock(delai, unite) (throws InterruptedException) -> boolean ; unlock seulement si obtenu
 *   ReentrantLock : getHoldCount(), isLocked(), new ReentrantLock(true) = equitable ; unlock sans lock -> IllegalMonitorStateException
 *   CyclicBarrier(n, action) : await() bloque jusqu'a n threads ; l'action tourne a chaque "tour" ; la barriere se rearme
 * ---------------------------------------------------------------------
 */
public class Drill02_SynchronizationTools {

    public static class Tally {
        private int total;

        public void add(int days) {
            throw new UnsupportedOperationException("TODO 1 : implementer add() (synchronized)");
        }

        public int total() {
            return total;
        }
    }

    // Donnee : threads threads executent chacun times fois action, puis on attend la fin de tous.
    public static void hammer(int threads, int times, Runnable action) throws InterruptedException {
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
        throw new UnsupportedOperationException("TODO 2 : implementer atomicTotal()");
    }

    public static String getVersusIncrement() {
        throw new UnsupportedOperationException("TODO 3 : implementer getVersusIncrement()");
    }

    public static String compareAndSet() {
        throw new UnsupportedOperationException("TODO 4 : implementer compareAndSet()");
    }

    public static int longestLoan() throws InterruptedException {
        throw new UnsupportedOperationException("TODO 5 : implementer longestLoan()");
    }

    public static int lockedTotal() throws InterruptedException {
        throw new UnsupportedOperationException("TODO 6 : implementer lockedTotal()");
    }

    public static boolean tryOnce(ReentrantLock lock) {
        throw new UnsupportedOperationException("TODO 7 : implementer tryOnce()");
    }

    public static String holdCounts() {
        throw new UnsupportedOperationException("TODO 8 : implementer holdCounts()");
    }

    public static int barrierTrips() throws InterruptedException {
        throw new UnsupportedOperationException("TODO 9 : implementer barrierTrips()");
    }

    public static String unlockWithoutLock() {
        throw new UnsupportedOperationException("TODO 10 : implementer unlockWithoutLock()");
    }

    public static void main(String[] args) throws Exception {
        Tally tally = new Tally();
        hammer(4, 1000, () -> tally.add(1));
        ExerciseChecker.check("1  Tally (synchronized) : 4 x 1000 -> 4000", tally.total() == 4000);
        ExerciseChecker.check("2  atomicTotal == 134", atomicTotal() == 134);
        ExerciseChecker.check("3  getVersusIncrement == 0 2", "0 2".equals(getVersusIncrement()));
        ExerciseChecker.check("4  compareAndSet == true false 7", "true false 7".equals(compareAndSet()));
        ExerciseChecker.check("5  longestLoan == 30", longestLoan() == 30);
        ExerciseChecker.check("6  lockedTotal == 4000", lockedTotal() == 4000);

        ReentrantLock lock = new ReentrantLock();
        boolean freeOk = tryOnce(lock) && !lock.isLocked();
        CountDownLatch held = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        Thread holder = new Thread(() -> {
            lock.lock();
            try {
                held.countDown();
                release.await();
            } catch (InterruptedException e) {
                // fin
            } finally {
                lock.unlock();
            }
        });
        holder.start();
        held.await();
        boolean busyRefused = !tryOnce(lock);
        release.countDown();
        holder.join();
        ExerciseChecker.check("7  tryOnce : true si libre (et rendu aussitot), false si un autre thread le tient", freeOk && busyRefused);
        ExerciseChecker.check("8  holdCounts == 2 0", "2 0".equals(holdCounts()));
        ExerciseChecker.check("9  barrierTrips == 2", barrierTrips() == 2);
        ExerciseChecker.check("10 unlockWithoutLock == IllegalMonitorStateException", "IllegalMonitorStateException".equals(unlockWithoutLock()));

        ExerciseChecker.summary();
    }
}
