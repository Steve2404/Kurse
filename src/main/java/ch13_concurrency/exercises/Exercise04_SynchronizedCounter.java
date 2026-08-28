package ch13_concurrency.exercises;

import ch13_concurrency.ExerciseChecker;

/**
 * EXERCICE 4 - Proteger un compteur partage avec synchronized (niveau : difficile)
 * ===============================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ThreadBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "value++" a l'air d'une seule etape, mais c'est en realite TROIS
 * petites etapes cachees : (1) LIRE la valeur actuelle, (2) calculer
 * valeur+1, (3) RANGER ce nouveau nombre. Si DEUX ouvriers font ca EN
 * MEME TEMPS sur le MEME compteur, ils peuvent tous les deux LIRE la
 * meme valeur de depart AVANT que l'un d'eux ait fini de RANGER son
 * resultat - un des deux incrementations est alors PERDUE (le
 * compteur final est plus petit que prevu). C'est une "race
 * condition" (rappel du chapitre : 2 threads qui se marchent sur les
 * pieds, resultat imprevisible).
 *
 * Le mot-cle synchronized transforme une methode (ou un bloc) en
 * "piece a un seul carrelage" (moniteur) : UN SEUL thread a la fois
 * peut y entrer ; tous les autres attendent leur tour a la porte,
 * meme s'ils sont "prets" en meme temps.
 *
 *
 * ==================================================================
 * TODO : completer Counter (increment() et get())
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. increment() : marquer la methode 'synchronized', et faire
 *      value++ dedans.
 *   2. get() : marquer la methode 'synchronized' AUSSI (meme une
 *      simple LECTURE doit passer par le meme moniteur, sinon rien ne
 *      garantit qu'un thread voit la toute derniere valeur ecrite par
 *      un autre).
 *
 * Exemple a verifier : 8 threads incrementent le MEME Counter 10 000
 * fois chacun (80 000 incrementations au total). Une fois TOUS les
 * threads termines (join()), counter.get() DOIT valoir EXACTEMENT
 * 80000 - pas 79 998, pas 80 001 : synchronized garantit qu'AUCUNE
 * incrementation n'est jamais perdue, meme sous forte concurrence.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : Counter a deja ses 2 methodes dediees, chacune tient en une
 * ligne une fois "synchronized" ajoute.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - synchronized void increment() { value++; }
 *   - synchronized int get() { return value; }
 *   - Les 2 methodes synchronized sur la MEME instance partagent le
 *     MEME moniteur (celui de l'objet Counter lui-meme) : un thread
 *     dans increment() bloque bien un autre thread qui voudrait
 *     entrer dans get() en meme temps, et inversement.
 */
public class Exercise04_SynchronizedCounter {

    static class Counter {
        private int value;

        void increment() {
            throw new UnsupportedOperationException("TODO : implementer increment()");
        }

        int get() {
            throw new UnsupportedOperationException("TODO : implementer get()");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        int threadCount = 8;
        int incrementsPerThread = 10_000;

        Thread[] threads = new Thread[threadCount];
        for (int t = 0; t < threadCount; t++) {
            threads[t] = new Thread(() -> {
                for (int i = 0; i < incrementsPerThread; i++) {
                    counter.increment();
                }
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        ExerciseChecker.check("aucune incrementation perdue : " + threadCount + " x " + incrementsPerThread + " == "
                        + (threadCount * incrementsPerThread),
                counter.get() == threadCount * incrementsPerThread);

        ExerciseChecker.summary();
    }
}
