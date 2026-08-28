package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

import java.util.Arrays;

/**
 * EXERCICE 7 - Arrays.sort() et Arrays.binarySearch() : la formule du point d'insertion (niveau : difficile)
 * =====================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise06_ArraysBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Arrays.sort(tableau) trie le tableau EN PLACE (le MEME tableau,
 * modifie directement - comme StringBuilder, PAS comme String).
 * Arrays.binarySearch(tableau, valeur) cherche EFFICACEMENT dans un
 * tableau DEJA TRIE (sinon le resultat n'a AUCUN sens garanti) :
 *   - trouve : rend l'index exact de la valeur.
 *   - PAS trouve : au lieu de rendre juste "-1" (qui ne dit RIEN sur
 *     OU l'inserer), Java rend une formule un peu tordue mais UTILE :
 *     -(pointDInsertion) - 1 - c'est-a-dire "a QUEL index il
 *     faudrait l'inserer pour garder le tableau trie, code en
 *     negatif pour bien distinguer 'trouve' de 'pas trouve' (un index
 *     TROUVE est TOUJOURS >= 0, un index NON trouve est TOUJOURS <
 *     0 - jamais d'ambiguite possible, meme pour un point d'insertion
 *     0)".
 *
 *
 * ==================================================================
 * TODO 1 : sortAscending(nums)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Appeler Arrays.sort(nums) (AUCUN retour a recuperer : nums,
 *      LUI, est trie EN PLACE).
 *
 *
 * ==================================================================
 * TODO 2 : insertionPointFor(sortedArr, target)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Sur {1, 3, 5, 7, 9} (DEJA trie) : chercher 5 (present, a l'index 2)
 * rend directement 2. Chercher 4 (absent) rend -3 - pour retrouver le
 * VRAI point d'insertion (2, entre 3 et 5), il faut calculer
 * -(-3) - 1 = 3 - 1 = 2.
 *
 * -- Le plan --
 *
 *   1. Appeler Arrays.binarySearch(sortedArr, target), garder le
 *      resultat.
 *   2. Si ce resultat est >= 0 (trouve) : le renvoyer tel quel,
 *      c'est DEJA l'index exact.
 *   3. Sinon (negatif, pas trouve) : renvoyer -(resultat) - 1 (le
 *      VRAI point d'insertion, "denegative").
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en quelques lignes.
 *
 * Exemple a verifier : sortAscending({5, 3, 9, 1, 7}) trie en
 * {1, 3, 5, 7, 9}. insertionPointFor({1, 3, 5, 7, 9}, 5) == 2
 * (trouve). insertionPointFor({1, 3, 5, 7, 9}, 4) == 2 (pas trouve,
 * s'inserait ENTRE 3 et 5, tous les 2 a des index differents mais le
 * point d'insertion, lui, reste 2). insertionPointFor({1, 3, 5, 7, 9}, 10)
 * == 5 (s'ajouterait tout a la fin, apres le dernier index 4).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "-(resultat) - 1" et PAS "-resultat - 1" ecrits differemment :
 *     ce sont MATHEMATIQUEMENT la meme chose, mais des parentheses
 *     explicites evitent toute confusion de lecture.
 */
public class Exercise07_ArraysSortAndBinarySearch {

    public static void sortAscending(int[] nums) {
        throw new UnsupportedOperationException("TODO 1 : implementer sortAscending()");
    }

    public static int insertionPointFor(int[] sortedArr, int target) {
        throw new UnsupportedOperationException("TODO 2 : implementer insertionPointFor()");
    }

    public static void main(String[] args) {
        int[] nums = {5, 3, 9, 1, 7};
        sortAscending(nums);
        ExerciseChecker.check("sortAscending() trie EN PLACE", Arrays.equals(nums, new int[] {1, 3, 5, 7, 9}));

        ExerciseChecker.check("insertionPointFor() : valeur TROUVEE rend l'index exact",
                insertionPointFor(nums, 5) == 2);
        ExerciseChecker.check("insertionPointFor() : valeur ABSENTE rend le VRAI point d'insertion (au milieu)",
                insertionPointFor(nums, 4) == 2);
        ExerciseChecker.check("insertionPointFor() : valeur ABSENTE, apres la fin",
                insertionPointFor(nums, 10) == 5);

        ExerciseChecker.summary();
    }
}
