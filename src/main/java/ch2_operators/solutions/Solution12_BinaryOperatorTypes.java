package ch2_operators.solutions;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise12_BinaryOperatorTypes.
 */
public class Solution12_BinaryOperatorTypes {

    public static boolean isNumeric(String type) {
        // char compte comme un nombre (un code 0..65535).
        return isIntegral(type) || type.equals("float") || type.equals("double");
    }

    public static boolean isIntegral(String type) {
        // Les seuls types acceptes par & | ^ (en version bits) et par les decalages.
        return type.equals("byte") || type.equals("short") || type.equals("char")
                || type.equals("int") || type.equals("long");
    }

    public static boolean isReference(String type) {
        // Dans cet exercice, les references sont String et le litteral null.
        return type.equals("String") || type.equals("null");
    }

    public static String promote(String left, String right) {
        // Promotion binaire : le plus "grand" gagne, et jamais plus petit qu'un int.
        if (left.equals("double") || right.equals("double")) {
            return "double";
        }
        if (left.equals("float") || right.equals("float")) {
            return "float";
        }
        if (left.equals("long") || right.equals("long")) {
            return "long";
        }
        return "int";
    }

    public static String promoteOne(String type) {
        // Promotion unaire : utilisee par les decalages, qui ne regardent que l'operande de gauche.
        return type.equals("byte") || type.equals("short") || type.equals("char") ? "int" : type;
    }

    public static String resultType(String operator, String left, String right) {
        // Un groupe de "case" par regle ; tout ce qui ne rentre dans aucune regle est une ERREUR.
        boolean bothNumeric = isNumeric(left) && isNumeric(right);
        boolean bothBoolean = left.equals("boolean") && right.equals("boolean");
        boolean bothIntegral = isIntegral(left) && isIntegral(right);
        switch (operator) {
            case "+":
                if (left.equals("String") || right.equals("String")) {
                    return "String";
                }
                return bothNumeric ? promote(left, right) : "ERREUR";
            case "-":
            case "*":
            case "/":
            case "%":
                return bothNumeric ? promote(left, right) : "ERREUR";
            case "<":
            case "<=":
            case ">":
            case ">=":
                return bothNumeric ? "boolean" : "ERREUR";
            case "==":
            case "!=":
                return bothNumeric || bothBoolean || (isReference(left) && isReference(right)) ? "boolean" : "ERREUR";
            case "&&":
            case "||":
                return bothBoolean ? "boolean" : "ERREUR";
            case "&":
            case "|":
            case "^":
                if (bothBoolean) {
                    return "boolean";
                }
                return bothIntegral ? promote(left, right) : "ERREUR";
            case "<<":
            case ">>":
            case ">>>":
                return bothIntegral ? promoteOne(left) : "ERREUR";
            default:
                return "ERREUR";
        }
    }
}
