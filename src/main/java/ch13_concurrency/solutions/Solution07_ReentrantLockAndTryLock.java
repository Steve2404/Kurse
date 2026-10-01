package ch13_concurrency.solutions;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.exercises.Exercise07_ReentrantLockAndTryLock.
 */
public class Solution07_ReentrantLockAndTryLock {

    public static class SharedCounter {
        public int value;
    }

    public static void incrementWithLock(ReentrantLock lock, SharedCounter counter) {
        // lock() AVANT le try, unlock() dans finally : le verrou est rendu meme si la section critique lance une exception.
        lock.lock();
        try {
            counter.value++;
        } finally {
            lock.unlock();
        }
    }

    public static boolean tryIncrementWithLock(ReentrantLock lock, SharedCounter counter) {
        // tryLock() n'attend pas : false si le verrou est pris ; on ne fait unlock() QUE si on l'a obtenu.
        if (lock.tryLock()) {
            try {
                counter.value++;
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false;
    }
}
