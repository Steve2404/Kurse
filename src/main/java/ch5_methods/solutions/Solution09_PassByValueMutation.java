package ch5_methods.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans methods.exercises.Exercise09_PassByValueMutation.
 */
public class Solution09_PassByValueMutation {

    public static void appendExclamation(StringBuilder sb) {
        sb.append("!");
    }

    public static void addItem(List<String> list, String item) {
        list.add(item);
    }
}
