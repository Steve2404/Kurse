package ch3_makingdecisions.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise08_WhileVsDoWhile.
 */
public class Solution08_WhileVsDoWhile {

    public static List<Integer> countdownWhile(int n) {
        List<Integer> result = new ArrayList<>();
        while (n > 0) {
            result.add(n);
            n--;
        }
        return result;
    }

    public static int runAtLeastOnce(int startValue) {
        int count = 0;
        int n = startValue;
        do {
            count++;
            n--;
        } while (n > 0);
        return count;
    }
}
