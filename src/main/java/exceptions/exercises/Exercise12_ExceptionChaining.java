package exceptions.exercises;

import exceptions.ExerciseChecker;

/**
 * EXERCICE 12 - Chainer une exception avec sa cause d'origine (niveau : moyen/difficile)
 * ====================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CheckedVsUnchecked.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine que tu attrapes une petite exception TECHNIQUE, trop
 * precise pour interesser le code appelant (par exemple,
 * NumberFormatException, qui ne veut rien dire pour quelqu'un qui
 * importe des donnees metier), et que tu preferes la "traduire" en
 * une exception PLUS PARLANTE pour ton domaine (DataImportException :
 * "cet enregistrement est invalide"). Le probleme : si tu perds la
 * trace de l'exception TECHNIQUE d'origine, tu perds une information
 * precieuse pour deboguer plus tard (LE detail exact de ce qui a
 * rate). La solution : ATTACHER l'ancienne exception comme "cause" de
 * la nouvelle, au lieu de la jeter a la poubelle.
 *
 *
 * ==================================================================
 * TODO 1 : completer DataImportException
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Le constructeur recoit un message ET une cause (Throwable).
 *   2. Appeler super(message, cause) - le constructeur HERITE
 *      d'Exception qui accepte deja les deux, et memorise
 *      automatiquement la cause pour toi (recuperable plus tard via
 *      getCause()).
 *
 *
 * ==================================================================
 * TODO 2 : importRecord(raw)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * importRecord("42") -> 42 (aucun probleme, on renvoie simplement le
 * nombre).
 *
 * importRecord("abc") -> Integer.parseInt("abc") lance une
 * NumberFormatException (TECHNIQUE). On l'ATTRAPE, et on la
 * RETRADUIT en DataImportException("Enregistrement invalide : abc",
 * causeOriginale) avant de la relancer - le code appelant recoit une
 * exception PARLANTE pour son metier, sans jamais perdre le detail
 * technique d'origine (toujours consultable via getCause()).
 *
 * -- Le plan --
 *
 *   1. Essayer Integer.parseInt(raw).
 *   2. Si NumberFormatException est attrapee : lancer une NOUVELLE
 *      DataImportException, avec un message parlant qui inclut 'raw',
 *      et l'exception attrapee comme cause.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun est deja sa propre boite (un constructeur, une
 * methode).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - DataImportException(String message, Throwable cause) {
 *         super(message, cause);
 *     }
 *   - try {
 *         return Integer.parseInt(raw);
 *     } catch (NumberFormatException e) {
 *         throw new DataImportException("Enregistrement invalide : " + raw, e);
 *     }
 *   - e.getCause() renvoie EXACTEMENT l'objet exception d'origine
 *     passe au constructeur - pas une copie, pas un nouveau message :
 *     le MEME objet, avec son PROPRE getMessage() d'origine intact.
 */
public class Exercise12_ExceptionChaining {

    static class DataImportException extends Exception {
        DataImportException(String message, Throwable cause) {
            throw new UnsupportedOperationException("TODO 1 : implementer le constructeur");
        }
    }

    public static int importRecord(String raw) throws DataImportException {
        throw new UnsupportedOperationException("TODO 2 : implementer importRecord()");
    }

    public static void main(String[] args) throws DataImportException {
        ExerciseChecker.check("importRecord('42') == 42", importRecord("42") == 42);

        boolean caught = false;
        String message = null;
        Throwable cause = null;
        try {
            importRecord("abc");
        } catch (DataImportException e) {
            caught = true;
            message = e.getMessage();
            cause = e.getCause();
        }

        ExerciseChecker.check("importRecord('abc') lance DataImportException", caught);
        ExerciseChecker.check("le message mentionne l'enregistrement fautif",
                message != null && message.contains("abc"));
        ExerciseChecker.check("la cause d'origine (NumberFormatException) est preservee",
                cause instanceof NumberFormatException);
        ExerciseChecker.check("le message TECHNIQUE d'origine reste consultable via la cause",
                cause != null && cause.getMessage().contains("abc"));

        ExerciseChecker.summary();
    }
}
