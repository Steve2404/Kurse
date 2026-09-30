package ch7_beyondclasses.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise11_SealedRules.
 */
public class Solution11_SealedRules {

    public static String childError(String childKind, String modifier) {
        // Chaque enfant d'un sealed doit dire la suite de la lignee ; record et enum sont deja final.
        return switch (childKind) {
            case "anonymous" -> "anonymous classes must not extend sealed classes";
            case "local" -> "local classes must not extend sealed classes";
            case "record", "enum" -> "OK";
            case "interface" -> modifier.isEmpty() ? "sealed or non-sealed modifiers expected"
                    : modifier.equals("final") ? "illegal combination of modifiers: interface and final" : "OK";
            default -> modifier.isEmpty() ? "sealed, non-sealed or final modifiers expected" : "OK";
        };
    }

    public static String parentError(boolean hasPermits, boolean childrenInSameFile, boolean childListed,
                                     boolean childExtends, boolean hasChildren) {
        // Sans permits, javac ne cherche les enfants QUE dans le meme fichier.
        if (!hasChildren || (!hasPermits && !childrenInSameFile)) {
            return "sealed class must have subclasses";
        }
        if (hasPermits && !childListed) {
            return "class is not allowed to extend sealed class: Shape (as it is not listed in its permits clause)";
        }
        if (hasPermits && !childExtends) {
            return "invalid permits clause";
        }
        return "OK";
    }

    public static String modifiersError(List<String> modifiers, boolean parentIsSealed) {
        // sealed et final se contredisent ; non-sealed n'a de sens que sous un parent sealed.
        if (modifiers.contains("sealed") && modifiers.contains("final")) {
            return "illegal combination of modifiers: final and sealed";
        }
        if (modifiers.contains("non-sealed") && !parentIsSealed) {
            return "non-sealed modifier not allowed here";
        }
        return "OK";
    }
}
