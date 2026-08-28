package ch3_makingdecisions.solutions;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise10_BreakAndContinue.
 */
public class Solution10_BreakAndContinue {

    public static int firstMultipleOf(int[] nums, int divisor) {
        int result = -1;
        for (int n : nums) {
            if (n % divisor == 0) {
                result = n;
                break;
            }
        }
        return result;
    }

    public static int sumSkippingNegatives(int[] nums) {
        int total = 0;
        for (int n : nums) {
            if (n < 0) {
                continue;
            }
            total += n;
        }
        return total;
    }
}
