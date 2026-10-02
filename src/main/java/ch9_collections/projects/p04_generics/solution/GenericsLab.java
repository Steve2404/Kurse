package ch9_collections.projects.p04_generics.solution;

import ch9_collections.projects.p04_generics.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * SOLUTION du projet 4 - le laboratoire des generiques.
 */
public class GenericsLab {

    public static void main(String[] args) {
        Pair<String, Integer> p = new Pair<>("age", 30);
        Pair<Integer, String> swapped = p.swap();
        Pair<String, String> twin = Pair.twin("x");
        Pair<Integer, Integer> explicit = Pair.<Integer>twin(7);                   // type explicite a l'appel : Pair.<Integer>twin
        System.out.println("paires : " + p + " " + swapped + " " + twin + " " + explicit + " " + swapped.first().getClass().getSimpleName());

        Range<Integer> teen = new Range<>(19, 13);
        Range<String> letters = new Range<>("c", "m");
        System.out.println("intervalles : " + teen + " " + teen.contains(15) + " " + teen.contains(20) + " " + letters.contains("java") + " " + letters.contains("python"));

        List<Integer> numbers = new ArrayList<>();
        for (int n : Data.NUMBERS) {
            numbers.add(n);
        }
        List<String> words = Arrays.asList(Data.WORDS);
        Heap<Integer> minHeap = new Heap<>(Comparator.naturalOrder());
        Heap<String> byLength = new Heap<>(Comparator.comparingInt(String::length).thenComparing(Comparator.reverseOrder()));
        numbers.forEach(minHeap::push);
        words.forEach(byLength::push);
        StringBuilder sorted = new StringBuilder();
        while (!minHeap.isEmpty()) {
            sorted.append(minHeap.pop()).append(' ');
        }
        StringBuilder shortFirst = new StringBuilder();
        while (!byLength.isEmpty()) {
            shortFirst.append(byLength.pop()).append(' ');
        }
        System.out.println("tas : " + sorted.toString().strip() + " | " + shortFirst.toString().strip());

        List<String> byLen = Algos.mergeSort(words, Comparator.comparingInt(String::length));
        List<Integer> sortedNumbers = Algos.mergeSort(numbers, Comparator.naturalOrder());
        System.out.println("tri fusion : " + byLen + " " + sortedNumbers + " ; recherche 25 -> " + Algos.binarySearch(sortedNumbers, 25, Comparator.naturalOrder())
                + ", 20 -> " + Algos.binarySearch(sortedNumbers, 20, Comparator.naturalOrder()));

        Map<String, Integer> lengths = new TreeMap<>();
        for (String w : words) {
            lengths.put(w, w.length());
        }
        System.out.println("bornes : max " + Algos.max(numbers) + " " + Algos.max(words) + ", argMax " + Algos.argMax(lengths) + ", somme "
                + Algos.sum(numbers) + " " + Algos.sum(List.of(1.5, 2.5)));

        List<Number> squares = new ArrayList<>();
        Algos.fillSquares(squares, 4);                       // List<Number> accepte des Integer : ? super Integer
        List<Object> all = new ArrayList<>();
        Algos.<Number>copy(all, squares);                    // T = Number : dst List<Object> (super), src List<Number> (extends)
        all.add("texte");
        System.out.println("jokers : " + squares + " " + all + " | " + Algos.describe(all) + " | " + Algos.repeat("ab", 3));

        Cache<String, Integer> cache = new Cache<>(Data.CAPACITY);
        StringBuilder trace = new StringBuilder();
        for (String key : Data.ACCESSES) {
            cache.load(key, String::length);
            trace.append(cache.keySet()).append(' ');
        }
        System.out.println("LRU : " + trace.toString().strip() + " -> " + cache.stats());

        Transformer<String, Integer> length = String::length;
        Transformer<String, String> stars = length.then(n -> "*".repeat(n));
        System.out.println("transformer : " + stars.transform("generique") + " ; effacement : " + (new ArrayList<String>().getClass() == new ArrayList<Integer>().getClass()));
    }
}
