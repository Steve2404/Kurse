package ch1_buildingblocks.solutions;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise09_WrappersInPractice.
 */
public class Solution09_WrappersInPractice {

    public static int parseQuantity(String text) {
        // parseInt refuse les espaces et les nombres trop grands : on nettoie d'abord,
        // puis on transforme l'exception en valeur "impossible" (-1) au lieu de planter.
        try {
            return Integer.parseInt(text.strip());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static boolean sameBoxedObject(int value) {
        // == compare les BOITES : valueOf reutilise les memes boites de -128 a 127 (cache),
        // en fabrique des neuves au-dela. Pour comparer des valeurs : equals().
        return Integer.valueOf(value) == Integer.valueOf(value);
    }

    public static int unboxOrZero(Integer box) {
        // L'unboxing automatique d'un null lance NullPointerException : on teste avant.
        return box == null ? 0 : box;
    }

    public static boolean parseFlag(String text) {
        // parseBoolean ne lance jamais d'exception : true seulement pour "true" (casse ignoree).
        return Boolean.parseBoolean(text);
    }

    public static int digitValue(char c) {
        // Le caractere '7' a le code 55 : getNumericValue donne la VALEUR representee.
        return Character.isDigit(c) ? Character.getNumericValue(c) : -1;
    }

    public static String receiptBlock() {
        // \s garde l'espace de fin (normalement supprime), \ colle la ligne suivante,
        // "  fin" garde son surplus d'indentation, et le """ seul sur sa ligne ajoute un \n final.
        return """
                Ticket\s
                pomme \
                x3
                  fin
                """;
    }
}
