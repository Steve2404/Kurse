package ch11_exceptions.solutions;

import java.util.List;
import java.util.function.Supplier;

/**
 * Corrige de l'exercice 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch11_exceptions.exercises.Exercise03_MultiCatchAndFinally.
 */
public class Solution03_MultiCatchAndFinally {

    public static Integer withFinallyTrace(Supplier<Integer> action, List<String> trace) {
        // Multi-catch : deux types sans lien d'heritage, un seul bloc ; finally s'ajoute a la trace meme apres un return.
        try {
            int result = action.get();
            trace.add("try");
            return result;
        } catch (ArithmeticException | NullPointerException e) {
            trace.add("catch:" + e.getClass().getSimpleName());
            return -1;
        } finally {
            trace.add("finally");
        }
    }
}
