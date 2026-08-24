package exceptions.exercises;

import exceptions.ExerciseChecker;

import java.text.ParseException;
import java.time.LocalDate;

/**
 * EXERCICE 11 - Relire un format personnalise avec parse() (niveau : difficile)
 * ===========================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CheckedVsUnchecked.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Jusqu'ici (Exercise06, Exercise07), on transformait une VALEUR en
 * TEXTE (format()). parse() fait exactement le CHEMIN INVERSE : on lui
 * donne un TEXTE deja ecrit dans un format precis, et il essaie d'en
 * RETROUVER la valeur d'origine. C'est le meme pattern qu'un decodeur
 * qui sait relire un message ecrit selon des regles connues.
 *
 * -- Piege interessant : 2 API JDK, 2 philosophies differentes --
 *
 * DecimalFormat.parse(String) est une methode ANCIENNE (java.text,
 * depuis Java 1.1) : elle declare "throws ParseException", une
 * exception CHECKED - le compilateur t'OBLIGE a la gerer.
 *
 * LocalDate.parse(text, formatter) est une methode MODERNE (java.time,
 * depuis Java 8) : en cas d'echec, elle lance DateTimeParseException,
 * qui est UNCHECKED (elle herite de RuntimeException via
 * DateTimeException) - le compilateur ne t'oblige a RIEN.
 *
 * C'est un excellent exemple concret pour reviser la difference
 * checked/unchecked de l'Exercise01 : la MEME idee (rater un parsing)
 * est modelisee DIFFEREMMENT selon l'epoque de l'API.
 *
 *
 * ==================================================================
 * TODO 1 : parseAmount(text)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * parseAmount("1,234.50") -> 1234.5 (le DecimalFormat comprend le
 * separateur de milliers ',' et le separateur decimal '.', car on lui
 * donne les MEMES symboles US que dans Exercise06).
 *
 * -- Le plan --
 *
 *   1. Construire le MEME DecimalFormat que dans
 *      Exercise06.formatAmount() (pattern "#,##0.00", symboles
 *      Locale.US).
 *   2. Appeler format.parse(text) (renvoie un Number - il peut s'agir
 *      d'un Long ou d'un Double selon le texte), puis .doubleValue()
 *      pour recuperer un double dans tous les cas.
 *   3. Cette methode doit declarer "throws ParseException" sur sa
 *      signature (deja fait plus bas) - exception CHECKED.
 *
 *
 * ==================================================================
 * TODO 2 : parseDate(text)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * parseDate("18/08/2026") -> LocalDate.of(2026, 8, 18) (le MEME
 * pattern "dd/MM/yyyy" que dans Exercise07.formatDate()).
 *
 * -- Le plan --
 *
 *   1. Construire le MEME DateTimeFormatter que dans
 *      Exercise07.formatDate() (pattern "dd/MM/yyyy").
 *   2. Renvoyer LocalDate.parse(text, formatter).
 *   3. Cette methode n'a PAS besoin de declarer "throws" (deja le cas
 *      plus bas) - DateTimeParseException est UNCHECKED.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en 2 lignes.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US))
 *     .parse(text).doubleValue();
 *   - DateTimeFormatter.ofPattern("dd/MM/yyyy") reutilise pour LA
 *     LECTURE (parse) comme pour L'ECRITURE (format) - c'est le MEME
 *     formatter dans les 2 sens.
 */
public class Exercise11_ParsingCustomFormats {

    public static double parseAmount(String text) throws ParseException {
        throw new UnsupportedOperationException("TODO 1 : implementer parseAmount()");
    }

    public static LocalDate parseDate(String text) {
        throw new UnsupportedOperationException("TODO 2 : implementer parseDate()");
    }

    public static void main(String[] args) throws ParseException {
        ExerciseChecker.check("parseAmount('1,234.50') == 1234.5", parseAmount("1,234.50") == 1234.5);

        boolean threwChecked = false;
        try {
            parseAmount("pas-un-nombre");
        } catch (ParseException e) {
            threwChecked = true;
        }
        ExerciseChecker.check("parseAmount(texte invalide) lance ParseException (checked)", threwChecked);

        ExerciseChecker.check("parseDate('18/08/2026') == LocalDate.of(2026,8,18)",
                parseDate("18/08/2026").equals(LocalDate.of(2026, 8, 18)));

        boolean threwUnchecked = false;
        try {
            parseDate("pas-une-date");
        } catch (java.time.format.DateTimeParseException e) {
            threwUnchecked = true;
        }
        ExerciseChecker.check("parseDate(texte invalide) lance DateTimeParseException (unchecked)",
                threwUnchecked);

        ExerciseChecker.summary();
    }
}
