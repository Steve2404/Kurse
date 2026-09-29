package ch10_streams.solutions;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise09_SortedStream.
 */
public class Solution09_SortedStream {

    public static List<String> naturalSort(List<String> words) {
        // sorted() sans argument = ordre naturel (les String sont Comparable).
        return words.stream().sorted().collect(Collectors.toList());
    }

    public static List<String> sortByLengthThenAlpha(List<String> words) {
        // comparingInt pour le 1er critere, thenComparing pour departager les egalites.
        return words.stream()
                .sorted(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()))
                .collect(Collectors.toList());
    }
}
