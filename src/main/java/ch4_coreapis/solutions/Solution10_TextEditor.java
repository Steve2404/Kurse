package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise10_TextEditor.
 */
public class Solution10_TextEditor {

    public static String render(StringBuilder text, int cursor) {
        // Une COPIE : insert sur text lui-meme ajouterait un '|' pour toujours.
        return new StringBuilder(text).insert(cursor, '|').toString();
    }

    public static int clamp(int value, int min, int max) {
        // min puis max : la valeur est coincee entre les deux bornes.
        return Math.max(min, Math.min(max, value));
    }

    public static int apply(StringBuilder text, int cursor, String command) {
        // Le switch expression choisit l'action ; chaque branche rend la nouvelle position.
        int space = command.indexOf(' ');
        String name = space < 0 ? command : command.substring(0, space);
        String arg = space < 0 ? "" : command.substring(space + 1);
        int length = text.length();
        return switch (name) {
            case "type" -> {
                text.insert(cursor, arg);
                yield cursor + arg.length();
            }
            case "left" -> clamp(cursor - Integer.parseInt(arg), 0, length);
            case "right" -> clamp(cursor + Integer.parseInt(arg), 0, length);
            case "back" -> {
                int start = clamp(cursor - Integer.parseInt(arg), 0, cursor);
                text.delete(start, cursor);
                yield start;
            }
            case "del" -> {
                text.delete(cursor, clamp(cursor + Integer.parseInt(arg), 0, length));
                yield cursor;
            }
            case "home" -> 0;
            case "end" -> length;
            case "upper" -> {
                for (int i = 0; i < length; i++) {
                    text.setCharAt(i, Character.toUpperCase(text.charAt(i)));
                }
                yield cursor;
            }
            default -> throw new IllegalArgumentException("commande inconnue : " + name);
        };
    }

    public static String run(String... commands) {
        // Un seul StringBuilder modifie commande apres commande.
        StringBuilder text = new StringBuilder();
        int cursor = 0;
        for (String command : commands) {
            cursor = apply(text, cursor, command);
        }
        return render(text, cursor);
    }
}
