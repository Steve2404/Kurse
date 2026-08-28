package ch10_streams.solutions;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise06_MapToConversions.
 */
public class Solution06_MapToConversions {

    public static IntStream wordLengths(Stream<String> words) {
        return words.mapToInt(String::length);
    }

    public static Stream<String> intsToLabels(IntStream values) {
        return values.mapToObj(n -> "n=" + n);
    }

    public static DoubleStream intsToPercentages(IntStream values, int total) {
        return values.mapToDouble(v -> 100.0 * v / total);
    }
}
