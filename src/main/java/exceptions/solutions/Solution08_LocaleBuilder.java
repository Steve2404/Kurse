package exceptions.solutions;

import java.util.Locale;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans exceptions.exercises.Exercise08_LocaleBuilder.
 */
public class Solution08_LocaleBuilder {

    public static Locale buildLocale(String language, String country) {
        if (country == null || country.isBlank()) {
            return new Locale.Builder().setLanguage(language).build();
        }
        return new Locale.Builder().setLanguage(language).setRegion(country).build();
    }
}
