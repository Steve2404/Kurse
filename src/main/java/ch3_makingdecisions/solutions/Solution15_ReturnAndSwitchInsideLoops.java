package ch3_makingdecisions.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 15. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise15_ReturnAndSwitchInsideLoops.
 */
public class Solution15_ReturnAndSwitchInsideLoops {

    public static List<Integer> processExceptThree(int[] nums) {
        // continue dans un switch agit sur la BOUCLE englobante : il saute au tour suivant.
        List<Integer> result = new ArrayList<>();
        for (int n : nums) {
            switch (n) {
                case 3:
                    continue;
                default:
                    result.add(n);
            }
        }
        return result;
    }

    public static Integer findFirstNegativeOrZero(int[] nums) {
        // return quitte la boucle ET la methode d'un coup ; null si rien n'est trouve.
        for (int n : nums) {
            if (n <= 0) {
                return n;
            }
        }
        return null;
    }
}
