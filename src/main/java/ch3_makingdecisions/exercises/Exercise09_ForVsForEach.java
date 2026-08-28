package ch3_makingdecisions.exercises;

import ch3_makingdecisions.ExerciseChecker;

/**
 * EXERCICE 9 - for vs for-each : quand tu as besoin de l'index, et quand tu n'en as PAS besoin (niveau : moyen)
 * =======================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise08_WhileVsDoWhile.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le for CLASSIQUE ("for (init; condition; mise-a-jour)") te donne
 * un CONTROLE TOTAL : tu choisis TOI-MEME par ou commencer, quand
 * t'arreter, et de COMBIEN avancer a chaque tour (i++, i += 2,
 * i--...) - utile des que tu as besoin de L'INDEX lui-meme, ou d'un
 * parcours "non standard" (a l'envers, en sautant des cases...). Le
 * for-each ("for (Type element : collection)"), lui, ne te donne
 * JAMAIS l'index : il se contente de te donner, l'un apres l'autre,
 * CHAQUE element - le compilateur construit LUI-MEME la condition
 * d'arret, tu n'as RIEN a ecrire de plus. Des que tu n'as PAS besoin
 * de l'index, le for-each est presque toujours le choix le plus
 * clair.
 *
 *
 * ==================================================================
 * TODO 1 : sumEvenIndices(nums)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Avec nums = {10, 20, 30, 40, 50} : les index PAIRS sont 0, 2, 4 -
 * soit les valeurs 10, 30, 50 - somme = 90.
 *
 * -- Le plan --
 *
 *   1. Declarer int total = 0.
 *   2. for (int i = 0; i < nums.length; i += 2) : ajouter nums[i] a
 *      total - i AVANCE DE 2 EN 2 (besoin de L'INDEX lui-meme ici,
 *      donc for classique obligatoire).
 *   3. Renvoyer total.
 *
 *
 * ==================================================================
 * TODO 2 : sumAll(nums)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Declarer int total = 0.
 *   2. for (int value : nums) : ajouter value a total - AUCUN index
 *      necessaire ici, un for-each suffit amplement.
 *   3. Renvoyer total.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient dans une seule boucle.
 *
 * Exemple a verifier : sumEvenIndices({10, 20, 30, 40, 50}) == 90.
 * sumAll({10, 20, 30, 40, 50}) == 150.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "i += 2" (et pas "i++") est ce qui fait sauter i de 2 en 2 -
 *     impossible a exprimer avec un for-each, qui, LUI, parcourt
 *     TOUJOURS element par element, sans jamais sauter.
 */
public class Exercise09_ForVsForEach {

    public static int sumEvenIndices(int[] nums) {
        throw new UnsupportedOperationException("TODO 1 : implementer sumEvenIndices()");
    }

    public static int sumAll(int[] nums) {
        throw new UnsupportedOperationException("TODO 2 : implementer sumAll()");
    }

    public static void main(String[] args) {
        int[] nums = {10, 20, 30, 40, 50};

        ExerciseChecker.check("sumEvenIndices() : for classique, avance de 2 en 2", sumEvenIndices(nums) == 90);
        ExerciseChecker.check("sumAll() : for-each, aucun index necessaire", sumAll(nums) == 150);

        ExerciseChecker.summary();
    }
}
