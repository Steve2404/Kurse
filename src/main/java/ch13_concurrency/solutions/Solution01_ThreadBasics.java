package ch13_concurrency.solutions;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans concurrency.exercises.Exercise01_ThreadBasics.
 */
public class Solution01_ThreadBasics {

    public static void startAndJoin(Runnable task) throws InterruptedException {
        Thread thread = new Thread(task);
        thread.start();
        thread.join();
    }

    public static Thread buildInterruptibleWorker(AtomicBoolean interruptedFlag) {
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
