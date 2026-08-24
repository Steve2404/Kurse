package exceptions.exercises;

import exceptions.ExerciseChecker;

import java.util.Locale;

/**
 * EXERCICE 13 - 3 chemins differents, la MEME Locale a l'arrivee (niveau : moyen)
 * ============================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CheckedVsUnchecked.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le JDK offre PLUSIEURS "recettes" differentes pour fabriquer la
 * MEME Locale - un peu comme on peut arriver au meme endroit en
 * marchant, a velo, ou en voiture. Cet exercice le prouve avec
 * .equals() : 3 recettes DIFFERENTES, un resultat EGAL.
 *
 *   1. Une CONSTANTE deja toute prete dans la classe Locale
 *      (Locale.US, Locale.FRANCE, Locale.GERMANY...) - pratique pour
 *      les pays/langues les plus courants.
 *   2. Le constructeur "classique" a 2 arguments : new
 *      Locale(langue, pays).
 *   3. Locale.Builder (deja utilise a l'Exercise08) - le plus
 *      explicite, utile quand on construit le code de langue/pays a
 *      partir de variables.
 *
 *
 * ==================================================================
 * TODO 1, 2, 3 : localeFromConstant() / localeFromLegacyConstructor(...) / localeFromBuilder(...)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   TODO 1 : renvoyer directement la constante Locale.US.
 *   TODO 2 : renvoyer new Locale(language, country).
 *   TODO 3 : renvoyer new Locale.Builder().setLanguage(language)
 *            .setRegion(country).build() (comme a l'Exercise08).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une seule ligne, c'est le CHOIX de la bonne
 * "recette" pour chaque situation qui compte.
 *
 * Exemple a verifier : localeFromConstant(), localeFromLegacyConstructor("en", "US")
 * et localeFromBuilder("en", "US") sont TOUS LES 3 .equals() entre eux,
 * malgre 3 facons totalement differentes de les avoir construits.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Locale redefinit equals()/hashCode() en comparant langue + pays
 *     (+ variante), PAS la maniere dont l'objet a ete construit - 2
 *     Locale "logiquement identiques" sont TOUJOURS egales, quel que
 *     soit leur chemin de fabrication.
 */
public class Exercise13_LocaleConstructionEquivalence {

    public static Locale localeFromConstant() {
        throw new UnsupportedOperationException("TODO 1 : implementer localeFromConstant()");
    }

    public static Locale localeFromLegacyConstructor(String language, String country) {
        throw new UnsupportedOperationException("TODO 2 : implementer localeFromLegacyConstructor()");
    }

    public static Locale localeFromBuilder(String language, String country) {
        throw new UnsupportedOperationException("TODO 3 : implementer localeFromBuilder()");
    }

    public static void main(String[] args) {
        Locale fromConstant = localeFromConstant();
        Locale fromLegacy = localeFromLegacyConstructor("en", "US");
        Locale fromBuilder = localeFromBuilder("en", "US");

        ExerciseChecker.check("constante Locale.US == constructeur classique new Locale('en','US')",
                fromConstant.equals(fromLegacy));
        ExerciseChecker.check("constructeur classique == Locale.Builder", fromLegacy.equals(fromBuilder));
        ExerciseChecker.check("constante == Locale.Builder (par transitivite)", fromConstant.equals(fromBuilder));

        ExerciseChecker.check("langue == 'en' partout",
                fromConstant.getLanguage().equals("en") && fromLegacy.getLanguage().equals("en")
                        && fromBuilder.getLanguage().equals("en"));
        ExerciseChecker.check("pays == 'US' partout",
                fromConstant.getCountry().equals("US") && fromLegacy.getCountry().equals("US")
                        && fromBuilder.getCountry().equals("US"));

        ExerciseChecker.summary();
    }
}
