package ch5_methods.solutions;

import java.util.Arrays;

/**
 * Corrige de l'exercice 17. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch5_methods.exercises.Exercise17_OverloadResolver.
 */
public class Solution17_OverloadResolver {

    public static final String[] PRIMITIVES = {"byte", "short", "int", "long", "float", "double"};

    public static boolean widens(String from, String to) {
        // Elargir = aller vers la droite (ou rester) dans byte < short < int < long < float < double.
        int f = index(from);
        int t = index(to);
        return f >= 0 && t >= 0 && t >= f;
    }

    private static int index(String type) {
        // -1 pour une reference : pratique pour savoir si c'est un primitif.
        return Arrays.asList(PRIMITIVES).indexOf(type);
    }

    public static String boxOf(String primitive) {
        // Chaque primitif a SA boite ; un short ne devient jamais un Integer.
        return switch (primitive) {
            case "short" -> "Short";
            case "int" -> "Integer";
            case "long" -> "Long";
            default -> throw new IllegalArgumentException(primitive);
        };
    }

    public static boolean acceptsReference(String argRef, String candidate) {
        // Une boite va vers elle-meme, ou vers ses super-types Number et Object.
        return candidate.equals(argRef) || candidate.equals("Number") || candidate.equals("Object");
    }

    public static String resolve(String arg, String... candidates) {
        // Trois tours ; le premier tour qui trouve un candidat decide.
        boolean primitiveArg = index(arg) >= 0;
        String best = null;
        for (String c : candidates) {
            boolean ok = !c.endsWith("...") && (primitiveArg ? widens(arg, c) : index(c) < 0 && acceptsReference(arg, c));
            best = ok ? moreSpecific(best, c) : best;
        }
        if (best != null) {
            return best;
        }
        for (String c : candidates) {
            boolean ok = !c.endsWith("...") && (primitiveArg ? index(c) < 0 && acceptsReference(boxOf(arg), c) : widens("int", c));
            best = ok ? moreSpecific(best, c) : best;
        }
        if (best != null) {
            return best;
        }
        for (String c : candidates) {
            if (c.endsWith("...")) {
                String element = c.substring(0, c.length() - 3);
                boolean ok = index(element) >= 0
                        ? widens(primitiveArg ? arg : "int", element)
                        : acceptsReference(primitiveArg ? boxOf(arg) : arg, element);
                if (ok) {
                    return c;
                }
            }
        }
        return "none";
    }

    private static String moreSpecific(String current, String candidate) {
        // Petite boite : le plus petit primitif, ou la boite exacte avant Number avant Object.
        if (current == null) {
            return candidate;
        }
        return rank(candidate) < rank(current) ? candidate : current;
    }

    private static int rank(String type) {
        // Primitifs : leur index ; references : 0 pour une boite, 1 pour Number, 2 pour Object.
        if (index(type) >= 0) {
            return index(type);
        }
        return switch (type) {
            case "Number" -> 1;
            case "Object" -> 2;
            default -> 0;
        };
    }
}
