package ch10_streams.solutions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise10_GroupingBy.
 */
public class Solution10_GroupingBy {

    public static Map<Integer, List<String>> groupByLength(List<String> words) {
        return words.stream().collect(Collectors.groupingBy(String::length));
    }

    public static Map<Integer, Long> countByLength(List<String> words) {
        return words.stream().collect(Collectors.groupingBy(String::length, Collectors.counting()));
    }
}
