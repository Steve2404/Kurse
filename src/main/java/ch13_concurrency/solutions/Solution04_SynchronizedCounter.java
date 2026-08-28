package ch13_concurrency.solutions;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans concurrency.exercises.Exercise04_SynchronizedCounter.
 */
public class Solution04_SynchronizedCounter {

    public static class Counter {
        private int value;

        synchronized void increment() {
            value++;
        }

        synchronized int get() {
            return value;
        }
    }
}
