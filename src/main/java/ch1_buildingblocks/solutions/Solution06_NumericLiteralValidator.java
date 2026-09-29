package ch1_buildingblocks.solutions;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise06_NumericLiteralValidator.
 */
public class Solution06_NumericLiteralValidator {

    public static boolean startsWithDigit(String literal) {
        // Un litteral numerique commence TOUJOURS par un chiffre : "_1000" est un identifiant.
        return !literal.isEmpty() && isDigit(literal.charAt(0));
    }

    public static boolean underscoresWellPlaced(String literal) {
        // On traite chaque GROUPE de _ d'un bloc : "1__000" est permis, seules les
        // deux rives du groupe doivent etre des chiffres.
        int i = 0;
        while (i < literal.length()) {
            if (literal.charAt(i) != '_') {
                i++;
                continue;
            }
            if (i == 0 || !isDigit(literal.charAt(i - 1))) {
                return false;
            }
            while (i < literal.length() && literal.charAt(i) == '_') {
                i++;
            }
            if (i == literal.length() || !isDigit(literal.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isDigit(char c) {
        // Plus strict que Character.isDigit, qui accepte aussi les chiffres d'autres alphabets.
        return c >= '0' && c <= '9';
    }

    public static boolean isValidDecimalLiteral(String literal) {
        // Les deux boites magiques ensemble : c'est un nombre ET les _ sont bien places.
        return startsWithDigit(literal) && underscoresWellPlaced(literal);
    }

    public static long numericValue(String literal) {
        // Les _ sont purement decoratifs ; le suffixe L (ou l) dit seulement "c'est un long".
        String digits = literal.replace("_", "");
        if (digits.endsWith("L") || digits.endsWith("l")) {
            digits = digits.substring(0, digits.length() - 1);
        }
        return Long.parseLong(digits);
    }
}
