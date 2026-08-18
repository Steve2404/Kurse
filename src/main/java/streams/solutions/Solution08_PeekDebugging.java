package streams.solutions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise08_PeekDebugging.
 */
public class Solution08_PeekDebugging {

    public static List<Integer> processWithTrace(List<Integer> values, List<String> trace) {
        return values.stream()
                .peek(v -> trace.add("vu:" + v))
                .filter(v -> v % 2 == 0)
                .peek(v -> trace.add("garde:" + v))
                .map(v -> v * v)
                .collect(Collectors.toList());
    }
}
