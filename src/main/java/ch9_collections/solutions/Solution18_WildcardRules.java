package ch9_collections.solutions;

import java.util.Map;

/**
 * Corrige de l'exercice 18.
 */
public class Solution18_WildcardRules {

    static final Map<String, String> PARENT = Map.of("Integer", "Number", "Double", "Number", "Number", "Object", "String", "Object");

    public static boolean isA(String a, String b) {
        // Donnee de l'exercice : remonter la chaine des parents de a ; null convient a tout type reference.
        if (a.equals("null")) {
            return true;
        }
        for (String t = a; t != null; t = PARENT.get(t)) {
            if (t.equals(b)) {
                return true;
            }
        }
        return false;
    }

    public static boolean canAdd(String kind, String bound, String arg) {
        // Avec extends ou ?, le vrai type est inconnu : aucun ajout n'est sur, sauf null.
        if (arg.equals("null")) {
            return true;
        }
        switch (kind) {
            case "exact":
            case "super":
                return isA(arg, bound);
            default:
                return false;
        }
    }

    public static String readType(String kind, String bound) {
        // super garantit ce qu'on peut METTRE, pas ce qui sort : a la lecture il ne reste qu'Object.
        return kind.equals("exact") || kind.equals("extends") ? bound : "Object";
    }

    public static boolean canAssign(String kind, String bound, String source) {
        // Les generiques sont invariants : List<Integer> n'est pas une List<Number> ; seuls les wildcards ouvrent la porte.
        switch (kind) {
            case "exact":
                return source.equals(bound);
            case "extends":
                return isA(source, bound);
            case "super":
                return isA(bound, source);
            default:
                return true;
        }
    }
}
