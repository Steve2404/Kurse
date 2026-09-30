package ch9_collections.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Corrige de l'exercice 8.
 */
public class Solution08_MapApiWorkout {

    public static Map<String, Integer> countWords(List<String> words) {
        // merge remplace "get, tester null, put" : 1 si absent, sinon ancien + 1.
        Map<String, Integer> counts = new TreeMap<>();
        for (String w : words) {
            counts.merge(w, 1, Integer::sum);
        }
        return counts;
    }

    public static Map<Integer, List<String>> groupByLength(List<String> words) {
        // computeIfAbsent rend la liste (nouvelle OU existante) : on peut enchainer add directement.
        Map<Integer, List<String>> groups = new TreeMap<>();
        for (String w : words) {
            groups.computeIfAbsent(w.length(), k -> new ArrayList<>()).add(w);
        }
        return groups;
    }

    public static Map<String, Integer> sell(Map<String, Integer> stock, String item, int quantity) {
        // computeIfPresent ne touche pas un article absent ; rendre null supprime le tiroir.
        stock.computeIfPresent(item, (k, v) -> v - quantity <= 0 ? null : v - quantity);
        return stock;
    }

    public static Integer returned(String method, Integer old) {
        // put, putIfAbsent, remove, replace rendent l'ANCIENNE valeur ; compute... et merge rendent la NOUVELLE.
        switch (method) {
            case "put":
            case "putIfAbsent":
            case "remove":
            case "replace":
                return old;
            case "getOrDefault":
                return old != null ? old : 0;
            default:
                return stored(method, old);
        }
    }

    public static Integer stored(String method, Integer old) {
        // replace et computeIfPresent n'ajoutent jamais ; merge sur un tiroir vide ne consulte pas la fonction.
        switch (method) {
            case "put":
                return 5;
            case "putIfAbsent":
            case "computeIfAbsent":
                return old != null ? old : 5;
            case "getOrDefault":
                return old;
            case "remove":
                return null;
            case "replace":
                return old != null ? 5 : null;
            case "computeIfPresent":
                return old != null ? old * 10 : null;
            case "merge":
                return old != null ? old + 5 : 5;
            default:
                return old != null ? null : 5;
        }
    }
}
