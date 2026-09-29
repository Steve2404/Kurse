package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise09_StringBuilderOperations.
 */
public class Solution09_StringBuilderOperations {

    public static String reverseWords(String sentence) {
        // insert(0, ...) met chaque nouveau mot devant les precedents : l'ordre s'inverse.
        StringBuilder sb = new StringBuilder();
        for (String word : sentence.split(" ")) {
            if (sb.length() > 0) {
                sb.insert(0, ' ');
            }
            sb.insert(0, word);
        }
        return sb.toString();
    }

    public static void removeVowels(StringBuilder sb) {
        // De la fin vers le debut : une suppression ne decale que ce qu'on a deja vu.
        for (int i = sb.length() - 1; i >= 0; i--) {
            if ("aeiouy".indexOf(sb.charAt(i)) >= 0) {
                sb.deleteCharAt(i);
            }
        }
    }

    public static String censor(String text, String word) {
        // replace(debut, fin, texte) : fin EXCLUE ; on repart apres le mot remplace.
        StringBuilder sb = new StringBuilder(text);
        int length = word.length();
        int i = sb.indexOf(word);
        while (i >= 0) {
            sb.replace(i, i + length, "*".repeat(length));
            i = sb.indexOf(word, i + length);
        }
        return sb.toString();
    }

    public static String formatPhone(String digits) {
        // Inserer depuis la fin : les positions de devant restent valides.
        StringBuilder sb = new StringBuilder(digits);
        for (int i = digits.length() - 2; i > 0; i -= 2) {
            sb.insert(i, ' ');
        }
        return sb.toString();
    }

    public static String truncate(String text, int max) {
        // setLength coupe net ; on garde 3 places pour "...".
        if (text.length() <= max) {
            return text;
        }
        StringBuilder sb = new StringBuilder(text);
        sb.setLength(max - 3);
        return sb.append("...").toString();
    }

    public static void toggleCase(StringBuilder sb) {
        // setCharAt remplace une case sans changer la longueur (et rend void).
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.setCharAt(i, Character.toLowerCase(c));
            } else {
                sb.setCharAt(i, Character.toUpperCase(c));
            }
        }
    }
}
