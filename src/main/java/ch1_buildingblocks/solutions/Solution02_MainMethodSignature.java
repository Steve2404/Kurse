package ch1_buildingblocks.solutions;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Corrige de l'exercice 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise02_MainMethodSignature.
 */
public class Solution02_MainMethodSignature {

    static class ValidArray {
        public static void main(String[] args) {
        }
    }

    static class ValidVarargs {
        public static void main(String... args) {
        }
    }

    static class ValidCStyle {
        final static public void main(final String args[]) {
        }
    }

    static class NotStatic {
        public void main(String[] args) {
        }
    }

    static class PrivateMain {
        private static void main(String[] args) {
        }
    }

    static class ReturnsInt {
        public static int main(String[] args) {
            return 0;
        }
    }

    static class NoMain {
    }

    static class WrongParam {
        public static void main(String arg) {
        }
    }

    public static Method findMain(Class<?> type) {
        // String... et String args[] sont tous les deux des String[] pour le compilateur :
        // une seule recherche suffit. main(String) est une AUTRE methode, donc pas trouvee.
        try {
            return type.getDeclaredMethod("main", String[].class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static String diagnose(Class<?> type) {
        // Meme ordre que le lanceur : une porte private est invisible (INTROUVABLE),
        // puis il faut pouvoir l'ouvrir sans objet (static), puis rien a rendre (void).
        Method main = findMain(type);
        if (main == null || !Modifier.isPublic(main.getModifiers())) {
            return "INTROUVABLE";
        }
        if (!Modifier.isStatic(main.getModifiers())) {
            return "PAS_STATIC";
        }
        if (main.getReturnType() != void.class) {
            return "PAS_VOID";
        }
        return "LANCABLE";
    }

    public static String launcherMessage(Class<?> type) {
        // Les phrases sont celles, reelles, du lanceur java 17 (version anglaise, coupee
        // avant "please define the main method as").
        String name = type.getSimpleName();
        switch (diagnose(type)) {
            case "INTROUVABLE":
                return "Main method not found in class " + name;
            case "PAS_STATIC":
                return "Main method is not static in class " + name;
            case "PAS_VOID":
                return "Main method must return a value of type void in class " + name;
            default:
                return "OK";
        }
    }
}
