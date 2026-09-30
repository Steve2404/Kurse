package ch8_lambdas.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Corrige de l'exercice 13. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise13_EffectivelyFinalPitfall.
 */
public class Solution13_EffectivelyFinalPitfall {

    public static List<Supplier<String>> buildDeferredReports(List<String> tasks) {
        // Une variable NEUVE par tour (copie ou for-each) : la lambda capture une valeur qui ne change plus.
        List<Supplier<String>> deferred = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            int index = i;
            deferred.add(() -> "Tache #" + index + " : " + tasks.get(index));
        }
        return deferred;
    }
}
