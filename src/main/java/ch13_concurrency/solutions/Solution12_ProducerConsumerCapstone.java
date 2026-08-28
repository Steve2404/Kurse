package ch13_concurrency.solutions;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans concurrency.exercises.Exercise12_ProducerConsumerCapstone.
 */
public class Solution12_ProducerConsumerCapstone {

    static final int POISON_PILL = Integer.MIN_VALUE;

    public static Runnable buildProducer(BlockingQueue<Integer> queue, int itemCount) {
        return () -> {
            try {
                for (int i = 0; i < itemCount; i++) {
                    queue.put(i);
                }
                queue.put(POISON_PILL);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
    }

    public static Runnable buildConsumer(BlockingQueue<Integer> queue, List<Integer> consumed) {
        return () -> {
            try {
                while (true) {
                    int item = queue.take();
                    if (item == POISON_PILL) {
                        return;
                    }
                    consumed.add(item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
    }
}
