package ch3_makingdecisions.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise12_ReturnAndSwitchInsideLoops.
 */
public class Solution12_ReturnAndSwitchInsideLoops {

    public static List<Integer> processExceptThree(int[] nums) {
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
        for (int n : nums) {
            if (n <= 0) {
                return n;
            }
        }
        return null;
    }
}
