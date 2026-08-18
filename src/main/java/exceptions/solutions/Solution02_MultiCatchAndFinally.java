package exceptions.solutions;

import java.util.List;
import java.util.function.Supplier;

/**
 * Corrige de l'exercice 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans exceptions.exercises.Exercise02_MultiCatchAndFinally.
 */
public class Solution02_MultiCatchAndFinally {

    public static Integer withFinallyTrace(Supplier<Integer> action, List<String> trace) {
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
