package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise05_StringCleaningAndFormatting.
 */
public class Solution05_StringCleaningAndFormatting {

    public static String normalizeSpaces(String text) {
        // strip enleve les bords ; "\\s+" coupe sur n'importe quel paquet de blancs (tabulation comprise).
        String clean = text.strip();
        if (clean.isEmpty()) {
            return "";
        }
        return String.join(" ", clean.split("\\s+"));
    }

    public static String tableRow(String name, int qty) {
        // Le signe - aligne a gauche ; sans lui, format aligne a droite.
        return String.format("%-6s|%4d", name, qty);
    }

    public static String frame(String text) {
        // repeat evite une boucle ; + 2 pour les espaces autour du mot.
        String border = "+" + "-".repeat(text.length() + 2) + "+";
        return border + "\n| " + text + " |\n" + border;
    }

    public static String[] csvFields(String line) {
        // Limite -1 : les champs vides de la fin sont gardes (split(",") les supprimerait).
        return line.split(",", -1);
    }

    public static long countNonBlankLines(String text) {
        // isBlank (pas isEmpty) : une ligne d'espaces ne compte pas.
        long count = 0;
        for (String line : text.split("\n")) {
            if (!line.isBlank()) {
                count++;
            }
        }
        return count;
    }

    public static String kind(String s) {
        // L'ordre compte : "" est aussi blanc, donc isEmpty est teste avant isBlank.
        if (s == null) {
            return "null";
        }
        if (s.isEmpty()) {
            return "vide";
        }
        if (s.isBlank()) {
            return "blanc";
        }
        return "texte";
    }
}
