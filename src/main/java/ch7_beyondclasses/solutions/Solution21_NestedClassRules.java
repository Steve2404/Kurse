package ch7_beyondclasses.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 21. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise21_NestedClassRules.
 */
public class Solution21_NestedClassRules {

    public static String declarationError(String where, String modifier) {
        // Un membre accepte tout ; une classe top-level n'est que public ou package ; une locale n'a aucun acces.
        if (where.equals("member")) {
            return "OK";
        }
        if (where.equals("top-level") && List.of("private", "protected", "static").contains(modifier)) {
            return "modifier " + modifier + " not allowed here";
        }
        if (where.equals("local") && List.of("public", "protected", "private", "static").contains(modifier)) {
            return "illegal start of expression";
        }
        return "OK";
    }

    public static String accessError(boolean fromStaticNested, String target) {
        // Une imbriquee static n'a pas d'objet O : ni champ d'instance, ni O.this.
        if (!fromStaticNested) {
            return "OK";
        }
        return switch (target) {
            case "instance field" -> "non-static variable f cannot be referenced from a static context";
            case "O.this" -> "non-static variable this cannot be referenced from a static context";
            default -> "OK";
        };
    }

    public static String creationError(boolean innerClass, boolean fromStaticContext, boolean withOuterObject) {
        // Une classe interne a besoin d'un objet O : new I() dans une methode static n'en a pas.
        if (innerClass && fromStaticContext && !withOuterObject) {
            return "non-static variable this cannot be referenced from a static context";
        }
        return "OK";
    }

    public static String anonymousError(int superTypes, boolean isInterface, boolean hasArguments, boolean declaresConstructor) {
        // Une anonyme : un seul super-type, pas de constructeur (elle n'a pas de nom), pas d'arguments pour une interface.
        if (superTypes > 1) {
            return "'(' or '[' expected";
        }
        if (isInterface && hasArguments) {
            return "anonymous class implements interface; cannot have arguments";
        }
        if (declaresConstructor) {
            return "invalid method declaration; return type required";
        }
        return "OK";
    }
}
