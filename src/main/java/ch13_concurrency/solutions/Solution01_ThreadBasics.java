package ch13_concurrency.solutions;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.exercises.Exercise01_ThreadBasics.
 */
public class Solution01_ThreadBasics {

    public static void startAndJoin(Runnable task) throws InterruptedException {
        // start() lance le travail dans un NOUVEAU thread et rend la main tout de suite ; join() attend sa fin reelle.
        Thread thread = new Thread(task);
        thread.start();
        thread.join();
    }

    public static Thread buildInterruptibleWorker(AtomicBoolean interruptedFlag) {
        // interrupt() reveille sleep par une InterruptedException : c'est au thread de decider de s'arreter (ici : drapeau + return).
        return new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    interruptedFlag.set(true);
                    return;
                }
            }
        });
    }
}
