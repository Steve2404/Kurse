package ch9_collections.projects.p04_generics;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON GenericsLab, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "paires : Pair[first=age, second=30] Pair[first=30, second=age] Pair[first=x, second=x] Pair[first=7, second=7] Integer",
            "intervalles : Range[low=13, high=19] true false true false",
            "tas : 1 3 7 11 19 25 30 42 | set map deque lambda generique collection",
            "tri fusion : [map, set, deque, lambda, generique, collection] [1, 3, 7, 11, 19, 25, 30, 42] ; recherche 25 -> 5, 20 -> -6",
            "bornes : max 42 set, argMax collection, somme 138.0 4.0",
            "jokers : [1, 4, 9, 16] [1, 4, 9, 16, texte] | 5 elements : Integer Integer Integer Integer String | [ab, ab, ab]",
            "LRU : [a] [a, b] [a, b, c] [b, c, a] [c, a, d] [a, d, b] [d, b, e] [b, e, a] [e, a, c] [e, c, a] -> 2 succes, 8 echecs",
            "transformer : ********* ; effacement : true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "record Pair<A, B>", "public static <X> Pair<X, X> twin(", "record Range<T extends Comparable<T>>", "class Heap<T>",
            "Comparator<? super T>", "class Cache<K, V> extends LinkedHashMap<K, V>", "removeEldestEntry(", "interface Transformer<A, B>",
            "default <C> Transformer<A, C> then(", "<T extends Comparable<? super T>> T max(Collection<? extends T>", "Collection<? extends Number>", "List<? super Integer>",
            "static <T> void copy(List<? super T> dst, List<? extends T> src)", "Collection<?>", "<K, V extends Comparable<? super V>>", "Pair.<Integer>twin(",
            "Algos.<Number>copy(",
            // Crescendo : notions des chapitres 10 a 15, interdites au chapitre 9.
            "!.stream(", "!Stream.", "!Stream<", "!Collectors", "!IntStream", "!LongStream", "!DoubleStream", "!.lines()",
            "!Optional", "!.chars()", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.now()",
            "!parallel");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "GenericsLab", args, EXPECTED, API);
    }
}
