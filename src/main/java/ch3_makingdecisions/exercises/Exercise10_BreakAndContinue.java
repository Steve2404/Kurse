package ch3_makingdecisions.exercises;

import ch3_makingdecisions.ExerciseChecker;

/**
 * EXERCICE 10 - break et continue : arreter la boucle ENTIEREMENT, ou juste sauter CE tour (niveau : moyen)
 * ====================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise08_WhileVsDoWhile.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * break, c'est appuyer sur le bouton "STOP DEFINITIF" de la boucle :
 * elle s'arrete IMMEDIATEMENT, MEME s'il restait des tours a faire -
 * on continue juste APRES la boucle. continue, c'est different :
 * "saute UNIQUEMENT ce tour-ci, mais continue la boucle normalement
 * au tour SUIVANT" - un peu comme sauter UNE case dans un jeu de
 * l'oie, sans jamais quitter le plateau.
 *
 *
 * ==================================================================
 * TODO 1 : firstMultipleOf(nums, divisor)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Avec nums = {7, 9, 12, 15} et divisor = 3 : 7 n'est pas multiple de
 * 3, 9 L'EST (9 / 3 = 3, sans reste) - on s'arrete LA, meme si 12 et
 * 15 sont eux aussi multiples de 3 plus loin.
 *
 * -- Le plan --
 *
 *   1. Declarer int result = -1 (valeur "rien trouve", par defaut).
 *   2. for (int n : nums) : si n % divisor == 0 : result = n, PUIS
 *      break (on ARRETE tout, on ne regarde plus les nombres
 *      suivants).
 *   3. Renvoyer result.
 *
 *
 * ==================================================================
 * TODO 2 : sumSkippingNegatives(nums)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Avec nums = {5, -3, 8, -1, 2} : on IGNORE -3 et -1 (negatifs), on
 * additionne SEULEMENT 5 + 8 + 2 = 15.
 *
 * -- Le plan --
 *
 *   1. Declarer int total = 0.
 *   2. for (int n : nums) : si n < 0 : continue (on saute CE tour
 *      SEULEMENT, la boucle repart normalement au nombre suivant).
 *      Sinon : total += n.
 *   3. Renvoyer total.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient dans une seule boucle.
 *
 * Exemple a verifier : firstMultipleOf({7, 9, 12, 15}, 3) == 9
 * (s'arrete au 1er trouve, ignore 12 et 15). sumSkippingNegatives({5,
 * -3, 8, -1, 2}) == 15.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "if (n < 0) { continue; }" DOIT etre place AVANT le "total +=
 *     n" pour vraiment sauter cette ligne pour les nombres negatifs.
 */
public class Exercise10_BreakAndContinue {

    public static int firstMultipleOf(int[] nums, int divisor) {
        throw new UnsupportedOperationException("TODO 1 : implementer firstMultipleOf()");
    }

    public static int sumSkippingNegatives(int[] nums) {
        throw new UnsupportedOperationException("TODO 2 : implementer sumSkippingNegatives()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("firstMultipleOf() s'arrete au 1er trouve (break)",
                firstMultipleOf(new int[] {7, 9, 12, 15}, 3) == 9);

        ExerciseChecker.check("sumSkippingNegatives() saute juste les negatifs (continue)",
                sumSkippingNegatives(new int[] {5, -3, 8, -1, 2}) == 15);

        ExerciseChecker.summary();
    }
}
