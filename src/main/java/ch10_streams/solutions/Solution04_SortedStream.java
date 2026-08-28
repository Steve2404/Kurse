package ch10_streams.solutions;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise04_SortedStream.
 */
public class Solution04_SortedStream {

    public static List<String> naturalSort(List<String> words) {
        return words.stream().sorted().collect(Collectors.toList());
    }

    public static List<String> sortByLengthThenAlpha(List<String> words) {
        return words.stream()
                .sorted(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()))
                .collect(Collectors.toList());
    }
}
