package ch6_classdesign.solutions;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.exercises.Exercise08_FinalFieldsRules.
 */
public class Solution08_FinalFieldsRules {

    public static final String NOT_INIT_DEFAULT = "variable x not initialized in the default constructor";
    public static final String MIGHT_NOT = "variable x might not have been initialized";
    public static final String ALREADY = "variable x might already have been assigned";
    public static final String CANNOT_ASSIGN = "cannot assign a value to final variable x";
    public static final String STATIC_NOT_INIT = "variable X not initialized in the default constructor";
    public static final String STATIC_CANNOT_ASSIGN = "cannot assign a value to final variable X";

    public static String check(boolean hasInitializer, boolean assignedInBlock, String... constructors) {
        // Compter les affectations AVANT le constructeur, puis verifier chaque chemin de construction.
        int before = (hasInitializer ? 1 : 0) + (assignedInBlock ? 1 : 0);
        if (constructors.length == 0) {
            return before == 0 ? NOT_INIT_DEFAULT : "OK";
        }
        for (String kind : constructors) {
            if (kind.equals("delegate")) {
                continue;
            }
            if (kind.equals("delegate+assign")) {
                return ALREADY;
            }
            if (before == 1 && assigns(kind)) {
                return hasInitializer ? CANNOT_ASSIGN : ALREADY;
            }
            if (before == 0) {
                if (kind.equals("none") || kind.equals("if") || kind.equals("read-then-assign")) {
                    return MIGHT_NOT;
                }
                if (kind.equals("assign twice")) {
                    return ALREADY;
                }
            }
        }
        return "OK";
    }

    private static boolean assigns(String kind) {
        // Petite boite : ce constructeur ecrit-il x lui-meme ?
        return !kind.equals("none") && !kind.equals("delegate");
    }

    public static String checkStatic(boolean hasInitializer, boolean assignedInStaticBlock, boolean assignedInConstructor) {
        // Un static final appartient a la classe : le constructeur (qui tourne a chaque new) ne peut pas l'ecrire.
        if (assignedInConstructor) {
            return STATIC_CANNOT_ASSIGN;
        }
        if (!hasInitializer && !assignedInStaticBlock) {
            return STATIC_NOT_INIT;
        }
        return "OK";
    }
}
