package ch9_collections.drills.solutions;

import ch9_collections.drills.Pantry;
import ch9_collections.drills.Pantry.Item;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Corrige du drill 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch9_collections.drills.exercises.Drill06_MixedKata.
 */
public class SolutionDrill06_MixedKata {

    public static String mostExpensive() {
        // Collections.max sur les entrees, compare par valeur : Map.Entry.comparingByValue() est deja pret.
        return Collections.max(Pantry.prices().entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    public static Set<String> cheaperThan(int max) {
        // TreeSet pour la sortie triee ; le filtre se fait en parcourant entrySet.
        Set<String> result = new TreeSet<>();
        for (Map.Entry<String, Integer> e : Pantry.prices().entrySet()) {
            if (e.getValue() < max) {
                result.add(e.getKey());
            }
        }
        return result;
    }

    public static Set<String> duplicates() {
        // Set.add rend false au 2e passage : c'est le detecteur de doublon.
        Set<String> seen = new TreeSet<>();
        Set<String> dup = new TreeSet<>();
        for (String f : Pantry.FRUITS) {
            if (!seen.add(f)) {
                dup.add(f);
            }
        }
        return dup;
    }

    public static String firstUnique() {
        // On compte d'abord (merge), puis on reparcourt la liste dans l'ordre d'origine.
        Map<String, Integer> counts = new TreeMap<>();
        for (String f : Pantry.FRUITS) {
            counts.merge(f, 1, Integer::sum);
        }
        for (String f : Pantry.FRUITS) {
            if (counts.get(f) == 1) {
                return f;
            }
        }
        return null;
    }

    public static List<Integer> topTwo() {
        // Une file de priorite inversee rend le plus grand a chaque poll.
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        pq.addAll(Pantry.NUMBERS);
        return List.of(pq.poll(), pq.poll());
    }

    public static int basketTotal(List<String> basket) {
        // getOrDefault : un fruit inconnu ne fait ni null ni NullPointerException.
        Map<String, Integer> prices = Pantry.prices();
        int total = 0;
        for (String f : basket) {
            total += prices.getOrDefault(f, 0);
        }
        return total;
    }

    public static List<String> replay(List<String> actions) {
        // Une pile : undo retire le dernier ; pollFirst (et pas pop) ne plante pas sur une pile vide.
        Deque<String> stack = new ArrayDeque<>();
        for (String a : actions) {
            if (a.equals("undo")) {
                stack.pollFirst();
            } else {
                stack.push(a);
            }
        }
        List<String> result = new ArrayList<>(stack);
        Collections.reverse(result);
        return result;
    }

    public static Map<String, List<String>> priceBands() {
        // TreeMap pour les cles triees, computeIfAbsent pour creer chaque tranche a la demande.
        Map<String, List<String>> bands = new TreeMap<>();
        Pantry.prices().forEach((name, price) ->
                bands.computeIfAbsent(price <= 2 ? "bas" : "haut", k -> new ArrayList<>()).add(name));
        return bands;
    }

    public static int rankByPrice(String fruit) {
        // Trier une copie des items, puis chercher la position (index + 1).
        List<Item> sorted = new ArrayList<>(Pantry.items());
        sorted.sort(Comparator.comparingInt(Item::price));
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i).name().equals(fruit)) {
                return i + 1;
            }
        }
        return -1;
    }

    public static Map<String, Integer> mergeBaskets(Map<String, Integer> a, Map<String, Integer> b) {
        // On copie a (Map.of est immuable) dans un TreeMap, puis merge additionne ce qui vient de b.
        Map<String, Integer> result = new TreeMap<>(a);
        b.forEach((k, v) -> result.merge(k, v, Integer::sum));
        return result;
    }
}
