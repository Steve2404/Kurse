package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise08_StringBuilderMutationAndIdentity.
 */
public class Solution08_StringBuilderMutationAndIdentity {

    public static String extractWithoutMutating(StringBuilder sb) {
        // substring rend un String et NE modifie PAS le StringBuilder.
        return sb.substring(0, 3);
    }

    public static void deleteMiddle(StringBuilder sb) {
        // delete(debut, fin) modifie sb en place ; fin EXCLUE.
        sb.delete(2, 5);
    }

    public static void insertAtStart(StringBuilder sb, String text) {
        // insert(0, ...) ajoute devant et modifie sb en place.
        sb.insert(0, text);
    }
}
