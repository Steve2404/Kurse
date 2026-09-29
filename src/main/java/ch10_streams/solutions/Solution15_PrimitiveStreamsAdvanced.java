package ch10_streams.solutions;

import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Corrige de l'exercice 15. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.exercises.Exercise15_PrimitiveStreamsAdvanced.
 */
public class Solution15_PrimitiveStreamsAdvanced {

    public record Line(int quantity, double unitPrice) {
    }

    public static OptionalDouble averageLength(List<String> words) {
        // Stream<String> n'a pas average() : on passe en IntStream. La boite vient du cas "liste vide".
        return words.stream().mapToInt(String::length).average();
    }

    public static String describeStats(int[] values) {
        // On utilise les getters et PAS toString(), dont le format depend de la langue de la machine.
        // Pour un tableau vide, min vaut Integer.MAX_VALUE et max Integer.MIN_VALUE.
        IntSummaryStatistics s = IntStream.of(values).summaryStatistics();
        return "count=" + s.getCount() + ",min=" + s.getMin() + ",max=" + s.getMax() + ",avg=" + s.getAverage();
    }

    public static long sumOfSquares(int n) {
        // LongStream DES LE DEBUT : x * x deborderait deja en int pour x proche de 100 000.
        return LongStream.rangeClosed(1, n).map(x -> x * x).sum();
    }

    public static long countVowels(String text) {
        // chars() donne les CODES des caracteres ; indexOf(int) accepte directement un code.
        return text.toLowerCase().chars().filter(c -> "aeiouy".indexOf(c) >= 0).count();
    }

    public static List<Integer> top3Desc(int[] values) {
        // IntStream.sorted() ne prend pas de Comparator : boxed() pour revenir aux objets.
        return IntStream.of(values).boxed().sorted(Comparator.reverseOrder()).limit(3).toList();
    }

    public static int[] transform(int[] values, IntUnaryOperator op, IntPredicate keep) {
        // IntUnaryOperator et IntPredicate : les versions int de Function et Predicate, sans boxing.
        return IntStream.of(values).map(op).filter(keep).toArray();
    }

    public static long product(int[] values) {
        // asLongStream() elargit en long AVANT de multiplier ; 1 est l'element neutre de *.
        return IntStream.of(values).asLongStream().reduce(1L, (a, b) -> a * b);
    }

    public static double totalRevenue(List<Line> lines) {
        // mapToDouble puis sum : la somme d'un DoubleStream vide vaut 0.0 (pas de boite).
        return lines.stream().mapToDouble(l -> l.quantity() * l.unitPrice()).sum();
    }

    public static String initials(List<String> names) {
        // Le cast (char) est indispensable : String.valueOf(65) donne "65", pas "A".
        return names.stream()
                .mapToInt(n -> n.charAt(0))
                .map(Character::toUpperCase)
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());
    }
}
