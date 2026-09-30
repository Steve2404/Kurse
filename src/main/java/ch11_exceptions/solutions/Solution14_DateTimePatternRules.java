package ch11_exceptions.solutions;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Corrige de l'exercice 14.
 */
public class Solution14_DateTimePatternRules {

    public static String render(String pattern, LocalDateTime dt) {
        // Un seul parcours : apostrophes (texte brut ou '' = '), suites de lettres (cases), le reste est recopie.
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < pattern.length()) {
            char c = pattern.charAt(i);
            if (c == '\'') {
                // Texte entre apostrophes ; a l'interieur aussi, '' vaut une apostrophe ("'o''clock'" -> o'clock).
                i++;
                if (i < pattern.length() && pattern.charAt(i) == '\'') {
                    out.append('\'');
                    i++;
                    continue;
                }
                while (i < pattern.length()) {
                    if (pattern.charAt(i) == '\'') {
                        if (i + 1 < pattern.length() && pattern.charAt(i + 1) == '\'') {
                            out.append('\'');
                            i += 2;
                            continue;
                        }
                        i++;
                        break;
                    }
                    out.append(pattern.charAt(i));
                    i++;
                }
            } else if (Character.isLetter(c)) {
                int j = i;
                while (j < pattern.length() && pattern.charAt(j) == c) {
                    j++;
                }
                out.append(field(c, j - i, dt));
                i = j;
            } else {
                out.append(c);
                i++;
            }
        }
        return out.toString();
    }

    public static String formatError(String pattern, String type) {
        // Ca compile toujours : c'est A L'EXECUTION qu'un champ absent du type lance l'exception. Les lettres entre ' ne comptent pas.
        String letters = lettersOutsideQuotes(pattern);
        String forbidden = type.equals("LocalDate") ? "Hhmsa" : "yMdE";
        for (char c : letters.toCharArray()) {
            if (forbidden.indexOf(c) >= 0) {
                return "UnsupportedTemporalTypeException";
            }
        }
        return "OK";
    }

    private static String field(char letter, int count, LocalDateTime dt) {
        // La lettre choisit le champ, le nombre de repetitions choisit la forme (chiffres, abrege, complet).
        switch (letter) {
            case 'y':
                return count == 2 ? pad(dt.getYear() % 100, 2) : pad(dt.getYear(), count);
            case 'M':
                if (count >= 3) {
                    return dt.getMonth().getDisplayName(count == 3 ? TextStyle.SHORT : TextStyle.FULL, Locale.US);
                }
                return pad(dt.getMonthValue(), count);
            case 'd':
                return pad(dt.getDayOfMonth(), count);
            case 'E':
                return dt.getDayOfWeek().getDisplayName(count == 4 ? TextStyle.FULL : TextStyle.SHORT, Locale.US);
            case 'H':
                return pad(dt.getHour(), count);
            case 'h':
                return pad(dt.getHour() % 12 == 0 ? 12 : dt.getHour() % 12, count);
            case 'm':
                return pad(dt.getMinute(), count);
            case 's':
                return pad(dt.getSecond(), count);
            case 'a':
                return dt.getHour() < 12 ? "AM" : "PM";
            default:
                throw new IllegalArgumentException("lettre non geree : " + letter);
        }
    }

    private static String pad(int value, int width) {
        // Zeros a gauche jusqu'a width chiffres ; une valeur plus longue n'est jamais coupee.
        return String.format("%0" + width + "d", value);
    }

    private static String lettersOutsideQuotes(String pattern) {
        // Meme regle d'apostrophes que render : on saute tout ce qui est entre deux '.
        StringBuilder letters = new StringBuilder();
        boolean quoted = false;
        for (char c : pattern.toCharArray()) {
            if (c == '\'') {
                quoted = !quoted;
            } else if (!quoted && Character.isLetter(c)) {
                letters.append(c);
            }
        }
        return letters.toString();
    }
}
