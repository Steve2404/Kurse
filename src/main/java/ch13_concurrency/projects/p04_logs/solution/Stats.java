package ch13_concurrency.projects.p04_logs.solution;

import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * SOLUTION - les agregats partages par les consommateurs. Chaque structure est concurrente :
 * plusieurs threads peuvent y ecrire en meme temps sans verrou de notre part.
 */
public class Stats {

    // merge est ATOMIQUE sur une ConcurrentHashMap (pas de "lire puis ecrire" a la main).
    final Map<String, Integer> hits = new ConcurrentHashMap<>();
    final Map<String, Long> totalMs = new ConcurrentHashMap<>();
    // Versions TRIEES et concurrentes de TreeMap / TreeSet.
    final NavigableMap<Integer, Integer> statuses = new ConcurrentSkipListMap<>();
    final NavigableSet<String> errorUsers = new ConcurrentSkipListSet<>();
    // File non bloquante, sans limite.
    final Queue<String> slow = new ConcurrentLinkedQueue<>();

    public void record(String line) {
        String user = null;
        String page = null;
        int ms = 0;
        int status = 0;
        for (String part : line.split(" ")) {
            String[] kv = part.split("=");
            switch (kv[0]) {
                case "user" -> user = kv[1];
                case "page" -> page = kv[1];
                case "ms" -> ms = Integer.parseInt(kv[1]);
                default -> status = Integer.parseInt(kv[1]);
            }
        }
        hits.merge(page, 1, Integer::sum);
        totalMs.merge(page, (long) ms, Long::sum);
        statuses.merge(status, 1, Integer::sum);
        if (status == 500) {
            errorUsers.add(user);
        }
        if (ms > 950) {
            slow.offer(ms + " ms " + user + " " + page);
        }
    }
}
