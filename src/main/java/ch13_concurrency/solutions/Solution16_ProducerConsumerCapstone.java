package ch13_concurrency.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Corrige de l'exercice 16. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.exercises.Exercise16_ProducerConsumerCapstone.
 */
public class Solution16_ProducerConsumerCapstone {

    static final int POISON_PILL = Integer.MIN_VALUE;

    public static Runnable buildProducer(BlockingQueue<Integer> queue, int itemCount) {
        // put() bloque quand la file est pleine (contre-pression) ; la pilule empoisonnee annonce la fin au consommateur.
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
        // take() bloque quand la file est vide ; on s'arrete a la pilule. interrupt() re-pose le drapeau si on est interrompu.
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

    public static List<Integer> runProducerConsumer(int itemCount, int queueCapacity) throws InterruptedException {
        // Donnee de l'exercice : une file BORNEE (put bloque quand elle est pleine) entre deux threads, puis join des deux.
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(queueCapacity);
        List<Integer> consumed = new ArrayList<>();

        Thread producer = new Thread(buildProducer(queue, itemCount));
        Thread consumer = new Thread(buildConsumer(queue, consumed));

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        return consumed;
    }
}
