package ch5_methods.solutions;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans methods.exercises.Exercise11_PassByValueReassignment.
 */
public class Solution11_PassByValueReassignment {

    public static void tryToDouble(int value) {
        // value est une COPIE du nombre de l'appelant : la doubler ne change rien dehors.
        value = value * 2;
    }

    public static void tryToReplace(StringBuilder sb) {
        // sb est une copie de l'ADRESSE : la faire pointer ailleurs ne touche pas l'objet de l'appelant.
        sb = new StringBuilder("Replaced");
    }
}
