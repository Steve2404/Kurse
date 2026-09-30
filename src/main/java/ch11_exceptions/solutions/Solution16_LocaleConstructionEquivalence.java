package ch11_exceptions.solutions;

import java.util.Locale;

/**
 * Corrige de l'exercice 16. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch11_exceptions.exercises.Exercise16_LocaleConstructionEquivalence.
 */
public class Solution16_LocaleConstructionEquivalence {

    public static Locale localeFromConstant() {
        // Une constante toute prete pour les locales courantes.
        return Locale.US;
    }

    public static Locale localeFromLegacyConstructor(String language, String country) {
        // Le constructeur (deprecie depuis Java 19, mais au programme du 17) donne une Locale equals aux autres.
        return new Locale(language, country);
    }

    public static Locale localeFromBuilder(String language, String country) {
        // Trois chemins, un seul resultat : equals compare langue, pays (et variante).
        return new Locale.Builder().setLanguage(language).setRegion(country).build();
    }
}
