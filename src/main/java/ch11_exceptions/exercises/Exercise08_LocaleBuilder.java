package ch11_exceptions.exercises;

import ch11_exceptions.ExerciseChecker;

/**
 * EXERCICE 8 - Construire une Locale avec Locale.Builder (niveau : moyen)
 * ====================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CheckedVsUnchecked.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une Locale, c'est une "carte d'identite regionale" pour un
 * programme : elle dit dans quelle LANGUE et dans quel PAYS on se
 * trouve, pour adapter automatiquement les formats de dates, de
 * nombres, etc. Le code de langue est TOUJOURS en minuscules et
 * OBLIGATOIRE (ex: "fr", "en", "de"). Le code de pays est TOUJOURS en
 * MAJUSCULES et FACULTATIF (ex: "FR", "US", "CA") - on peut tres bien
 * avoir une langue sans pays precis (juste "fr", le francais en
 * general), mais jamais un pays sans langue.
 *
 *
 * ==================================================================
 * TODO : buildLocale(language, country)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * buildLocale("fr", "FR") -> une Locale dont toString() == "fr_FR"
 * (francais de France).
 * buildLocale("en", null) -> une Locale dont toString() == "en"
 * (anglais general, sans pays precis - PAS de underscore final).
 *
 * -- Le plan --
 *
 *   1. Si country est null OU country.isBlank() : construire la
 *      Locale avec SEULEMENT la langue (via Locale.Builder).
 *   2. Sinon : construire la Locale avec la langue ET le pays.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule methode avec un if/else suffit.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - new Locale.Builder().setLanguage(language).setRegion(country).build();
 *   - new Locale.Builder().setLanguage(language).build(); (sans
 *     .setRegion(...) du tout, si aucun pays n'est fourni)
 *   - Locale.Builder EXIGE deja des codes bien formes (minuscules
 *     pour la langue, majuscules pour le pays) - il lance lui-meme
 *     une exception si on lui donne un code invalide, pas la peine
 *     de re-verifier la casse a la main.
 */
public class Exercise08_LocaleBuilder {

    public static java.util.Locale buildLocale(String language, String country) {
        throw new UnsupportedOperationException("TODO : implementer buildLocale()");
    }

    public static void main(String[] args) {
        java.util.Locale frFR = buildLocale("fr", "FR");
        ExerciseChecker.check("buildLocale('fr','FR').toString() == 'fr_FR'", frFR.toString().equals("fr_FR"));
        ExerciseChecker.check("buildLocale('fr','FR').getLanguage() == 'fr'", frFR.getLanguage().equals("fr"));
        ExerciseChecker.check("buildLocale('fr','FR').getCountry() == 'FR'", frFR.getCountry().equals("FR"));

        java.util.Locale en = buildLocale("en", null);
        ExerciseChecker.check("buildLocale('en', null).toString() == 'en' (pas de pays)", en.toString().equals("en"));
        ExerciseChecker.check("buildLocale('en', null).getCountry() est vide", en.getCountry().isEmpty());

        java.util.Locale deWithBlankCountry = buildLocale("de", "");
        ExerciseChecker.check("buildLocale('de', '') == 'de' (chaine vide traitee comme absente)",
                deWithBlankCountry.toString().equals("de"));

        ExerciseChecker.summary();
    }
}
