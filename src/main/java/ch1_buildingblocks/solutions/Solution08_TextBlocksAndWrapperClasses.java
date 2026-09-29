package ch1_buildingblocks.solutions;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise08_TextBlocksAndWrapperClasses.
 */
public class Solution08_TextBlocksAndWrapperClasses {

    public static String withTrailingBreak() {
        // Le """ final seul sur sa ligne ajoute un saut de ligne a la fin du texte.
        return """
                Hello
                """;
    }

    public static String withoutTrailingBreak() {
        // Le """ colle au texte : aucun saut de ligne final.
        return """
                Hello""";
    }

    public static int compareBoxedValue(int value, Integer other) {
        // Un int n'a aucune methode ; une fois mis en boite (Integer), il a compareTo.
        // Seul le SIGNE du resultat compte, pas forcement -1/0/1.
        Integer boxed = value;
        return boxed.compareTo(other);
    }
}
