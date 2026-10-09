package ch17_algorithms.projects.p04_hashing.solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/** Les grands classiques du hachage : une HashMap ou un HashSet transforme un O(n^2) en O(n). */
public final class Hashing {

    private Hashing() {
    }

    // Pour chaque case : "ai-je deja vu le complement ?" en O(1). Les indices {i, j}, i < j, de la PREMIERE paire complete.
    public static int[] twoSum(int[] a, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int j = 0; j < a.length; j++) {
            Integer i = seen.get(target - a[j]);
            if (i != null) {
                return new int[]{i, j};
            }
            seen.putIfAbsent(a[j], j);
        }
        return new int[0];
    }

    // Deux anagrammes ont les memes lettres : leur "signature" (les lettres triees) est la meme cle.
    public static Map<String, List<String>> groupAnagrams(List<String> words) {
        Map<String, List<String>> groups = new HashMap<>();
        for (String w : words) {
            char[] letters = w.toCharArray();
            Arrays.sort(letters);
            groups.computeIfAbsent(new String(letters), k -> new ArrayList<>()).add(w);
        }
        return groups;
    }

    // Deux passages sur la CHAINE : compter, puis relire dans l'ordre et rendre le premier compte a 1.
    public static Character firstUnique(String s) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char c : s.toCharArray()) {
            counts.merge(c, 1, Integer::sum);
        }
        for (char c : s.toCharArray()) {
            if (counts.get(c) == 1) {
                return c;
            }
        }
        return null;
    }

    // Les k mots les plus frequents ; a frequence egale, l'ordre alphabetique.
    public static List<String> topWords(String text, int k) {
        Map<String, Integer> counts = new HashMap<>();
        for (String w : text.toLowerCase(Locale.ROOT).split("[^a-z]+")) {
            if (!w.isEmpty()) {
                counts.merge(w, 1, Integer::sum);
            }
        }
        List<String> words = new ArrayList<>(counts.keySet());
        words.sort(Comparator.comparing((String w) -> counts.get(w)).reversed().thenComparing(Comparator.naturalOrder()));
        return words.subList(0, Math.min(k, words.size()));
    }

    // O(n) : on ne compte une suite qu'a partir de son DEBUT (le nombre d'avant est absent).
    public static int longestConsecutive(int[] a) {
        Set<Integer> values = new HashSet<>();
        for (int v : a) {
            values.add(v);
        }
        int best = 0;
        for (int v : values) {
            if (!values.contains(v - 1)) {
                int length = 1;
                while (values.contains(v + length)) {
                    length++;
                }
                best = Math.max(best, length);
            }
        }
        return best;
    }
}
