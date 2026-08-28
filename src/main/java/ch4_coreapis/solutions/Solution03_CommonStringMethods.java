package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise03_CommonStringMethods.
 */
public class Solution03_CommonStringMethods {

    public static String extractWorld(String text) {
        return text.substring(7, 12);
    }

    public static String firstWord(String sentence) {
        int position = sentence.indexOf(' ');
        if (position == -1) {
            return sentence;
        }
        return sentence.substring(0, position);
    }

    public static String cleanedTrim(String text) {
        return text.strip();
    }
}
