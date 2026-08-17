package lambdas.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans lambdas.exercises.Exercise07_EffectivelyFinalPitfall.
 */
public class Solution07_EffectivelyFinalPitfall {

    public static List<Supplier<String>> buildDeferredReports(List<String> tasks) {
        List<Supplier<String>> deferred = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            int index = i;
            deferred.add(() -> "Tache #" + index + " : " + tasks.get(index));
        }
        return deferred;
    }
}
