package ch1_buildingblocks.solutions;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise05_PrimitivesVsReferenceTypes.
 */
public class Solution05_PrimitivesVsReferenceTypes {

    static class Defaults {
        int number;
        boolean flag;
        String text;
        Integer wrapped;
    }

    public static String describeDefaults() {
        // Les CHAMPS recoivent une valeur par defaut (0, false, null) ; une variable locale jamais.
        // Integer est un type de reference : sa valeur par defaut est null, pas 0.
        Defaults d = new Defaults();
        return d.number + "/" + d.flag + "/" + d.text + "/" + d.wrapped;
    }

    public static Integer nullableWrapper() {
        // Seuls les types de reference (ici Integer) acceptent null ; un int ne le pourrait pas.
        return null;
    }
}
