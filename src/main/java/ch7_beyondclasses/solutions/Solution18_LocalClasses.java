package ch7_beyondclasses.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Corrige de l'exercice 18. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise18_LocalClasses.
 */
public class Solution18_LocalClasses {

    public static String buildGreeting(String prefix, String name) {
        // Classe locale : visible seulement dans la methode ; elle lit des parametres effectivement final.
        class Greeting {
            String render() {
                return prefix + ", " + name + " !";
            }
        }
        return new Greeting().render();
    }

    public static List<Supplier<Integer>> buildCounters(int n) {
        // Une copie par tour (i change a chaque tour, la copie jamais).
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
