package ch13_concurrency.exercises;

import ch13_concurrency.ExerciseChecker;

import java.util.concurrent.locks.ReentrantLock;

/**
 * EXERCICE 5 - ReentrantLock et tryLock() (niveau : difficile)
 * =========================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ThreadBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * ReentrantLock fait le MEME travail que synchronized (une "piece a un
 * seul carrelage") mais en version EXPLICITE - toi-meme tu ouvres la
 * porte (lock()) et tu la refermes (unlock()), au lieu que Java le
 * fasse automatiquement autour du bloc. Avantage cle du chapitre :
 * tryLock() permet de VERIFIER si la piece est libre SANS jamais
 * rester bloque a attendre - si elle est deja occupee, tryLock()
 * renvoie false IMMEDIATEMENT, et tu peux faire autre chose plutot que
 * d'attendre.
 *
 * -- Piege a connaitre --
 *
 * lock() et unlock() ne sont PAS automatiques comme avec synchronized
 * : si le code entre les deux lance une exception, unlock() ne sera
 * JAMAIS appele sauf si tu le mets dans un bloc finally - une regle
 * absolue avec ReentrantLock.
 *
 *
 * ==================================================================
 * TODO 1 : incrementWithLock(lock, counter)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. lock.lock().
 *   2. Dans un bloc try : counter.value++.
 *   3. Dans le finally correspondant : lock.unlock() (TOUJOURS,
 *      meme si counter.value++ avait leve une exception).
 *
 *
 * ==================================================================
 * TODO 2 : tryIncrementWithLock(lock, counter)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Si lock.tryLock() reussit (renvoie true) : incrementer
 *      counter.value DANS un try, liberer le verrou dans le finally
 *      correspondant, puis renvoyer true.
 *   2. Sinon (tryLock() a renvoye false, le verrou etait deja pris) :
 *      renvoyer false TOUT DE SUITE, sans jamais attendre.
 *
 * Exemple a verifier : le thread principal prend lui-meme le verrou
 * (lock.lock()) et le GARDE. Un AUTRE thread appelle
 * tryIncrementWithLock() PENDANT ce temps : il doit renvoyer false
 * IMMEDIATEMENT (le compteur ne bouge pas). Une fois le thread
 * principal liberant le verrou (lock.unlock()), un nouvel appel a
 * tryIncrementWithLock() doit cette fois renvoyer true, et le
 * compteur passer a 1.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun est deja sa propre methode.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - lock.lock(); try { ... } finally { lock.unlock(); }
 *   - if (lock.tryLock()) { try { ... return true; } finally { lock.unlock(); } } return false;
 *   - Un MEME ReentrantLock, partage entre plusieurs threads, protege
 *     le MEME SharedCounter partout ou il est utilise - exactement
 *     comme le moniteur de synchronized a l'Exercise04.
 */
public class Exercise05_ReentrantLockAndTryLock {

    static class SharedCounter {
        private int value;
    }

    public static void incrementWithLock(ReentrantLock lock, SharedCounter counter) {
        throw new UnsupportedOperationException("TODO 1 : implementer incrementWithLock()");
    }

    public static boolean tryIncrementWithLock(ReentrantLock lock, SharedCounter counter) {
        throw new UnsupportedOperationException("TODO 2 : implementer tryIncrementWithLock()");
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        SharedCounter counter = new SharedCounter();
        int threadCount = 8;
        int incrementsPerThread = 10_000;

        Thread[] threads = new Thread[threadCount];
        for (int t = 0; t < threadCount; t++) {
            threads[t] = new Thread(() -> {
                for (int i = 0; i < incrementsPerThread; i++) {
                    incrementWithLock(lock, counter);
                }
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        ExerciseChecker.check("aucune incrementation perdue avec ReentrantLock",
                counter.value == threadCount * incrementsPerThread);

        ReentrantLock lock2 = new ReentrantLock();
        SharedCounter counter2 = new SharedCounter();
        lock2.lock();
        boolean[] resultWhileHeld = new boolean[1];
        Thread contender = new Thread(() -> resultWhileHeld[0] = tryIncrementWithLock(lock2, counter2));
        contender.start();
        contender.join();
        ExerciseChecker.check("tryLock() renvoie false immediatement quand le verrou est deja pris",
                !resultWhileHeld[0]);
        ExerciseChecker.check("le compteur n'a pas bouge tant que le verrou etait pris", counter2.value == 0);

        lock2.unlock();
        boolean resultAfterRelease = tryIncrementWithLock(lock2, counter2);
        ExerciseChecker.check("tryLock() reussit une fois le verrou libere", resultAfterRelease);
        ExerciseChecker.check("le compteur est bien passe a 1", counter2.value == 1);

        ExerciseChecker.summary();
    }
}
