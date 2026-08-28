package ch11_exceptions.exercises;

import ch11_exceptions.ExerciseChecker;

/**
 * EXERCICE 1 - Creer sa propre exception checked et sa propre exception unchecked (niveau : moyen/difficile)
 * =========================================================================================================================
 *
 * -- Rappel du decoupage en "boites magiques" --
 *
 * Une methode, c'est une boite magique : tu la nourris d'ingredients
 * (parametres), et elle rend un resultat, sans que tu aies besoin de
 * savoir comment elle travaille dedans. Pour CHAQUE etape d'un plan,
 * demande-toi : est-ce qu'elle se raconte seule ? revient-elle
 * plusieurs fois ? cache-t-elle sa propre petite recette ? Si oui a au
 * moins une question, elle merite sa propre boite.
 *
 * -- Checked vs unchecked, en une phrase chacune --
 *
 *   - CHECKED (herite de Exception, mais pas de RuntimeException) :
 *     le compilateur t'OBLIGE a la geree (try/catch) ou a la declarer
 *     (throws) - typiquement pour des problemes PREVISIBLES et
 *     RECUPERABLES (fonds insuffisants, fichier absent...).
 *   - UNCHECKED (herite de RuntimeException) : le compilateur ne
 *     t'oblige a RIEN - typiquement pour des erreurs de PROGRAMMATION
 *     (un identifiant invalide passe par erreur) qu'on ne s'attend
 *     PAS a devoir gerer partout.
 *
 *
 * ==================================================================
 * TODO 1 : completer InsufficientFundsException
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une exception, c'est un objet comme un autre : elle peut avoir ses
 * PROPRES champs, en plus du message standard. Ici, en plus du
 * message d'erreur classique, on veut memoriser DE COMBIEN il
 * manquait d'argent (le "manque a gagner", deja declare comme champ
 * plus bas), pour que le code qui attrape l'exception puisse
 * l'afficher precisement.
 *
 * -- Le plan --
 *
 *   1. Le constructeur recoit un message ET le montant manquant :
 *      appeler super(message) (pour que le message standard
 *      fonctionne normalement, y compris dans la stack trace), PUIS
 *      ranger shortfall dans le champ deja declare.
 *   2. Ecrire le getter getShortfall(), qui renvoie simplement ce
 *      champ.
 *
 *
 * ==================================================================
 * TODO 2 : withdraw(balance, amount)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Si amount > balance : lancer une InsufficientFundsException,
 *      avec un message parlant et shortfall = amount - balance.
 *   2. Sinon : renvoyer balance - amount.
 *
 *
 * ==================================================================
 * TODO 3 : validateAccountId(id)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un identifiant de compte null ou vide, c'est une ERREUR DE
 * PROGRAMMATION (quelqu'un a mal appele le code), pas un cas metier
 * normal a gerer partout - InvalidAccountException (deja fournie plus
 * bas, unchecked) convient mieux ici qu'une exception checked.
 *
 * -- Le plan --
 *
 *   1. Si id est null OU id.isBlank(), lancer une
 *      InvalidAccountException avec un message parlant.
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun est deja sa propre boite (une methode ou un
 * constructeur dedie).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - InsufficientFundsException(String message, double shortfall) {
 *         super(message);
 *         this.shortfall = shortfall;
 *     }
 *   - withdraw() DOIT declarer "throws InsufficientFundsException"
 *     dans sa signature (exception CHECKED - le compilateur
 *     l'exige), alors que validateAccountId() n'a PAS besoin de
 *     declarer "throws InvalidAccountException" (exception
 *     UNCHECKED - facultatif, meme si on peut le faire par
 *     lisibilite).
 */
public class Exercise01_CheckedVsUnchecked {

    static class InsufficientFundsException extends Exception {
        private final double shortfall;

        InsufficientFundsException(String message, double shortfall) {
            throw new UnsupportedOperationException("TODO 1 : implementer le constructeur");
        }

        double getShortfall() {
            throw new UnsupportedOperationException("TODO 1 : implementer getShortfall()");
        }
    }

    static class InvalidAccountException extends RuntimeException {
        InvalidAccountException(String message) {
            super(message);
        }
    }

    public static double withdraw(double balance, double amount) throws InsufficientFundsException {
        throw new UnsupportedOperationException("TODO 2 : implementer withdraw()");
    }

    public static void validateAccountId(String id) {
        throw new UnsupportedOperationException("TODO 3 : implementer validateAccountId()");
    }

    public static void main(String[] args) throws InsufficientFundsException {
        ExerciseChecker.check("withdraw(100, 30) == 70.0", withdraw(100, 30) == 70.0);

        boolean caughtChecked = false;
        double shortfall = -1;
        try {
            withdraw(100, 150);
        } catch (InsufficientFundsException e) {
            caughtChecked = true;
            shortfall = e.getShortfall();
        }
        ExerciseChecker.check("withdraw(100, 150) lance InsufficientFundsException avec shortfall == 50.0",
                caughtChecked && shortfall == 50.0);

        boolean noExceptionForValidId = true;
        try {
            validateAccountId("A123");
        } catch (RuntimeException e) {
            noExceptionForValidId = false;
        }
        ExerciseChecker.check("validateAccountId('A123') ne lance rien", noExceptionForValidId);

        boolean caughtUnchecked = false;
        try {
            validateAccountId("");
        } catch (InvalidAccountException e) {
            caughtUnchecked = true;
        }
        ExerciseChecker.check("validateAccountId('') lance InvalidAccountException", caughtUnchecked);

        ExerciseChecker.summary();
    }
}
