package ch13_concurrency.solutions;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Corrige de l'exercice 15. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.exercises.Exercise15_ParallelPipelines.
 */
public class Solution15_ParallelPipelines {

    public static int firstSquareAbove(List<Integer> values, int limit) {
        // findFirst respecte l'ordre de rencontre meme en parallele (findAny pourrait rendre un autre carre).
        return values.parallelStream().map(v -> v * v).filter(v -> v > limit).findFirst().orElse(-1);
    }

    public static String shoutInOrder(List<String> words) {
        // Le map tourne en parallele, mais forEachOrdered livre les elements un par un, dans l'ordre :
        // le StringBuilder (non thread-safe) n'est jamais touche par deux threads a la fois.
        StringBuilder sb = new StringBuilder();
        words.parallelStream().map(String::toUpperCase).forEachOrdered(w -> sb.append(sb.length() > 0 ? "-" : "").append(w));
        return sb.toString();
    }

    public static String sumAndProduct(int n) {
        // L'identite est appliquee a CHAQUE morceau : elle doit etre neutre (0 pour +, 1 pour x).
        int sum = IntStream.rangeClosed(1, n).parallel().reduce(0, Integer::sum);
        long product = IntStream.rangeClosed(1, n).parallel().asLongStream().reduce(1, (a, b) -> a * b);
        return "somme=" + sum + " produit=" + product;
    }

    public static ConcurrentMap<Integer, Long> countByLength(List<String> words) {
        // groupingByConcurrent remplit UNE ConcurrentMap partagee au lieu de fusionner une HashMap par morceau.
        return words.parallelStream().collect(Collectors.groupingByConcurrent(String::length, Collectors.counting()));
    }
}
