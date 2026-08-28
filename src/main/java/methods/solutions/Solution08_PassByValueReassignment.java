package methods.solutions;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans methods.exercises.Exercise08_PassByValueReassignment.
 */
public class Solution08_PassByValueReassignment {

    public static void tryToDouble(int value) {
        value = value * 2;
    }

    public static void tryToReplace(StringBuilder sb) {
        sb = new StringBuilder("Replaced");
    }
}
