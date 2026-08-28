package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise05_StringBuilderMutationAndIdentity.
 */
public class Solution05_StringBuilderMutationAndIdentity {

    public static String extractWithoutMutating(StringBuilder sb) {
        return sb.substring(0, 3);
    }

    public static void deleteMiddle(StringBuilder sb) {
        sb.delete(2, 5);
    }

    public static void insertAtStart(StringBuilder sb, String text) {
        sb.insert(0, text);
    }
}
