package exceptions.exercises;

import exceptions.ExerciseChecker;

/**
 * EXERCICE 6 - Formats de nombres personnalises, et messages avec MessageFormat (niveau : moyen/difficile)
 * =======================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CheckedVsUnchecked.java.
 *
 * -- DecimalFormat, en une phrase --
 *
 * Un pattern DecimalFormat decrit la FORME souhaitee avec des
 * symboles : '#' = un chiffre optionnel (n'apparait pas si inutile),
 * '0' = un chiffre OBLIGATOIRE (apparait meme si c'est un zero de
 * remplissage), ',' = separateur de milliers, '.' = separateur
 * decimal. TOUT texte place entre 2 apostrophes ('...') est du texte
 * LITTERAL, recopie tel quel, jamais interprete comme un symbole de
 * format.
 *
 *
 * ==================================================================
 * TODO 1 : formatAmount(amount)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Pattern "#,##0.00" applique a 1234.5 -> "1,234.50" (le "0.00" force
 * TOUJOURS 2 decimales, meme si le nombre d'origine n'en avait
 * qu'une ; le "#,##0" ajoute le separateur de milliers seulement si
 * necessaire).
 *
 * -- Le plan --
 *
 *   1. Construire un DecimalFormat avec le pattern "#,##0.00", et les
 *      symboles EXPLICITEMENT fixes sur Locale.US (pour ne JAMAIS
 *      dependre de la locale par defaut de la machine qui execute le
 *      code - un point important pour la reproductibilite).
 *   2. Renvoyer format.format(amount).
 *
 *
 * ==================================================================
 * TODO 2 : formatWithEscapedLiteral(amount)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Pattern "'Total:' #,##0.00 'EUR'" applique a 1234.5 -> "Total:
 * 1,234.50 EUR" (le texte entre apostrophes est recopie tel quel,
 * SANS etre interprete - "T", "o", "t"... ne sont pas des symboles de
 * pattern, mais on doit quand meme les proteger avec des apostrophes
 * des qu'on les MELANGE a de vrais symboles de pattern comme # ou 0).
 *
 * -- Le plan --
 *
 *   1. Meme principe que TODO 1, avec le pattern
 *      "'Total:' #,##0.00 'EUR'".
 *
 *
 * ==================================================================
 * TODO 3 : formatMessage(name, count)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * MessageFormat.format() est different de DecimalFormat : au lieu de
 * formater UN SEUL nombre, il remplit un GABARIT de phrase, avec des
 * emplacements numerotes {0}, {1}, {2}... remplaces DANS L'ORDRE par
 * les arguments fournis.
 *
 * -- Essayons a la main --
 *
 * MessageFormat.format("Bonjour {0}, vous avez {1} message(s).",
 * "Steve", 3) -> "Bonjour Steve, vous avez 3 message(s)."
 *
 * -- Le plan --
 *
 *   1. Renvoyer MessageFormat.format("Bonjour {0}, vous avez {1} message(s).",
 *      name, count).
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en 1-2 lignes, c'est le CHOIX et l'ECRITURE du
 * bon pattern qui est le vrai coeur de l'exercice.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - new DecimalFormat(pattern, DecimalFormatSymbols.getInstance(Locale.US))
 *     fixe les symboles (separateur decimal '.', separateur de
 *     milliers ',') independamment de la machine qui execute le code.
 *   - MessageFormat.format(gabarit, args...) accepte un nombre
 *     VARIABLE d'arguments (varargs Object...), autoboxes
 *     automatiquement les int comme 'count' en Integer.
 */
public class Exercise06_NumberAndMessageFormatting {

    public static String formatAmount(double amount) {
        throw new UnsupportedOperationException("TODO 1 : implementer formatAmount()");
    }

    public static String formatWithEscapedLiteral(double amount) {
        throw new UnsupportedOperationException("TODO 2 : implementer formatWithEscapedLiteral()");
    }

    public static String formatMessage(String name, int count) {
        throw new UnsupportedOperationException("TODO 3 : implementer formatMessage()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("formatAmount(1234.5) == '1,234.50'", formatAmount(1234.5).equals("1,234.50"));

        ExerciseChecker.check("formatWithEscapedLiteral(1234.5) == 'Total: 1,234.50 EUR'",
                formatWithEscapedLiteral(1234.5).equals("Total: 1,234.50 EUR"));

        ExerciseChecker.check("formatMessage('Steve', 3) == 'Bonjour Steve, vous avez 3 message(s).'",
                formatMessage("Steve", 3).equals("Bonjour Steve, vous avez 3 message(s)."));

        ExerciseChecker.summary();
    }
}
