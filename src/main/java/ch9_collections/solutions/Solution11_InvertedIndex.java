package ch9_collections.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Corrige de l'exercice 11.
 */
public class Solution11_InvertedIndex {

    public static NavigableMap<String, TreeSet<Integer>> build(List<String> docs) {
        // computeIfAbsent = "prendre ou creer" ; le TreeSet ignore un 2e ajout du meme numero.
        NavigableMap<String, TreeSet<Integer>> index = new TreeMap<>();
        for (int i = 0; i < docs.size(); i++) {
            for (String word : docs.get(i).toLowerCase().split(" ")) {
                index.computeIfAbsent(word, k -> new TreeSet<>()).add(i);
            }
        }
        return index;
    }

    public static TreeSet<Integer> searchAll(NavigableMap<String, TreeSet<Integer>> index, List<String> words) {
        // On copie avant retainAll : modifier l'ensemble de l'index le casserait pour les recherches suivantes.
        if (words.isEmpty()) {
            return new TreeSet<>();
        }
        TreeSet<Integer> result = new TreeSet<>(docsOf(index, words.get(0)));
        for (String w : words.subList(1, words.size())) {
            result.retainAll(docsOf(index, w));
        }
        return result;
    }

    public static TreeSet<Integer> searchAny(NavigableMap<String, TreeSet<Integer>> index, List<String> words) {
        // addAll dans un NOUVEL ensemble : l'index reste intact.
        TreeSet<Integer> result = new TreeSet<>();
        for (String w : words) {
            result.addAll(docsOf(index, w));
        }
        return result;
    }

    public static List<String> wordsWithPrefix(NavigableMap<String, TreeSet<Integer>> index, String prefix) {
        // Les cles etant triees, les mots au meme prefixe forment une tranche contigue : subMap la decoupe sans tout parcourir.
        return new ArrayList<>(index.subMap(prefix, true, prefix + '￿', false).keySet());
    }

    public static String mostCommonWord(NavigableMap<String, TreeSet<Integer>> index) {
        // Parcours dans l'ordre alphabetique + remplacement seulement si STRICTEMENT mieux = egalite gagnee par le premier mot.
        String best = null;
        int bestSize = -1;
        for (Map.Entry<String, TreeSet<Integer>> e : index.entrySet()) {
            if (e.getValue().size() > bestSize) {
                best = e.getKey();
                bestSize = e.getValue().size();
            }
        }
        return best;
    }

    private static TreeSet<Integer> docsOf(NavigableMap<String, TreeSet<Integer>> index, String word) {
        // Boite magique commune a l'ET et au OU : un mot absent se comporte comme "aucun document".
        return index.getOrDefault(word, new TreeSet<>());
    }
}
