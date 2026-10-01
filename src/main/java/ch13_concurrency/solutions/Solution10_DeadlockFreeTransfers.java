package ch13_concurrency.solutions;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.exercises.Exercise10_DeadlockFreeTransfers.
 */
public class Solution10_DeadlockFreeTransfers {

    public static class Account {
        public final int id;
        public int balance;
        public final ReentrantLock lock = new ReentrantLock();

        public Account(int id, int balance) {
            this.id = id;
            this.balance = balance;
        }
    }

    public static void transfer(Account from, Account to, int amount) {
        // Ordre de verrouillage FIXE (plus petit id d'abord) : deux virements inverses prennent les verrous
        // dans le meme ordre, donc aucun cercle d'attente n'est possible.
        Account first = from.id < to.id ? from : to;
        Account second = first == from ? to : from;
        synchronized (first) {
            synchronized (second) {
                from.balance -= amount;
                to.balance += amount;
            }
        }
    }

    public static boolean tryTransfer(Account from, Account to, int amount, long millis) throws InterruptedException {
        // tryLock avec delai : on abandonne au lieu d'attendre pour toujours ; unlock seulement ce qu'on a obtenu.
        if (from.lock.tryLock(millis, TimeUnit.MILLISECONDS)) {
            try {
                if (to.lock.tryLock(millis, TimeUnit.MILLISECONDS)) {
                    try {
                        from.balance -= amount;
                        to.balance += amount;
                        return true;
                    } finally {
                        to.lock.unlock();
                    }
                }
            } finally {
                from.lock.unlock();
            }
        }
        return false;
    }

    public static int safeCount(int threads, int perThread) throws InterruptedException {
        // incrementAndGet fait lire-ajouter-ranger d'un seul coup (atomique) : plus aucune increment perdue.
        AtomicInteger counter = new AtomicInteger();
        Thread[] all = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            all[i] = new Thread(() -> {
                for (int k = 0; k < perThread; k++) {
                    counter.incrementAndGet();
                }
            });
            all[i].start();
        }
        for (Thread t : all) {
            t.join();
        }
        return counter.get();
    }

    public static String diagnose(boolean waitInCircle, boolean busyWithoutProgress, boolean oneNeverServed, boolean resultDependsOnTiming) {
        // Deadlock = on DORT en cercle ; livelock = on s'AGITE sans avancer ; famine = un seul oublie ; race = l'ordre decide du resultat.
        if (waitInCircle) {
            return "DEADLOCK";
        }
        if (busyWithoutProgress) {
            return "LIVELOCK";
        }
        if (oneNeverServed) {
            return "STARVATION";
        }
        return resultDependsOnTiming ? "RACE_CONDITION" : "OK";
    }
}
