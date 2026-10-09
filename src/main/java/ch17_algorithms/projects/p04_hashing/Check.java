package ch17_algorithms.projects.p04_hashing;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 * Les tests de reference contiennent des tests de VITESSE.
 */
public class Check {

    /** Les bugs que tes tests doivent attraper (INDICES.md dit, replie, ce que change chacun). */
    static final List<Mutant> MUTANTS = List.of(
            new Mutant("SimpleHashMap.java", "return Math.floorMod(key.hashCode(), length);", "return key.hashCode() % length;"),
            new Mutant("SimpleHashMap.java", "if (n.key.equals(key)) {\n                V old = n.value;", "if (n.key == key) {\n                V old = n.value;"),
            new Mutant("SimpleHashMap.java", "if (size > buckets.length * 3 / 4) {", "if (size > buckets.length) {"),
            new Mutant("SimpleHashMap.java", "                    previous.next = n.next;\n", "                    previous.next = null;\n"),
            new Mutant("SimpleHashMap.java", "                size--;\n", ""),
            new Mutant("LruCache.java", "        unlink(n);\n        appendNewest(n);\n        return n.value;", "        return n.value;"),
            new Mutant("LruCache.java", "Node victim = oldest.next;", "Node victim = newest.prev;"),
            new Mutant("LruCache.java", "            index.remove(victim.key);\n", ""),
            new Mutant("LruCache.java", "            n.value = value;\n            unlink(n);\n            appendNewest(n);\n", "            n.value = value;\n"),
            new Mutant("Hashing.java", "seen.putIfAbsent(a[j], j);", "seen.put(a[j], j);"),
            new Mutant("Hashing.java", "if (counts.get(c) == 1) {", "if (counts.get(c) <= 2) {"),
            new Mutant("Hashing.java", ".reversed().thenComparing(Comparator.naturalOrder())", ".reversed()"),
            new Mutant("Hashing.java", "if (!values.contains(v - 1)) {", "if (true) {"));

    static final List<String> API_CODE = List.of(
            "final class SimpleHashMap<K, V>", "V put(K key, V value)", "V get(Object key)", "V remove(Object key)",
            "int size()", "int capacity()", "Math.floorMod(", ".hashCode()", ".equals(",
            "final class LruCache<K, V>", "LruCache(int capacity)", "V get(K key)", "void put(K key, V value)", "List<K> keysFromOldest()",
            "final class Hashing", "static int[] twoSum(int[] a, int target)",
            "static Map<String, List<String>> groupAnagrams(List<String> words)", "static Character firstUnique(String s)",
            "static List<String> topWords(String text, int k)", "static int longestConsecutive(int[] a)",
            // La table et le cache s'ecrivent a la main.
            "!LinkedHashMap", "!java.util.Hashtable", "!removeEldestEntry");

    static final List<String> API_TESTS = List.of(
            "@Test", "@ParameterizedTest", "assertEquals(", "assertTimeoutPreemptively(", "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 25, MUTANTS, API_CODE, API_TESTS);
    }
}
