package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise06_LogLineParser.
 */
public class Solution06_LogLineParser {

    public static String date(String line) {
        // Fin EXCLUE : 10 pour garder les index 0 a 9.
        return line.substring(0, 10);
    }

    public static String time(String line) {
        // Les index 11 a 15 : "HH:mm".
        return line.substring(11, 16);
    }

    public static String level(String line) {
        // Longueur variable : on cherche l'espace suivant a partir de 17.
        return line.substring(17, line.indexOf(' ', 17));
    }

    public static String message(String line) {
        // substring(debut) va jusqu'au bout ; + 1 pour sauter l'espace.
        return line.substring(line.indexOf(' ', 17) + 1);
    }

    public static boolean isValid(String line) {
        // Verifier AVANT de decouper : sinon substring/charAt lancent StringIndexOutOfBoundsException.
        if (line == null || line.length() < 18) {
            return false;
        }
        if (line.charAt(4) != '-' || line.charAt(7) != '-' || line.charAt(10) != ' '
                || line.charAt(13) != ':' || line.charAt(16) != ' ') {
            return false;
        }
        if (line.indexOf(' ', 17) < 0) {
            return false;
        }
        String level = level(line);
        return level.equals("INFO") || level.equals("WARN") || level.equals("ERROR");
    }

    public static String capitalizeWords(String text) {
        // Chaque String est immuable : on construit le resultat dans un StringBuilder.
        StringBuilder sb = new StringBuilder();
        for (String word : text.split(" ")) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1));
        }
        return sb.toString();
    }

    public static String pretty(String line) {
        // On assemble les petites boites ; la validation protege tous les substring.
        if (!isValid(line)) {
            return "?";
        }
        return "[" + level(line) + "] " + time(line) + " " + capitalizeWords(message(line));
    }
}
