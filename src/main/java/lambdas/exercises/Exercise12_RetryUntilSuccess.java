package lambdas.exercises;

import lambdas.ExerciseChecker;

import java.util.function.Supplier;

/**
 * EXERCICE 12 - Reessayer une operation qui echoue parfois (niveau : difficile, style entretien)
 * ========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CustomFunctionalInterface.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine que tu essaies d'appeler un ami au telephone, mais parfois
 * la ligne est occupee. Tu ne raccroches pas pour toujours des le
 * premier echec : tu RAPPELLES, quelques fois de suite, en esperant
 * que ca passe. Si apres un certain nombre de tentatives ca ne passe
 * toujours pas, tu abandonnes et tu previens que ca n'a pas marche.
 *
 * Supplier<T> represente ici "l'operation risquee, pas encore
 * tentee" (appeler l'ami). Si elle echoue, elle le signale en lancant
 * une RuntimeException (pas en renvoyant une valeur bizarre) - c'est
 * la maniere normale, en Java, de dire "ca n'a pas marche cette
 * fois".
 *
 *
 * ==================================================================
 * TODO : retryUntilSuccess(action, maxAttempts)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Une action qui echoue les 2 premieres fois puis reussit a la 3e,
 * appelee avec maxAttempts=5 : tentative 1 (echoue), tentative 2
 * (echoue), tentative 3 (reussit) -> on RENVOIE le resultat de la
 * tentative 3, sans jamais tenter les tentatives 4 et 5 (inutiles,
 * puisqu'on a deja reussi).
 *
 * Une action qui echoue TOUJOURS, appelee avec maxAttempts=3 :
 * tentative 1, 2, 3 (toutes echouent) -> apres la 3e tentative
 * ratee, on ABANDONNE, et on relance (rethrow) l'exception de cette
 * derniere tentative (pas une exception "generique" inventee - la
 * VRAIE cause du dernier echec, pour que l'appelant sache exactement
 * ce qui a coince).
 *
 * -- Le plan --
 *
 *   1. Repeter, pour un numero de tentative allant de 1 a maxAttempts
 *      inclus :
 *      a. essayer d'appeler action.get() ;
 *      b. si ca reussit (pas d'exception), RENVOYER ce resultat tout
 *         de suite, sans tenter les tentatives suivantes ;
 *      c. si ca echoue (RuntimeException attrapee) ET qu'il s'agissait
 *         de la DERNIERE tentative autorisee, relancer cette
 *         exception (abandon definitif) ;
 *      d. si ca echoue mais qu'il RESTE des tentatives, ne rien faire
 *         de special - le tour de boucle suivant va retenter tout
 *         seul.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule boucle avec un essai/attrape (try/catch) dedans
 * suffit, pas besoin de la decouper davantage.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - for (int attempt = 1; attempt <= maxAttempts; attempt++) {
 *         try {
 *             return action.get();
 *         } catch (RuntimeException e) {
 *             if (attempt == maxAttempts) {
 *                 throw e;
 *             }
 *         }
 *     }
 *     throw new IllegalStateException("inatteignable"); // le compilateur
 *     // exige un retour/lancer sur TOUS les chemins, meme si cette
 *     // derniere ligne n'est en pratique jamais executee (la boucle
 *     // renvoie ou relance toujours avant sa toute derniere iteration).
 */
public class Exercise12_RetryUntilSuccess {

    public static <T> T retryUntilSuccess(Supplier<T> action, int maxAttempts) {
        throw new UnsupportedOperationException("TODO : implementer retryUntilSuccess()");
    }

    public static void main(String[] args) {
        int[] callCount = {0};
        Supplier<String> flaky = () -> {
            callCount[0]++;
            if (callCount[0] < 3) {
                throw new RuntimeException("panne reseau simulee");
            }
            return "OK";
        };

        String result = retryUntilSuccess(flaky, 5);
        ExerciseChecker.check("reussit a la 3e tentative, sans en tenter davantage",
                result.equals("OK") && callCount[0] == 3);

        int[] callCount2 = {0};
        Supplier<String> alwaysFails = () -> {
            callCount2[0]++;
            throw new RuntimeException("boom");
        };

        boolean threw = false;
        try {
            retryUntilSuccess(alwaysFails, 3);
        } catch (RuntimeException e) {
            threw = true;
        }
        ExerciseChecker.check("abandonne apres maxAttempts tentatives et relance la derniere exception",
                threw && callCount2[0] == 3);

        ExerciseChecker.summary();
    }
}
