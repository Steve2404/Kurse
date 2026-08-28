package ch5_methods.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans methods.exercises.Exercise10_AutoboxingAndUnboxing.
 */
public class Solution10_AutoboxingAndUnboxing {

    public static int sumViaAutobox(List<Integer> numbers) {
        int total = 0;
        for (Integer n : numbers) {
            total += n;
        }
        return total;
    }

    public static int unboxOrThrow(Integer value) {
        return value;
    }
}
