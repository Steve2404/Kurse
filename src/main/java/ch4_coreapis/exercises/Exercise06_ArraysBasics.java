package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

/**
 * EXERCICE 6 - Tableaux : taille FIXE, index de 0 a longueur-1 (niveau : moyen)
 * =====================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_StringImmutabilityAndConcatenation.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un tableau, c'est une RANGEE DE CASIERS NUMEROTES, de taille FIXE
 * decidee UNE FOIS POUR TOUTES a sa creation (int[] a = new int[6] :
 * exactement 6 casiers, ni plus ni moins, pour toujours). Les casiers
 * sont numerotes a partir de 0 : le TOUT DERNIER casier valide est
 * donc a l'index (taille - 1), JAMAIS a l'index "taille" lui-meme -
 * essayer d'ouvrir le casier numero "taille" (ou plus loin, ou
 * negatif) lance une ArrayIndexOutOfBoundsException A L'EXECUTION.
 *
 *
 * ==================================================================
 * TODO 1 : sumArray(nums)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Declarer int total = 0.
 *   2. Parcourir CHAQUE case de nums (for-each, ou for indexe de 0 a
 *      nums.length - 1) et l'ajouter a total.
 *   3. Renvoyer total.
 *
 *
 * ==================================================================
 * TODO 2 : lastElement(nums)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer nums[nums.length - 1] - le DERNIER casier valide,
 *      jamais nums[nums.length] (qui n'existe pas).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en quelques lignes.
 *
 * Exemple a verifier : sumArray({1, 2, 3, 4}) == 10. lastElement({1,
 * 2, 3, 4}) == 4. Acceder a nums[nums.length] (voir main()) lance
 * ArrayIndexOutOfBoundsException.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - nums.length est un CHAMP (pas une methode, contrairement a
 *     .length() sur un String !) - jamais nums.length().
 */
public class Exercise06_ArraysBasics {

    public static int sumArray(int[] nums) {
        throw new UnsupportedOperationException("TODO 1 : implementer sumArray()");
    }

    public static int lastElement(int[] nums) {
        throw new UnsupportedOperationException("TODO 2 : implementer lastElement()");
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4};

        ExerciseChecker.check("sumArray({1,2,3,4}) == 10", sumArray(nums) == 10);
        ExerciseChecker.check("lastElement({1,2,3,4}) == 4 (index length-1)", lastElement(nums) == 4);

        boolean caught = false;
        try {
            int outOfBounds = nums[nums.length];
        } catch (ArrayIndexOutOfBoundsException e) {
            caught = true;
        }
        ExerciseChecker.check("nums[nums.length] lance ArrayIndexOutOfBoundsException", caught);

        ExerciseChecker.summary();
    }
}
