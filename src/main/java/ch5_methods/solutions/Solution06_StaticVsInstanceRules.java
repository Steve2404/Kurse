package ch5_methods.solutions;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch5_methods.exercises.Exercise06_StaticVsInstanceRules.
 */
public class Solution06_StaticVsInstanceRules {

    public static class Tool {
        public static String version() {
            return "1.0";
        }

        public String name() {
            return "tournevis";
        }
    }

    public static boolean canCompile(String caller, String member, String how) {
        // Une seule question : le compilateur sait-il sur QUEL objet agir quand il en faut un ?
        return switch (how) {
            case "object" -> true;
            case "this" -> caller.equals("instance");
            case "className" -> member.equals("static");
            case "unqualified" -> member.equals("static") || caller.equals("instance");
            default -> throw new IllegalArgumentException(how);
        };
    }

    public static String errorFor(String caller, String member, String how) {
        // "this" n'existe pas dans un contexte static ; sinon c'est l'appel a im() sans objet.
        if (canCompile(caller, member, how)) {
            return "";
        }
        if (how.equals("this")) {
            return "non-static variable this cannot be referenced from a static context";
        }
        return "non-static method im() cannot be referenced from a static context";
    }

    @SuppressWarnings("static")
    public static String staticThroughNull() {
        // Un appel static ne regarde que le TYPE de la variable : null n'est jamais dereference.
        Tool t = null;
        return t.version();
    }

    public static String instanceThroughNull() {
        // Une methode d'instance a besoin de l'objet : null -> NullPointerException.
        try {
            Tool t = null;
            return t.name();
        } catch (NullPointerException e) {
            return "NullPointerException";
        }
    }
}
