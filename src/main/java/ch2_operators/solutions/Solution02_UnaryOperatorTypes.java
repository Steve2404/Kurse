package ch2_operators.solutions;

/**
 * Corrige de l'exercice 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise02_UnaryOperatorTypes.
 */
public class Solution02_UnaryOperatorTypes {

    public static boolean isNumeric(String type) {
        // char EST un type numerique (un code de 0 a 65535) ; boolean et String non.
        return isIntegral(type) || type.equals("float") || type.equals("double");
    }

    public static boolean isIntegral(String type) {
        // Les entiers : ceux sur lesquels ~, &, |, ^, << ont un sens.
        return type.equals("byte") || type.equals("short") || type.equals("char")
                || type.equals("int") || type.equals("long");
    }

    public static String unaryPromotion(String type) {
        // Java ne calcule jamais en byte/short/char : ils montent d'abord en int.
        if (type.equals("byte") || type.equals("short") || type.equals("char")) {
            return "int";
        }
        return type;
    }

    public static String unaryResultType(String operator, String type) {
        // ++ et -- gardent le type (cast cache) ; les autres operateurs appliquent la promotion.
        switch (operator) {
            case "!":
                return type.equals("boolean") ? "boolean" : "ERREUR";
            case "~":
                return isIntegral(type) ? unaryPromotion(type) : "ERREUR";
            case "-":
            case "+":
                return isNumeric(type) ? unaryPromotion(type) : "ERREUR";
            case "++":
            case "--":
                return isNumeric(type) ? type : "ERREUR";
            default:
                return "ERREUR";
        }
    }

    public static int complementByFormula(int x) {
        // Complement a deux : inverser tous les bits revient a -(x + 1). Meme les debordements
        // (MIN_VALUE, MAX_VALUE) tombent juste, car -(x + 1) deborde de la meme facon.
        return -(x + 1);
    }
}
