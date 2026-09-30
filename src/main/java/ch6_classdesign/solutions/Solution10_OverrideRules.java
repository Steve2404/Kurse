package ch6_classdesign.solutions;

import java.util.Arrays;
import java.util.Map;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.exercises.Exercise10_OverrideRules.
 */
public class Solution10_OverrideRules {

    public record Method(String access, boolean isStatic, boolean isFinal, String returnType, String throwsType) {
    }

    public static final Map<String, String> SUPER_TYPE = Map.ofEntries(
            Map.entry("String", "Object"), Map.entry("Number", "Object"), Map.entry("Integer", "Number"),
            Map.entry("Animal", "Object"), Map.entry("Dog", "Animal"), Map.entry("A", "Object"), Map.entry("D", "A"),
            Map.entry("Throwable", "Object"), Map.entry("Exception", "Throwable"), Map.entry("Error", "Throwable"),
            Map.entry("IOException", "Exception"), Map.entry("FileNotFoundException", "IOException"),
            Map.entry("RuntimeException", "Exception"), Map.entry("IllegalArgumentException", "RuntimeException"));

    public static final String[] ACCESS = {"private", "package", "protected", "public"};

    public static String reason(Method parent, Method child) {
        // L'enfant ne doit jamais promettre MOINS que le parent ; une methode private n'est pas heritee.
        if (parent.access().equals("private")) {
            return "OK";
        }
        if (parent.isFinal()) {
            return parent.isStatic() && child.isStatic() ? "overridden method is static,final" : "overridden method is final";
        }
        if (parent.isStatic() && !child.isStatic()) {
            return "overridden method is static";
        }
        if (!parent.isStatic() && child.isStatic()) {
            return "overriding method is static";
        }
        if (rank(child.access()) < rank(parent.access())) {
            return "attempting to assign weaker access privileges; was " + parent.access();
        }
        if (!child.returnType().equals(parent.returnType())
                && (isPrimitive(child.returnType()) || isPrimitive(parent.returnType())
                || !isSubtypeOf(child.returnType(), parent.returnType()))) {
            return "return type " + child.returnType() + " is not compatible with " + parent.returnType();
        }
        String thrown = child.throwsType();
        if (thrown != null && isChecked(thrown)
                && (parent.throwsType() == null || !isSubtypeOf(thrown, parent.throwsType()))) {
            return "overridden method does not throw " + thrown;
        }
        return "OK";
    }

    private static int rank(String access) {
        // private < package < protected < public.
        return Arrays.asList(ACCESS).indexOf(access);
    }

    private static boolean isPrimitive(String type) {
        // Les types primitifs (et void) commencent par une minuscule.
        return Character.isLowerCase(type.charAt(0));
    }

    private static boolean isSubtypeOf(String type, String parentType) {
        // On remonte la chaine des super-types jusqu'a Object.
        for (String t = type; t != null; t = SUPER_TYPE.get(t)) {
            if (t.equals(parentType)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isChecked(String exception) {
        // Unchecked = RuntimeException, Error et leurs sous-classes ; le reste est checked.
        return !isSubtypeOf(exception, "RuntimeException") && !isSubtypeOf(exception, "Error");
    }

    public static String relation(Method parent, Method child) {
        // private : rien d'herite ; static : cachage ; instance : redefinition polymorphe.
        if (parent.access().equals("private")) {
            return "redeclaration";
        }
        return parent.isStatic() ? "hiding" : "overriding";
    }
}
