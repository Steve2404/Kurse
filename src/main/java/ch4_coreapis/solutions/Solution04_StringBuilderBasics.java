package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise04_StringBuilderBasics.
 */
public class Solution04_StringBuilderBasics {

    public static String buildViaChaining(String name) {
        return new StringBuilder().append("Bonjour, ").append(name).append(" !").toString();
    }

    public static void appendExclamations(StringBuilder sb, int count) {
        for (int i = 0; i < count; i++) {
            sb.append("!");
        }
    }
}
