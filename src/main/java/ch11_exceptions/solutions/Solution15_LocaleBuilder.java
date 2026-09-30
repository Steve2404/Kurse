package ch11_exceptions.solutions;

import java.util.Locale;

/**
 * Corrige de l'exercice 15. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch11_exceptions.exercises.Exercise15_LocaleBuilder.
 */
public class Solution15_LocaleBuilder {

    public static Locale buildLocale(String language, String country) {
        // Le Builder valide et normalise (FR -> fr) ; sans pays, on ne fixe que la langue.
        if (country == null || country.isBlank()) {
            return new Locale.Builder().setLanguage(language).build();
        }
        return new Locale.Builder().setLanguage(language).setRegion(country).build();
    }
}
