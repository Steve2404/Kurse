package ch13_concurrency.projects.p04_logs.solution;

import ch13_concurrency.projects.p04_logs.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SOLUTION du projet 4 - producteurs / consommateurs avec une file BLOQUANTE, et les collections concurrentes.
 */
public class LogPipeline {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(Data.CAPACITY);   // bornee : put() attend si elle est pleine
        Stats stats = new Stats();
        AtomicInteger consumed = new AtomicInteger();
        ExecutorService producers = Executors.newFixedThreadPool(Data.PRODUCERS);
        ExecutorService consumers = Executors.newFixedThreadPool(Data.CONSUMERS);
        for (int c = 0; c < Data.CONSUMERS; c++) {
            consumers.submit(() -> {
                while (true) {
                    String line = queue.take();                        // attend qu'un element arrive
                    if (line.equals(Data.POISON)) {
                        return null;                                   // la "pilule empoisonnee" arrete le consommateur
                    }
                    stats.record(line);
                    consumed.incrementAndGet();
                }
            });
        }
        int share = Data.LINES / Data.PRODUCERS;
        for (int p = 0; p < Data.PRODUCERS; p++) {
            int from = p * share;
            producers.submit(() -> {
                for (int i = from; i < from + share; i++) {
                    queue.put(Data.line(i));
                }
                return null;
            });
        }
        producers.shutdown();
        producers.awaitTermination(30, TimeUnit.SECONDS);
        for (int c = 0; c < Data.CONSUMERS; c++) {
            queue.put(Data.POISON);                                     // une pilule PAR consommateur
        }
        consumers.shutdown();
        consumers.awaitTermination(30, TimeUnit.SECONDS);

        System.out.println("lignes traitees " + consumed.get() + " / " + Data.LINES + ", file vide " + queue.isEmpty());
        Map<String, Integer> hits = new TreeMap<>(stats.hits);
        StringBuilder avg = new StringBuilder();
        new TreeMap<>(stats.totalMs).forEach((page, ms) -> avg.append(' ').append(page).append('=').append(ms / stats.hits.get(page)));
        System.out.println("visites " + hits + " ; duree moyenne (ms)" + avg);
        List<Map.Entry<String, Integer>> top = new ArrayList<>(stats.hits.entrySet());
        top.sort(Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()));
        System.out.println("page la plus vue " + top.get(0) + " ; statuts " + stats.statuses + " ; erreurs 500 pour " + stats.errorUsers);
        // L'ordre d'arrivee dans la file depend des threads : on trie (duree decroissante, puis texte) avant d'afficher.
        List<String> slow = new ArrayList<>(stats.slow);
        slow.sort(Comparator.comparing((String s) -> Integer.parseInt(s.split(" ")[0])).reversed().thenComparing(Comparator.naturalOrder()));
        System.out.println("requetes lentes " + slow.size() + ", les 3 plus lentes " + slow.subList(0, 3));

        // offer / poll avec delai : ne bloquent que le temps indique.
        BlockingQueue<Integer> tiny = new LinkedBlockingQueue<>(1);
        boolean first = tiny.offer(1, 10, TimeUnit.MILLISECONDS);
        boolean second = tiny.offer(2, 10, TimeUnit.MILLISECONDS);
        Integer polled = tiny.poll(10, TimeUnit.MILLISECONDS);
        Integer empty = tiny.poll(10, TimeUnit.MILLISECONDS);
        System.out.println("file de 1 : offer " + first + " puis " + second + ", poll " + polled + " puis " + empty + ", remainingCapacity " + tiny.remainingCapacity());

        // Modifier une ArrayList pendant qu'on la parcourt : ConcurrentModificationException (meme avec UN seul thread).
        List<String> plain = new ArrayList<>(List.of("a", "b", "c"));
        String cme;
        try {
            for (String s : plain) {
                plain.add(s + "!");
            }
            cme = "pas d'erreur";
        } catch (ConcurrentModificationException e) {
            cme = e.getClass().getSimpleName();
        }
        // CopyOnWriteArrayList : chaque ecriture copie le tableau ; l'iteration lit une PHOTO, sans erreur.
        List<String> cow = new CopyOnWriteArrayList<>(List.of("a", "b", "c"));
        int visited = 0;
        for (String s : cow) {
            cow.add(s + "!");
            visited++;
        }
        System.out.println("ArrayList modifiee en boucle : " + cme + " ; CopyOnWriteArrayList : " + visited + " tours, taille finale " + cow.size() + " " + cow);

        // ConcurrentHashMap refuse null (cle ou valeur) ; HashMap l'accepte.
        Map<String, Integer> chm = new ConcurrentHashMap<>();
        String nullKey;
        try {
            chm.put(null, 1);
            nullKey = "accepte";
        } catch (NullPointerException e) {
            nullKey = e.getClass().getSimpleName();
        }
        Map<String, Integer> hash = new HashMap<>();
        hash.put(null, 1);
        chm.putIfAbsent("x", 1);
        chm.putIfAbsent("x", 2);
        chm.computeIfAbsent("y", k -> 10);
        chm.compute("x", (k, v) -> v * 100);
        List<Integer> synced = Collections.synchronizedList(new ArrayList<>(List.of(3, 1, 2)));
        synchronized (synced) {                                          // iterer une liste synchronisee : verrouiller A LA MAIN
            Collections.sort(synced);
        }
        System.out.println("ConcurrentHashMap.put(null) " + nullKey + ", HashMap.put(null) " + hash.get(null) + " ; x=" + chm.get("x") + " y=" + chm.get("y")
                + " ; synchronizedList " + synced + " ; newKeySet " + ConcurrentHashMap.newKeySet().add("k"));
    }
}
