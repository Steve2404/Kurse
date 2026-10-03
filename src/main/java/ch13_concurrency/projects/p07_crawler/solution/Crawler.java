package ch13_concurrency.projects.p07_crawler.solution;

import ch13_concurrency.projects.p07_crawler.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SOLUTION - le robot : un parcours en LARGEUR, niveau par niveau ; chaque niveau est traite en parallele (invokeAll).
 */
public class Crawler {

    private final ExecutorService pool;
    // newKeySet : un Set concurrent ; add() rend true pour UN SEUL thread, meme si plusieurs ajoutent la meme page.
    private final Set<String> visited = ConcurrentHashMap.newKeySet();
    private final Map<String, Set<String>> index = new ConcurrentHashMap<>();
    private final AtomicInteger fetched = new AtomicInteger();
    private final Set<String> broken = new TreeSet<>();

    public Crawler(ExecutorService pool) {
        this.pool = pool;
    }

    // "Telecharge" une page : indexe ses mots et rend ses liens.
    private List<String> fetch(String page) throws InterruptedException {
        Thread.sleep(1);                                             // la latence du reseau
        fetched.incrementAndGet();
        for (String w : Data.words(page)) {
            index.computeIfAbsent(w, k -> ConcurrentHashMap.newKeySet()).add(page);
        }
        return Data.links(page);
    }

    // Rend le nombre de pages decouvertes a chaque niveau.
    public List<Integer> crawl(String start, int maxDepth) throws InterruptedException {
        List<Integer> perLevel = new ArrayList<>();
        List<String> level = List.of(start);
        visited.add(start);
        for (int depth = 0; depth <= maxDepth && !level.isEmpty(); depth++) {
            perLevel.add(level.size());
            List<Callable<List<String>>> tasks = new ArrayList<>();
            for (String page : level) {
                tasks.add(() -> fetch(page));
            }
            List<String> nextLevel = new ArrayList<>();
            List<String> current = level;
            List<Future<List<String>>> results = pool.invokeAll(tasks);
            for (int i = 0; i < results.size(); i++) {
                try {
                    for (String link : results.get(i).get()) {
                        if (visited.add(link)) {
                            nextLevel.add(link);
                        }
                    }
                } catch (ExecutionException e) {
                    broken.add(current.get(i) + " (" + e.getCause().getMessage() + ")");
                }
            }
            level = nextLevel;
        }
        return perLevel;
    }

    public int fetched() {
        return fetched.get();
    }

    public int visited() {
        return visited.size();
    }

    public Set<String> broken() {
        return broken;
    }

    public Map<String, Set<String>> index() {
        return index;
    }
}
