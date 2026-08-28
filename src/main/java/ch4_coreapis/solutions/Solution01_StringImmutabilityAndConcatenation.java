package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise01_StringImmutabilityAndConcatenation.
 */
public class Solution01_StringImmutabilityAndConcatenation {

    public static String leftToRightConcat1() {
        return "1" + 2 + 3;
    }

    public static String leftToRightConcat2() {
        return 1 + 2 + "3";
    }

    public static String shoutedVersion(String original) {
        return original.toUpperCase().concat("!");
    }
}
