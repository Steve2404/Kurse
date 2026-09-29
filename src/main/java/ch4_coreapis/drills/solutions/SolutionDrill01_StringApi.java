package ch4_coreapis.drills.solutions;

import ch4_coreapis.drills.Journal;

/**
 * Corrige du drill 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.drills.exercises.Drill01_StringApi.
 */
public class SolutionDrill01_StringApi {

    public static int titleLength() {
        // length() est une METHODE pour String (length sans parentheses est pour les tableaux).
        return Journal.TITLE.length();
    }

    public static String cleanTitle() {
        // strip enleve les blancs des deux bords, y compris Unicode.
        return Journal.TITLE.strip();
    }

    public static char levelChar() {
        // charAt rend un char, pas un String.
        return Journal.LINE.charAt(17);
    }

    public static String datePart() {
        // Fin EXCLUE : 10 garde les index 0 a 9.
        return Journal.LINE.substring(0, 10);
    }

    public static String messagePart() {
        // Un seul argument : jusqu'a la fin.
        return Journal.LINE.substring(22);
    }

    public static int firstSpace() {
        // indexOf accepte un char ou un String.
        return Journal.LINE.indexOf(' ');
    }

    public static int secondSpace() {
        // Le depart est INCLUS : depuis 11 pour ne pas retrouver l'espace de l'index 10.
        return Journal.LINE.indexOf(' ', 11);
    }

    public static int lastSpace() {
        // lastIndexOf cherche depuis la fin.
        return Journal.LINE.lastIndexOf(' ');
    }

    public static boolean isWarning() {
        // contains cherche un morceau n'importe ou.
        return Journal.LINE.contains("WARN");
    }

    public static boolean startsIn2024() {
        // startsWith compare le debut exact.
        return Journal.LINE.startsWith("2024");
    }

    public static String shout() {
        // Immuable : toUpperCase rend un NOUVEAU String.
        return Journal.LINE.substring(22).toUpperCase();
    }

    public static boolean sameIgnoringCase() {
        // equalsIgnoreCase ignore majuscules et minuscules ; equals ne le fait pas.
        return "alpha".equalsIgnoreCase(Journal.WORDS[1]);
    }

    public static String dashes() {
        // repeat(n) remplace une boucle de concatenation.
        return "-".repeat(5);
    }

    public static String noSpaces() {
        // replace(char, char) remplace TOUTES les occurrences (pas seulement la premiere).
        return Journal.LINE.substring(22).replace(' ', '_');
    }

    public static boolean isBlankTitle() {
        // isBlank : vide OU que des espaces ; isEmpty : longueur 0 seulement.
        return "   ".isBlank();
    }

    public static String joinWords() {
        // String.join accepte un tableau (varargs) ou une collection.
        return String.join(", ", Journal.WORDS);
    }

    public static int wordCount() {
        // split rend un tableau : on prend sa length (sans parentheses).
        return Journal.LINE.substring(22).split(" ").length;
    }

    public static int compareFirstTwo() {
        // Premiere lettre differente : 'd' (100) - 'A' (65) = 35.
        return Journal.WORDS[0].compareTo(Journal.WORDS[1]);
    }

    public static String charPlusOne() {
        // 'a' + 1 est une addition d'entiers (98) ; la concatenation n'arrive qu'avec "".
        return 'a' + 1 + "";
    }
}
