package ch2_operators.exercises;

import ch2_operators.ExerciseChecker;

/**
 * EXERCICE 11 - Les parentheses : forcer TON ordre de calcul, contre l'ordre par defaut (niveau : moyen)
 * ================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise10_OperatorPrecedence.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Les parentheses ( ) sont le SEUL moyen de forcer un ordre de calcul
 * DIFFERENT de la precedence par defaut (Exercise10) - ce qui est
 * ENTOURE de parentheses est TOUJOURS calcule EN PREMIER, quelle que
 * soit sa priorite naturelle. C'est exactement comme au college :
 * "2 + 3 * 4" vaut 14 (multiplication d'abord), mais "(2 + 3) * 4"
 * vaut 20 (les parentheses "coupent la file" et passent devant).
 *
 *
 * ==================================================================
 * TODO 1 : forcedAdditionFirst()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * (2 + 3) * 4 : l'addition, ENTOUREE de parentheses, se fait
 * D'ABORD (2 + 3 = 5), PUIS la multiplication (5 * 4 = 20) -
 * EXACTEMENT les memes nombres que multiplyBeforeAdd() de
 * l'Exercise10 (qui, LUI, rendait 14), mais un resultat totalement
 * different.
 *
 * -- Le plan --
 *
 *   1. Renvoyer (2 + 3) * 4.
 *
 *
 * ==================================================================
 * TODO 2 : forcedOrLast(a, b, c)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * a > 0 && (b > 0 || c > 0) : sans les parentheses, && aurait ete
 * calcule AVANT || (priorite naturelle) - avec elles, on force le ||
 * (b > 0 OU c > 0) a se calculer D'ABORD, PUIS seulement APRES le &&
 * final avec a > 0.
 *
 * -- Le plan --
 *
 *   1. Renvoyer a > 0 && (b > 0 || c > 0).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : forcedAdditionFirst() == 20 (compare avec
 * multiplyBeforeAdd() de l'Exercise10, qui rendait 14). Avec a=1,
 * b=-1, c=5 : forcedOrLast(1, -1, 5) == true (a > 0 vrai, ET (b > 0
 * FAUX OU c > 0 VRAI) = vrai au final, grace au c).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Sans les parentheses, "a > 0 && b > 0 || c > 0" aurait ete lu
 *     comme "(a > 0 && b > 0) || c > 0" (le && naturel passe avant
 *     le ||) - un sens COMPLETEMENT different de celui voulu ici.
 */
public class Exercise11_ParenthesesOverridePrecedence {

    public static int forcedAdditionFirst() {
        throw new UnsupportedOperationException("TODO 1 : implementer forcedAdditionFirst()");
    }

    public static boolean forcedOrLast(int a, int b, int c) {
        throw new UnsupportedOperationException("TODO 2 : implementer forcedOrLast()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("(2 + 3) * 4 == 20 (parentheses forcent l'addition d'abord)",
                forcedAdditionFirst() == 20);

        ExerciseChecker.check("forcedOrLast(1, -1, 5) == true (le || entre parentheses se calcule d'abord)",
                forcedOrLast(1, -1, 5));

        ExerciseChecker.summary();
    }
}
