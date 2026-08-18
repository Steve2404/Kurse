package exceptions.exercises;

import exceptions.ExerciseChecker;

import java.util.Locale;

/**
 * EXERCICE 9 - NumberFormat sensible a la Locale : devise, pourcentage, format compact (niveau : moyen/difficile)
 * ===============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CheckedVsUnchecked.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * NumberFormat sait fabriquer plusieurs sortes de formateurs TOUT
 * PRETS, qui s'adaptent AUTOMATIQUEMENT a la Locale donnee (symbole de
 * devise, position du signe %, separateurs...) - pas besoin d'ecrire
 * son propre pattern comme dans Exercise06 pour ces cas courants.
 *
 * -- Piege a eviter --
 *
 * Comme pour DateTimeFormatter (Exercise07), NE JAMAIS utiliser la
 * Locale par defaut de la machine dans du code dont le resultat doit
 * etre PREVISIBLE - TOUJOURS preciser explicitement la Locale voulue.
 *
 *
 * ==================================================================
 * TODO 1 : formatCurrency(amount, locale)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * NumberFormat.getCurrencyInstance(Locale.US).format(1234.5) ->
 * "$1,234.50" (symbole devise, separateur de milliers, et 2 decimales
 * AUTOMATIQUEMENT ajoutees).
 *
 * -- Le plan --
 *
 *   1. Renvoyer NumberFormat.getCurrencyInstance(locale).format(amount).
 *
 *
 * ==================================================================
 * TODO 2 : formatPercent(ratio, locale)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * NumberFormat.getPercentInstance(Locale.US).format(0.75) -> "75%"
 * (le ratio 0.75 est AUTOMATIQUEMENT multiplie par 100 et le signe %
 * ajoute).
 *
 * -- Le plan --
 *
 *   1. Renvoyer NumberFormat.getPercentInstance(locale).format(ratio).
 *
 *
 * ==================================================================
 * TODO 3 : formatCompact(value, locale)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * CompactNumberFormat abrege les grands nombres pour les rendre
 * faciles a lire d'un coup d'oeil (comme sur un compteur de "likes"
 * sur un reseau social) : 5000 devient "5K" (K pour mille), 2000000
 * devient "2M" (M pour million), au lieu d'ecrire tous les chiffres.
 *
 * -- Essayons a la main --
 *
 * NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT)
 * .format(5000) -> "5K".
 * ... .format(2_000_000) -> "2M".
 *
 * -- Le plan --
 *
 *   1. Renvoyer NumberFormat.getCompactNumberInstance(locale,
 *      NumberFormat.Style.SHORT).format(value).
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une seule ligne, c'est le CHOIX du bon
 * "getXxxInstance" qui est le vrai coeur de l'exercice.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - NumberFormat.Style.SHORT donne "5K" ; NumberFormat.Style.LONG
 *     donnerait quelque chose de plus long comme "5 thousand" - le
 *     SHORT est de loin le plus courant.
 */
public class Exercise09_LocaleSensitiveFormatting {

    public static String formatCurrency(double amount, Locale locale) {
        throw new UnsupportedOperationException("TODO 1 : implementer formatCurrency()");
    }

    public static String formatPercent(double ratio, Locale locale) {
        throw new UnsupportedOperationException("TODO 2 : implementer formatPercent()");
    }

    public static String formatCompact(long value, Locale locale) {
        throw new UnsupportedOperationException("TODO 3 : implementer formatCompact()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("formatCurrency(1234.5, US) == '$1,234.50'",
                formatCurrency(1234.5, Locale.US).equals("$1,234.50"));

        ExerciseChecker.check("formatPercent(0.75, US) == '75%'",
                formatPercent(0.75, Locale.US).equals("75%"));

        ExerciseChecker.check("formatCompact(5000, US) == '5K'", formatCompact(5000, Locale.US).equals("5K"));
        ExerciseChecker.check("formatCompact(2000000, US) == '2M'",
                formatCompact(2_000_000, Locale.US).equals("2M"));
        ExerciseChecker.check("formatCompact(999, US) == '999' (pas encore assez grand pour s'abreger)",
                formatCompact(999, Locale.US).equals("999"));

        ExerciseChecker.summary();
    }
}
