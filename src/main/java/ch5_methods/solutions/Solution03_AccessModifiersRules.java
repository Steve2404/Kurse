package ch5_methods.solutions;

/**
 * Corrige de l'exercice 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch5_methods.exercises.Exercise03_AccessModifiersRules.
 */
public class Solution03_AccessModifiersRules {

    public static final String[] MODIFIERS = {"private", "package", "protected", "public"};

    public static boolean canAccess(String modifier, String situation) {
        // Trois questions : dans la classe ? dans le paquet ? en tant qu'heritiere (this, son type, ou static) ?
        boolean inside = situation.equals("SAME_CLASS") || situation.equals("NESTED_CLASS");
        boolean samePackage = inside || situation.equals("SAME_FILE_OTHER_CLASS") || situation.equals("SAME_PACKAGE");
        boolean asHeir = situation.equals("SUBCLASS_OTHER_PACKAGE_THIS") || situation.equals("SUBCLASS_OTHER_PACKAGE_CHILD_REF")
                || situation.equals("SUBCLASS_OTHER_PACKAGE_STATIC");
        return switch (modifier) {
            case "private" -> inside;
            case "package" -> samePackage;
            case "protected" -> samePackage || asHeir;
            case "public" -> true;
            default -> throw new IllegalArgumentException(modifier);
        };
    }

    public static String narrowest(String... situations) {
        // Du plus ferme au plus ouvert : le premier qui marche partout est le bon choix.
        for (String modifier : MODIFIERS) {
            boolean everywhere = true;
            for (String situation : situations) {
                everywhere &= canAccess(modifier, situation);
            }
            if (everywhere) {
                return modifier;
            }
        }
        return "public";
    }

    public static String errorMessage(String modifier, String field, String owner) {
        // Les phrases exactes de javac, une par niveau d'acces.
        return switch (modifier) {
            case "private" -> field + " has private access in " + owner;
            case "package" -> field + " is not public in " + owner + "; cannot be accessed from outside package";
            case "protected" -> field + " has protected access in " + owner;
            default -> "";
        };
    }
}
