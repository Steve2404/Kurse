package beyondclasses.solutions;

/**
 * Corrige de l'exercice 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans beyondclasses.exercises.Exercise03_StaticAndPrivateInterfaceMethods.
 */
public class Solution03_StaticAndPrivateInterfaceMethods {

    interface MathHelper {
        static int square(int n) {
            return n * n;
        }

        private static int doubleIt(int n) {
            return n * 2;
        }

        static int squarePlusDouble(int n) {
            return square(n) + doubleIt(n);
        }

        private int privateBonus(int n) {
            return n + 1;
        }

        default int scoreFor(int n) {
            return square(n) - privateBonus(n);
        }
    }

    static class Impl implements MathHelper {
    }
}
