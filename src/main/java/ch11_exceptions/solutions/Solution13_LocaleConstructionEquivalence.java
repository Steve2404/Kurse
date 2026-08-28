package ch11_exceptions.solutions;

import java.util.Locale;

/**
 * Corrige de l'exercice 13. A ne consulter qu'apres avoir essaye par
 * vous-meme dans exceptions.exercises.Exercise13_LocaleConstructionEquivalence.
 */
public class Solution13_LocaleConstructionEquivalence {

    public static Locale localeFromConstant() {
        return Locale.US;
    }

    public static Locale localeFromLegacyConstructor(String language, String country) {
        return new Locale(language, country);
    }

    public static Locale localeFromBuilder(String language, String country) {
        return new Locale.Builder().setLanguage(language).setRegion(country).build();
    }
}
