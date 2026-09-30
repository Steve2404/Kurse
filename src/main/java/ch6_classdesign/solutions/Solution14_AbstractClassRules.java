package ch6_classdesign.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 14. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.exercises.Exercise14_AbstractClassRules.
 */
public class Solution14_AbstractClassRules {

    public static List<String> stillAbstract(List<String> inherited, List<String> implemented) {
        // Tous les trous herites (parents, grands-parents, interfaces) moins ceux que la classe remplit.
        List<String> missing = new ArrayList<>();
        for (String method : inherited) {
            String name = method.substring(0, method.indexOf(" in "));
            if (!implemented.contains(name)) {
                missing.add(method);
            }
        }
        return missing;
    }

    public static String classError(String className, boolean isAbstract, List<String> inherited, List<String> implemented) {
        // Une classe abstract peut laisser des trous ; la premiere concrete doit tout remplir.
        if (isAbstract) {
            return "OK";
        }
        List<String> missing = stillAbstract(inherited, implemented);
        if (missing.isEmpty()) {
            return "OK";
        }
        return className + " is not abstract and does not override abstract method " + missing.get(0);
    }

    public static String modifierError(String target, List<String> modifiers, boolean hasBody) {
        // abstract = "a completer par un enfant" : incompatible avec tout ce qui empeche la redefinition.
        if (!modifiers.contains("abstract")) {
            return "OK";
        }
        if (target.equals("constructor") || target.equals("field")) {
            return "modifier abstract not allowed here";
        }
        for (String other : List.of("final", "private", "static")) {
            if (modifiers.contains(other)) {
                return "illegal combination of modifiers: abstract and " + other;
            }
        }
        if (target.equals("method") && hasBody) {
            return "abstract methods cannot have a body";
        }
        return "OK";
    }

    public static String instantiationError(String className, boolean isAbstract) {
        // On ne construit jamais directement un plan a moitie rempli.
        return isAbstract ? className + " is abstract; cannot be instantiated" : "OK";
    }
}
