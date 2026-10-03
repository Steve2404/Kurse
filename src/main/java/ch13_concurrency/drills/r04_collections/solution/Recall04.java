package ch13_concurrency.drills.r04_collections.solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * SOLUTION du drill de rappel 4 - les collections concurrentes.
 */
public class Recall04 {

    static String removeDuringLoop(Integer target) {
        List<Integer> plain = new ArrayList<>(List.of(1, 2, 3));
        try {
            for (Integer i : plain) {
                if (i.equals(target)) {
                    plain.remove(i);
                }
            }
            return "ok" + plain;
        } catch (ConcurrentModificationException e) {
            return e.getClass().getSimpleName();
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String, Integer> counts = new ConcurrentHashMap<>();
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try {
            for (int t = 0; t < 4; t++) {
                pool.submit(() -> {
                    for (String w : "a b a c b a".split(" ")) {
                        counts.merge(w, 1, Integer::sum);
                    }
                });
            }
        } finally {
            pool.shutdown();
        }
        pool.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("D01 : " + new TreeMap<>(counts));

        String npe;
        try {
            counts.put("x", null);
            npe = "ok";
        } catch (NullPointerException e) {
            npe = e.getClass().getSimpleName();
        }
        Map<String, Integer> sync = Collections.synchronizedMap(new HashMap<>());
        sync.put("k", null);
        System.out.println("D02 : " + npe + " " + sync.containsKey("k") + " " + counts.getOrDefault("z", 0) + " " + counts.putIfAbsent("a", 99) + " "
                + counts.computeIfAbsent("z", k -> 1));

        String cme = removeDuringLoop(1);                       // retirer le 1er : la boucle continue et detecte la modification
        String trap = removeDuringLoop(2);                      // retirer l'AVANT-DERNIER : hasNext() rend false, aucune exception !
        List<Integer> cow = new CopyOnWriteArrayList<>(List.of(1, 2, 3));
        for (Integer i : cow) {
            if (i == 2) {
                cow.remove(i);
            }
        }
        CopyOnWriteArraySet<String> cowSet = new CopyOnWriteArraySet<>(List.of("b", "a", "b"));
        System.out.println("D03 : " + cme + " " + trap + " " + cow + " " + cowSet);

        ConcurrentSkipListMap<Integer, String> skip = new ConcurrentSkipListMap<>(Map.of(30, "c", 10, "a", 20, "b"));
        ConcurrentSkipListSet<String> skipSet = new ConcurrentSkipListSet<>(List.of("pomme", "kiwi", "abricot"));
        System.out.println("D04 : " + skip + " " + skip.firstKey() + " " + skip.headMap(25) + " " + skip.ceilingKey(15) + " " + skipSet + " " + skipSet.last());

        BlockingQueue<String> q = new LinkedBlockingQueue<>(2);
        q.put("x");
        boolean added = q.offer("y");
        boolean full = q.offer("z", 10, TimeUnit.MILLISECONDS);
        String taken = q.take();
        String polled = q.poll(10, TimeUnit.MILLISECONDS);
        String empty = q.poll(10, TimeUnit.MILLISECONDS);
        System.out.println("D05 : " + added + " " + full + " " + taken + " " + polled + " " + empty);

        BlockingDeque<Integer> deque = new LinkedBlockingDeque<>();
        deque.offerFirst(2);
        deque.offerFirst(1);
        deque.offerLast(3);
        deque.putLast(4);
        ConcurrentLinkedDeque<Integer> cld = new ConcurrentLinkedDeque<>(List.of(1, 2, 3));
        System.out.println("D06 : " + deque + " " + deque.pollLast() + " " + deque.takeFirst() + " " + cld.pollLast() + " " + cld.peekFirst());
    }
}
