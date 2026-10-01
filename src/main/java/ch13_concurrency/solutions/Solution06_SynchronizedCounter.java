package ch13_concurrency.solutions;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.exercises.Exercise06_SynchronizedCounter.
 */
public class Solution06_SynchronizedCounter {

    public static class Counter {
        private int value;

        synchronized void increment() {
            // value++ = lire, ajouter, ranger : synchronized rend ces 3 etapes indivisibles (un thread a la fois sur ce moniteur).
            value++;
        }

        synchronized int get() {
            // La lecture aussi est synchronized : elle voit la derniere valeur ecrite (visibilite entre threads).
            return value;
        }
    }
}
