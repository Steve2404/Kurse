package concurrency.solutions;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans concurrency.exercises.Exercise05_ReentrantLockAndTryLock.
 */
public class Solution05_ReentrantLockAndTryLock {

    public static class SharedCounter {
        public int value;
    }

    public static void incrementWithLock(ReentrantLock lock, SharedCounter counter) {
        lock.lock();
        try {
            counter.value++;
        } finally {
            lock.unlock();
        }
    }

    public static boolean tryIncrementWithLock(ReentrantLock lock, SharedCounter counter) {
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
