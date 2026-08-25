package concurrency.solutions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans concurrency.exercises.Exercise08_ConcurrentCollections.
 */
public class Solution08_ConcurrentCollections {

    public static Map<String, Integer> countOccurrencesConcurrent(List<String> words, int threadCount)
            throws InterruptedException {
        ConcurrentHashMap<String, Integer> counts = new ConcurrentHashMap<>();
        List<List<String>> chunks = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            chunks.add(new ArrayList<>());
        }
        for (int i = 0; i < words.size(); i++) {
            chunks.get(i % threadCount).add(words.get(i));
        }

        Thread[] threads = new Thread[threadCount];
        for (int t = 0; t < threadCount; t++) {
            List<String> chunk = chunks.get(t);
            threads[t] = new Thread(() -> {
                for (String word : chunk) {
                    counts.merge(word, 1, Integer::sum);
                }
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        return counts;
    }

    public static Iterator<String> buildCopyOnWriteSnapshotIterator(CopyOnWriteArrayList<String> list) {
        return list.iterator();
    }
}
