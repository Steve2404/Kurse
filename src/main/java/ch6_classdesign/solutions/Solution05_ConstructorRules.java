package ch6_classdesign.solutions;

import java.util.List;
import java.util.Map;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.exercises.Exercise05_ConstructorRules.
 */
public class Solution05_ConstructorRules {

    public static final String NO_MATCH = "constructor P in class P cannot be applied to given types;";
    public static final String THIS_NOT_FIRST = "call to this must be first statement in constructor";
    public static final String SUPER_NOT_FIRST = "call to super must be first statement in constructor";

    public static String check(List<String> parentConstructors, String... body) {
        // this(...) et super(...) seulement en 1re ligne ; sinon javac glisse un super() sans argument.
        for (int i = 1; i < body.length; i++) {
            if (body[i].startsWith("this(")) {
                return THIS_NOT_FIRST;
            }
            if (body[i].startsWith("super(")) {
                return SUPER_NOT_FIRST;
            }
        }
        if (body.length > 0 && body[0].startsWith("this(")) {
            return "OK";
        }
        String called = superSignature(body.length > 0 ? body[0] : "");
        List<String> available = parentConstructors.isEmpty() ? List.of("()") : parentConstructors;
        return available.contains(called) ? "OK" : NO_MATCH;
    }

    private static String superSignature(String firstStatement) {
        // Pas de super(...) ecrit, ou super() vide : c'est le constructeur sans argument qui est appele.
        if (!firstStatement.startsWith("super(") || firstStatement.equals("super()")) {
            return "()";
        }
        return "(int)";
    }

    public static boolean isRecursive(Map<String, String> delegations) {
        // Suivre les this(...) : revenir au depart = cycle (au plus size() pas suffisent).
        for (String start : delegations.keySet()) {
            String current = delegations.get(start);
            for (int step = 0; step < delegations.size() && current != null; step++) {
                if (current.equals(start)) {
                    return true;
                }
                current = delegations.get(current);
            }
        }
        return false;
    }

    public static boolean canCallNoArg(List<String> writtenConstructors) {
        // Le constructeur par defaut n'existe que si AUCUN constructeur n'est ecrit.
        return writtenConstructors.isEmpty() || writtenConstructors.contains("()");
    }
}
