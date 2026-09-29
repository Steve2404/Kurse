package ch5_methods.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans methods.exercises.Exercise12_PassByValueMutation.
 */
public class Solution12_PassByValueMutation {

    public static void appendExclamation(StringBuilder sb) {
        // Meme adresse que l'appelant : modifier l'OBJET est visible dehors.
        sb.append("!");
    }

    public static void addItem(List<String> list, String item) {
        // La liste est modifiee en place : l'appelant voit le nouvel element.
        list.add(item);
    }
}
