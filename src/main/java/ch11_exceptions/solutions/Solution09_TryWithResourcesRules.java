package ch11_exceptions.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 9.
 */
public class Solution09_TryWithResourcesRules {

    public static boolean isA(String a, String b) {
        // Donnee de l'exercice : le petit arbre IOException -> Exception -> Throwable.
        List<String> chain = switch (a) {
            case "IOException" -> List.of("IOException", "Exception", "Throwable");
            case "Exception" -> List.of("Exception", "Throwable");
            default -> List.of(a);
        };
        return chain.contains(b);
    }

    public static String resourceError(String type, String form, boolean effectivelyFinal) {
        // Le close() cache doit etre appele sur une VARIABLE stable : pas d'expression new, pas de variable reassignee.
        if (form.equals("newExpression")) {
            return "the try-with-resources resource must either be a variable declaration or an expression "
                    + "denoting a reference to a final or effectively final variable";
        }
        if (type.equals("String")) {
            return "incompatible types: try-with-resources not applicable to variable type";
        }
        if (form.equals("existingVariable") && !effectivelyFinal) {
            return "variable r used as a try-with-resources resource neither final nor effectively final";
        }
        return "OK";
    }

    public static String closeError(String closeThrows, List<String> caught, List<String> declared) {
        // Le close() implicite est un appel comme un autre : sa checked doit etre attrapee OU declaree.
        if (closeThrows.isEmpty() || covered(closeThrows, caught) || covered(closeThrows, declared)) {
            return "OK";
        }
        return "unreported exception " + closeThrows + "; must be caught or declared to be thrown";
    }

    public static String bodyError(boolean assignsResource, boolean usesResourceAfter) {
        // La ressource est implicitement final, et sa portee s'arrete a la fin du bloc try.
        if (assignsResource) {
            return "auto-closeable resource r may not be assigned";
        }
        if (usesResourceAfter) {
            return "cannot find symbol";
        }
        return "OK";
    }

    public static String tryShapeError(boolean hasResources, boolean hasCatchOrFinally) {
        // Seul un try-with-resources peut vivre sans catch ni finally (son finally est implicite).
        return hasResources || hasCatchOrFinally ? "OK" : "'try' without 'catch', 'finally' or resource declarations";
    }

    private static boolean covered(String type, List<String> ancestors) {
        // Un catch ou un throws d'un ancetre couvre aussi l'exception.
        for (String a : ancestors) {
            if (isA(type, a)) {
                return true;
            }
        }
        return false;
    }
}
