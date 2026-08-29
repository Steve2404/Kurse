package ch1_buildingblocks.solutions;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise06_TextBlocksAndWrapperClasses.
 */
public class Solution06_TextBlocksAndWrapperClasses {

    public static String withTrailingBreak() {
        return """
                Hello
                """;
    }

    public static String withoutTrailingBreak() {
        return """
                Hello""";
    }

    public static int compareBoxedValue(int value, Integer other) {
        Integer boxed = value;
        return boxed.compareTo(other);
    }
}
