package ch10_streams.solutions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise11_PeekDebugging.
 */
public class Solution11_PeekDebugging {

    public static List<Integer> processWithTrace(List<Integer> values, List<String> trace) {
        // Le 1er peek voit TOUS les elements, le 2e (apres filter) seulement les pairs.
        // peek sert a observer, jamais a modifier.
        return values.stream()
                .peek(v -> trace.add("vu:" + v))
                .filter(v -> v % 2 == 0)
                .peek(v -> trace.add("garde:" + v))
                .map(v -> v * v)
                .collect(Collectors.toList());
    }
}
