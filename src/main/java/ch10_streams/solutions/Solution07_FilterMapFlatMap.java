package ch10_streams.solutions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise07_FilterMapFlatMap.
 */
public class Solution07_FilterMapFlatMap {

    public static List<String> longUppercaseWords(List<String> words, int minLength) {
        // filter AVANT map : on ne met en majuscules que les mots gardes.
        return words.stream()
                .filter(word -> word.length() >= minLength)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }

    public static List<Integer> flattenAndDouble(List<List<Integer>> nestedLists) {
        // flatMap(List::stream) aplatit la liste de listes en un seul flux d'entiers.
        return nestedLists.stream()
                .flatMap(List::stream)
                .map(n -> n * 2)
                .collect(Collectors.toList());
    }
}
