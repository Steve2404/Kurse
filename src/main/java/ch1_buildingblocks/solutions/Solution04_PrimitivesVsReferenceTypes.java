package ch1_buildingblocks.solutions;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise04_PrimitivesVsReferenceTypes.
 */
public class Solution04_PrimitivesVsReferenceTypes {

    static class Defaults {
        int number;
        boolean flag;
        String text;
        Integer wrapped;
    }

    public static String describeDefaults() {
        Defaults d = new Defaults();
        return d.number + "/" + d.flag + "/" + d.text + "/" + d.wrapped;
    }

    public static Integer nullableWrapper() {
        return null;
    }
}
