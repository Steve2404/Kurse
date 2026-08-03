package collections.solutions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans collections.exercises.Exercise11_TopKFrequentWords.
 */
public class Solution11_TopKFrequentWords {

    public static List<String> topKFrequent(List<String> words, int k) {
        Map<String, Integer> counts = new HashMap<>();
        for (String word : words) {
            counts.merge(word, 1, Integer::sum);
        }

        List<String> uniqueWords = new ArrayList<>(counts.keySet());
        uniqueWords.sort((a, b) -> {
            int cmp = counts.get(b) - counts.get(a);
            if (cmp != 0) {
                return cmp;
            }
            return a.compareTo(b);
        });

        return new ArrayList<>(uniqueWords.subList(0, Math.min(k, uniqueWords.size())));
    }
}
