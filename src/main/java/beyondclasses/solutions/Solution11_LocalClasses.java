package beyondclasses.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans beyondclasses.exercises.Exercise11_LocalClasses.
 */
public class Solution11_LocalClasses {

    public static String buildGreeting(String prefix, String name) {
        class Greeting {
            String render() {
                return prefix + ", " + name + " !";
            }
        }
        return new Greeting().render();
    }

    public static List<Supplier<Integer>> buildCounters(int n) {
        List<Supplier<Integer>> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            final int captured = i;
            class Counter implements Supplier<Integer> {
                @Override
                public Integer get() {
                    return captured;
                }
            }
            result.add(new Counter());
        }
        return result;
    }
}
