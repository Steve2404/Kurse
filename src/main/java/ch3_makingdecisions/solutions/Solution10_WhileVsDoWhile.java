package ch3_makingdecisions.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise10_WhileVsDoWhile.
 */
public class Solution10_WhileVsDoWhile {

    public static List<Integer> countdownWhile(int n) {
        // while teste AVANT : si n <= 0 au depart, la boucle ne tourne jamais.
        List<Integer> result = new ArrayList<>();
        while (n > 0) {
            result.add(n);
            n--;
        }
        return result;
    }

    public static int runAtLeastOnce(int startValue) {
        // do/while teste APRES : le corps s'execute au moins une fois, meme si la condition est fausse d'emblee.
        int count = 0;
        int n = startValue;
        do {
            count++;
            n--;
        } while (n > 0);
        return count;
    }
}
