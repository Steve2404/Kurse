package ch5_methods.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 14. A ne consulter qu'apres avoir essaye par
 * vous-meme dans methods.exercises.Exercise14_AutoboxingAndUnboxing.
 */
public class Solution14_AutoboxingAndUnboxing {

    public static int sumViaAutobox(List<Integer> numbers) {
        // total += n deballe chaque Integer automatiquement (un null lancerait NullPointerException).
        int total = 0;
        for (Integer n : numbers) {
            total += n;
        }
        return total;
    }

    public static int unboxOrThrow(Integer value) {
        // Retourner un Integer comme int le deballe : null -> NullPointerException.
        return value;
    }
}
