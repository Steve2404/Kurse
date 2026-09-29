package ch1_buildingblocks.solutions;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise01_MainMethodArgs.
 */
public class Solution01_MainMethodArgs {

    public static String getArgumentAt(String[] args, int index) {
        // Aucune verification : Java ne controle jamais args a notre place, un index absent
        // lance ArrayIndexOutOfBoundsException comme pour n'importe quel tableau.
        return args[index];
    }

    public static String firstArgumentOrDefault(String[] args, String defaultValue) {
        // On regarde la longueur AVANT de lire args[0] : c'est ce qui evite l'exception.
        if (args.length == 0) {
            return defaultValue;
        }
        return args[0];
    }
}
