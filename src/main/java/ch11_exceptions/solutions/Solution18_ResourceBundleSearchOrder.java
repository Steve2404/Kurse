package ch11_exceptions.solutions;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Corrige de l'exercice 18. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch11_exceptions.exercises.Exercise18_ResourceBundleSearchOrder.
 */
public class Solution18_ResourceBundleSearchOrder {

    public static String lookup(String key, Locale locale) {
        // getBundle choisit le fichier le plus precis qui existe ; getString remonte ses parents si la cle manque.
        ResourceBundle bundle = ResourceBundle.getBundle("ch11_exceptions.messages", locale);
        return bundle.getString(key);
    }
}
