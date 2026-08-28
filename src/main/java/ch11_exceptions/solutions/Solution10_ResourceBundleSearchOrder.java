package ch11_exceptions.solutions;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans exceptions.exercises.Exercise10_ResourceBundleSearchOrder.
 */
public class Solution10_ResourceBundleSearchOrder {

    public static String lookup(String key, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("ch11_exceptions.messages", locale);
        return bundle.getString(key);
    }
}
