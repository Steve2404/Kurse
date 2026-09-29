package ch3_makingdecisions.solutions;

/**
 * Corrige de l'exercice 13. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise13_BreakAndContinue.
 */
public class Solution13_BreakAndContinue {

    public static int firstMultipleOf(int[] nums, int divisor) {
        // break arrete toute la boucle des le premier trouve ; -1 si la boucle va jusqu'au bout.
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
        // continue abandonne seulement CE tour et passe au suivant.
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
