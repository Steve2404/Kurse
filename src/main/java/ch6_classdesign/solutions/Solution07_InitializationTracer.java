package ch6_classdesign.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.exercises.Exercise07_InitializationTracer.
 */
public class Solution07_InitializationTracer {

    public record ClassParts(List<String> statics, List<String> instance, String constructor) {
    }

    public static List<String> simulate(ClassParts parent, ClassParts child, boolean alreadyLoaded) {
        // Static une seule fois (parent puis enfant), puis parent complet, puis enfant complet.
        List<String> order = new ArrayList<>();
        if (!alreadyLoaded) {
            order.addAll(parent.statics());
            order.addAll(child.statics());
        }
        order.addAll(parent.instance());
        order.add(parent.constructor());
        order.addAll(child.instance());
        order.add(child.constructor());
        return order;
    }

    public static String defaultValueOf(String type) {
        // Valeurs par defaut des champs : nombres a zero, false, caractere nul, null pour les references.
        return switch (type) {
            case "int", "long", "short", "byte" -> "0";
            case "double", "float" -> "0.0";
            case "boolean" -> "false";
            case "char" -> "\u0000";
            default -> "null";
        };
    }
}
