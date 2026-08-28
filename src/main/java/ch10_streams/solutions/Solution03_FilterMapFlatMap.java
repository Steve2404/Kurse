package ch10_streams.solutions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Corrige de l'exercice 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise03_FilterMapFlatMap.
 */
public class Solution03_FilterMapFlatMap {

    public static List<String> longUppercaseWords(List<String> words, int minLength) {
        return words.stream()
                .filter(word -> word.length() >= minLength)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }

    public static List<Integer> flattenAndDouble(List<List<Integer>> nestedLists) {
        return nestedLists.stream()
                .flatMap(List::stream)
                .map(n -> n * 2)
                .collect(Collectors.toList());
    }
}
