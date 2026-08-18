package exceptions.exercises;

import exceptions.ExerciseChecker;

import java.time.LocalDate;

/**
 * EXERCICE 7 - Formats de date personnalises avec DateTimeFormatter (niveau : moyen)
 * ===============================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CheckedVsUnchecked.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * DateTimeFormatter.ofPattern(pattern) fonctionne comme DecimalFormat
 * (Exercise06), mais pour les dates : des LETTRES repetees decrivent
 * la forme voulue (dd = jour sur 2 chiffres, MM = mois sur 2 chiffres,
 * MMMM = nom COMPLET du mois en toutes lettres, yyyy = annee sur 4
 * chiffres), et le texte entre apostrophes ('...') est recopie tel
 * quel, jamais interprete.
 *
 *
 * ==================================================================
 * TODO 1 : formatDate(date)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Pattern "dd/MM/yyyy" applique a 18 aout 2026 -> "18/08/2026".
 *
 * -- Le plan --
 *
 *   1. Construire un DateTimeFormatter avec ofPattern("dd/MM/yyyy").
 *   2. Renvoyer formatter.format(date).
 *
 *
 * ==================================================================
 * TODO 2 : formatWithFrenchMonthName(date)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * MMMM (4 fois M) affiche le nom du mois EN TOUTES LETTRES - mais
 * dans QUELLE langue ? Ca depend de la Locale donnee au formatter :
 * sans le dire explicitement, Java utiliserait la Locale par defaut
 * de la machine (imprevisible dans un test !). Il faut donc TOUJOURS
 * preciser la Locale voulue quand le pattern contient un nom en
 * toutes lettres.
 *
 * -- Essayons a la main --
 *
 * Pattern "'Le' dd MMMM yyyy" avec Locale.FRENCH, applique a 18 aout
 * 2026 -> "Le 18 août 2026" ("Le " est du texte litteral protege par
 * des apostrophes, le nom du mois "août" vient de la Locale
 * francaise).
 *
 * -- Le plan --
 *
 *   1. Construire un DateTimeFormatter avec ofPattern("'Le' dd MMMM yyyy",
 *      Locale.FRENCH) - la Locale se donne en DEUXIEME argument de
 *      ofPattern(), pas apres coup.
 *   2. Renvoyer formatter.format(date).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en 2 lignes, c'est le CHOIX du bon pattern (et
 * de la Locale, quand un nom en toutes lettres est demande) qui est
 * le vrai coeur de l'exercice.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - DateTimeFormatter.ofPattern(pattern) SANS Locale utilise la
 *     Locale par defaut de la JVM - a EVITER des qu'un test doit
 *     rester reproductible partout.
 *   - DateTimeFormatter.ofPattern(pattern, locale) fixe la Locale une
 *     fois pour toutes, independamment de la machine qui execute le
 *     code.
 */
public class Exercise07_CustomDateFormatting {

    public static String formatDate(LocalDate date) {
        throw new UnsupportedOperationException("TODO 1 : implementer formatDate()");
    }

    public static String formatWithFrenchMonthName(LocalDate date) {
        throw new UnsupportedOperationException("TODO 2 : implementer formatWithFrenchMonthName()");
    }

    public static void main(String[] args) {
        LocalDate date = LocalDate.of(2026, 8, 18);

        ExerciseChecker.check("formatDate == '18/08/2026'", formatDate(date).equals("18/08/2026"));

        ExerciseChecker.check("formatWithFrenchMonthName == 'Le 18 août 2026'",
                formatWithFrenchMonthName(date).equals("Le 18 août 2026"));

        ExerciseChecker.summary();
    }
}
