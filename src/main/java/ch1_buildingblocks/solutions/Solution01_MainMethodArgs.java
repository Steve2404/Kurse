package ch1_buildingblocks.solutions;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise01_MainMethodArgs.
 */
public class Solution01_MainMethodArgs {

    public static String getArgumentAt(String[] args, int index) {
        return args[index];
    }

    public static String firstArgumentOrDefault(String[] args, String defaultValue) {
        if (args.length == 0) {
            return defaultValue;
        }
        return args[0];
    }
}
