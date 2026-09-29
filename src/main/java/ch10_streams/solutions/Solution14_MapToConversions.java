package ch10_streams.solutions;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Corrige de l'exercice 14. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise14_MapToConversions.
 */
public class Solution14_MapToConversions {

    public static IntStream wordLengths(Stream<String> words) {
        // mapToInt : Stream<String> -> IntStream (des int bruts, sans boxing).
        return words.mapToInt(String::length);
    }

    public static Stream<String> intsToLabels(IntStream values) {
        // mapToObj : IntStream -> Stream<String> (IntStream n'a pas de map vers un objet).
        return values.mapToObj(n -> "n=" + n);
    }

    public static DoubleStream intsToPercentages(IntStream values, int total) {
        // 100.0 (un double) AVANT la division, sinon v / total serait une division entiere.
        return values.mapToDouble(v -> 100.0 * v / total);
    }
}
