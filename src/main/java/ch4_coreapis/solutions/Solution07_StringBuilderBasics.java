package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise07_StringBuilderBasics.
 */
public class Solution07_StringBuilderBasics {

    public static String buildViaChaining(String name) {
        // append rend le MEME StringBuilder : on peut chainer, puis toString() a la fin.
        return new StringBuilder().append("Bonjour, ").append(name).append(" !").toString();
    }

    public static void appendExclamations(StringBuilder sb, int count) {
        // Le StringBuilder recu est modifie en place : l'appelant voit les '!' sans return.
        for (int i = 0; i < count; i++) {
            sb.append("!");
        }
    }
}
