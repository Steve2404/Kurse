package ch9_collections.projects.p02_words.solution;

import ch9_collections.projects.p02_words.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * SOLUTION du projet 2 - les mots : Map, Set et leurs methodes "modernes" (merge, computeIfAbsent, getOrDefault...).
 */
public class Words {

    static List<String> tokens(String text, Set<String> stop) {
        List<String> out = new ArrayList<>();
        for (String w : text.toLowerCase().split("[^a-z]+")) {
            if (!w.isEmpty() && !stop.contains(w)) {
                out.add(w);
            }
        }
        return out;
    }

    public static void main(String[] args) {
        Set<String> stop = new HashSet<>(Arrays.asList(Data.STOP));   // contains en temps constant
        Map<String, Integer> freq = new HashMap<>();
        Map<String, Integer> firstSeen = new LinkedHashMap<>();      // garde l'ordre d'INSERTION
        Map<String, Set<Integer>> index = new TreeMap<>();           // cles triees
        for (int d = 0; d < Data.DOCS.length; d++) {
            for (String w : tokens(Data.DOCS[d], stop)) {
                freq.merge(w, 1, Integer::sum);                       // absent : 1 ; present : ancien + 1
                firstSeen.putIfAbsent(w, d + 1);                      // seulement la premiere fois
                index.computeIfAbsent(w, k -> new TreeSet<>()).add(d + 1);
            }
        }
        System.out.println("mots distincts " + freq.size() + ", java " + freq.get("java") + ", python " + freq.get("python") + ", getOrDefault(python) "
                + freq.getOrDefault("python", 0) + ", containsKey(set) " + freq.containsKey("set") + ", containsValue(4) " + freq.containsValue(4));
        StringBuilder order = new StringBuilder();
        int n = 0;
        for (String w : firstSeen.keySet()) {
            if (n++ < 8) {
                order.append(w).append(' ');
            }
        }
        System.out.println("ordre d'apparition : " + order.toString().strip() + " ...");

        // Top-k : un tas MIN de taille k ; on retire le plus faible des qu'il y a un element de trop.
        Comparator<Map.Entry<String, Integer>> byCount = Map.Entry.comparingByValue();
        Comparator<Map.Entry<String, Integer>> weakestFirst = byCount.thenComparing(Map.Entry.comparingByKey(Comparator.reverseOrder()));
        PriorityQueue<Map.Entry<String, Integer>> heap = new PriorityQueue<>(weakestFirst);
        for (Map.Entry<String, Integer> e : freq.entrySet()) {
            heap.offer(e);
            if (heap.size() > Data.TOP) {
                heap.poll();
            }
        }
        List<Map.Entry<String, Integer>> top = new ArrayList<>(heap);
        top.sort(weakestFirst.reversed());
        System.out.println("top " + Data.TOP + " : " + top);

        // L'index inverse : TreeMap -> les mots dans l'ordre alphabetique ; TreeSet -> les documents tries.
        StringBuilder idx = new StringBuilder("index :");
        for (Map.Entry<String, Set<Integer>> e : index.entrySet()) {
            if (e.getValue().size() > 1) {
                idx.append(' ').append(e.getKey()).append(e.getValue());
            }
        }
        System.out.println(idx);

        // Requetes booleennes : retainAll (et), addAll (ou), removeAll (sauf) sur des COPIES.
        for (String q : Data.QUERIES) {
            String[] p = q.split(" ");
            Set<Integer> result = new TreeSet<>(index.getOrDefault(p[0], Set.of()));
            Set<Integer> other = index.getOrDefault(p[2], Set.of());
            switch (p[1]) {
                case "&" -> result.retainAll(other);
                case "|" -> result.addAll(other);
                default -> result.removeAll(other);
            }
            System.out.println("requete " + q + " -> " + result);
        }

        // Anagrammes : la cle est le mot aux lettres triees.
        Map<String, List<String>> groups = new TreeMap<>();
        for (String w : freq.keySet()) {
            char[] c = w.toCharArray();
            Arrays.sort(c);
            groups.computeIfAbsent(new String(c), k -> new ArrayList<>()).add(w);
        }
        groups.values().removeIf(g -> g.size() < 2);                  // modifier la VUE values modifie la map
        for (List<String> g : groups.values()) {
            g.sort(null);                                              // null : ordre naturel
        }
        System.out.println("anagrammes : " + groups.values());

        // merge qui rend null : la cle est SUPPRIMEE. On decremente jusqu'a disparition.
        Map<String, Integer> stock = new TreeMap<>(Map.of("java", 2, "map", 1));
        stock.merge("java", -1, (old, delta) -> old + delta == 0 ? null : old + delta);
        stock.merge("map", -1, (old, delta) -> old + delta == 0 ? null : old + delta);
        stock.merge("set", 5, Integer::sum);
        stock.compute("java", (k, v) -> v == null ? 1 : v * 10);
        stock.computeIfPresent("set", (k, v) -> v + 1);
        stock.replaceAll((k, v) -> v * 2);
        boolean removed = stock.remove("set", 999);
        System.out.println("merge/compute : " + stock + ", remove(set, 999) " + removed + ", entry " + Map.entry("k", 1) + ", ofEntries "
                + new TreeMap<>(Map.ofEntries(Map.entry("b", 2), Map.entry("a", 1))));

        // Trois Set, trois ordres : HashSet (aucun garanti), LinkedHashSet (insertion), TreeSet (tri).
        List<String> sample = List.of("set", "map", "java", "liste", "map");
        System.out.println("sets : hash " + new HashSet<>(sample).size() + " elements, linked " + new LinkedHashSet<>(sample) + ", tree " + new TreeSet<>(sample)
                + ", add en double " + new HashSet<>(sample).add("map"));
    }
}
