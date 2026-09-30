package ch7_beyondclasses.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise04_InterfaceRules.
 */
public class Solution04_InterfaceRules {

    public static String memberError(String kind, List<String> modifiers, boolean hasBody, boolean hasInitializer) {
        // Les modificateurs interdits d'abord, puis la regle du corps (ou de la valeur) propre a chaque sorte.
        if (modifiers.contains("private") && kind.equals("field")) {
            return "modifier private not allowed here";
        }
        if (modifiers.contains("protected")) {
            return "modifier protected not allowed here";
        }
        if (modifiers.contains("final") && !kind.equals("field")) {
            return "modifier final not allowed here";
        }
        if (kind.equals("field")) {
            return hasInitializer ? "OK" : "= expected";
        }
        if (kind.equals("abstract") && hasBody) {
            return "interface abstract methods cannot have body";
        }
        if (!kind.equals("abstract") && !hasBody) {
            return "missing method body, or declare abstract";
        }
        return "OK";
    }

    public static String implicitModifiers(String kind) {
        // Ce que le compilateur ajoute tout seul a chaque sorte de membre.
        return switch (kind) {
            case "field" -> "public static final";
            case "abstract" -> "public abstract";
            case "default" -> "public";
            case "static" -> "public static";
            case "private" -> "private";
            default -> throw new IllegalArgumentException(kind);
        };
    }

    public static String staticCallError(String how) {
        // Une methode static d'interface n'est pas heritee : seul I.s() la trouve.
        return how.equals("I.s()") ? "OK" : "cannot find symbol";
    }

    public static String diamondError(boolean bothHaveDefault, boolean classRedefines) {
        // Deux default de meme signature : la classe doit trancher en redefinissant (et peut appeler A.super.h()).
        return bothHaveDefault && !classRedefines ? "types A and B are incompatible;" : "OK";
    }
}
