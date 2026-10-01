package ch13_concurrency.solutions;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.exercises.Exercise12_VisitCounter.
 */
public class Solution12_VisitCounter {

    public static <T> void parallelForEach(List<T> items, int threads, Consumer<T> action) throws InterruptedException {
        // Donnee de l'exercice : shutdown + awaitTermination = "attendre que tout soit fini".
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        for (T item : items) {
            pool.submit(() -> action.accept(item));
        }
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);
    }

    public static Map<String, Integer> countVisits(List<String> pages, int threads) throws InterruptedException {
        // merge fait "lire, ajouter, ranger" en UNE operation atomique sur la cle : aucune visite perdue.
        Map<String, Integer> visits = new ConcurrentHashMap<>();
        parallelForEach(pages, threads, page -> visits.merge(page, 1, Integer::sum));
        return visits;
    }

    public static int uniqueVisitors(List<String> visitorIds, int threads) throws InterruptedException {
        // newKeySet : un Set concurrent adosse a une ConcurrentHashMap (il n'existe pas de "ConcurrentHashSet").
        Set<String> seen = ConcurrentHashMap.newKeySet();
        parallelForEach(visitorIds, threads, seen::add);
        return seen.size();
    }

    public static Map<String, Integer> bestScores(List<String> scores, int threads) throws InterruptedException {
        // merge avec Integer::max : garder le maximum est lui aussi atomique, cle par cle.
        Map<String, Integer> best = new ConcurrentHashMap<>();
        parallelForEach(scores, threads, s -> {
            String[] parts = s.split("=");
            best.merge(parts[0], Integer.parseInt(parts[1]), Integer::max);
        });
        return best;
    }

    public static String modifyWhileIterating(String kind) {
        // Les iterateurs de java.util.concurrent sont "faiblement coherents" (ou instantanes pour CopyOnWrite) : jamais de CME.
        // synchronizedList protege chaque appel, mais son iterateur reste "fail-fast" comme celui d'ArrayList.
        return kind.startsWith("CopyOnWrite") || kind.startsWith("Concurrent") ? "OK" : "ConcurrentModificationException";
    }
}
