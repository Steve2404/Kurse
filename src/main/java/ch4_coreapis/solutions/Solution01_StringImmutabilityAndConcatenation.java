package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise01_StringImmutabilityAndConcatenation.
 */
public class Solution01_StringImmutabilityAndConcatenation {

    public static String leftToRightConcat1() {
        // Lecture de gauche a droite : "1" est deja un String, donc chaque + concatene -> "123".
        return "1" + 2 + 3;
    }

    public static String leftToRightConcat2() {
        // 1 + 2 est d'abord une addition (3), puis 3 + "3" concatene -> "33".
        return 1 + 2 + "3";
    }

    public static String shoutedVersion(String original) {
        // String est immuable : chaque appel rend un NOUVEAU String, original ne change pas.
        return original.toUpperCase().concat("!");
    }
}
