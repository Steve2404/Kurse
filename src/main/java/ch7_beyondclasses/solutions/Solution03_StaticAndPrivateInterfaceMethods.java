package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise03_StaticAndPrivateInterfaceMethods.
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
            // Une static d'interface ne peut appeler que des methodes static (ici une private static).
            return square(n) + doubleIt(n);
        }

        private int privateBonus(int n) {
            return n + 1;
        }

        default int scoreFor(int n) {
            // Un default peut appeler une private d'instance ET une static de la meme interface.
            return square(n) - privateBonus(n);
        }
    }

    static class Impl implements MathHelper {
    }
}
