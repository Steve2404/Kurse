package ch10_streams.solutions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Corrige de l'exercice 19. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise19_GroupingBy.
 */
public class Solution19_GroupingBy {

    public static Map<Integer, List<String>> groupByLength(List<String> words) {
        // groupingBy(classifieur) seul : Map<cle, List<element>>.
        return words.stream().collect(Collectors.groupingBy(String::length));
    }

    public static Map<Integer, Long> countByLength(List<String> words) {
        // counting() en aval remplace chaque liste par son nombre d'elements (un Long).
        return words.stream().collect(Collectors.groupingBy(String::length, Collectors.counting()));
    }
}
