package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

import java.util.Arrays;

/**
 * EXERCICE 8 - Arrays.compare() et Arrays.mismatch() : comparer 2 tableaux SANS ecrire de boucle a la main (niveau : moyen/difficile)
 * ================================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise06_ArraysBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Arrays.compare(a, b) est comme comparer 2 mots dans un
 * dictionnaire, lettre par lettre : la 1ere DIFFERENCE decide de
 * tout (rend un nombre negatif si a "vient avant" b, positif si
 * apres, 0 si IDENTIQUES). Si aucune difference n'est trouvee mais
 * qu'un tableau est PLUS COURT (un simple "prefixe" de l'autre), le
 * PLUS COURT est considere "avant" (comme "chat" vient avant
 * "chatte" dans un dictionnaire). Arrays.mismatch(a, b), lui, ne dit
 * PAS "qui gagne" : il dit juste OU (quel index) la PREMIERE
 * difference apparait - et rend -1 si les 2 tableaux sont
 * EXACTEMENT identiques (aucune difference du tout).
 *
 *
 * ==================================================================
 * TODO 1 : describeComparison(a, b)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Appeler Arrays.compare(a, b), garder le resultat.
 *   2. Si le resultat vaut 0 : renvoyer "egaux".
 *   3. Si le resultat est negatif : renvoyer "a avant b".
 *   4. Sinon (positif) : renvoyer "a apres b".
 *
 *
 * ==================================================================
 * TODO 2 : describeMismatch(a, b)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Appeler Arrays.mismatch(a, b), garder le resultat.
 *   2. Si le resultat vaut -1 : renvoyer "identiques".
 *   3. Sinon : renvoyer "diverge a l'index " + resultat.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en quelques lignes.
 *
 * Exemple a verifier : describeComparison({1,2,3}, {1,2,3}) ==
 * "egaux". describeComparison({1,2,3}, {1,2,4}) == "a avant b" (3 <
 * 4 a la 1ere difference). describeComparison({1,2}, {1,2,3}) == "a
 * avant b" (le plus court "gagne" par prefixe). describeMismatch({1,2,3}, {1,2,3})
 * == "identiques". describeMismatch({1,2,3}, {1,2,4}) ==
 * "diverge a l'index 2".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Arrays.compare() et Arrays.mismatch() existent en versions
 *     surchargees pour TOUS les types de tableaux primitifs (int[],
 *     long[], double[]...) et pour les tableaux d'objets Comparable.
 */
public class Exercise08_ArraysCompareAndMismatch {

    public static String describeComparison(int[] a, int[] b) {
        throw new UnsupportedOperationException("TODO 1 : implementer describeComparison()");
    }

    public static String describeMismatch(int[] a, int[] b) {
        throw new UnsupportedOperationException("TODO 2 : implementer describeMismatch()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("describeComparison() : tableaux EGAUX",
                describeComparison(new int[] {1, 2, 3}, new int[] {1, 2, 3}).equals("egaux"));
        ExerciseChecker.check("describeComparison() : difference sur une valeur",
                describeComparison(new int[] {1, 2, 3}, new int[] {1, 2, 4}).equals("a avant b"));
        ExerciseChecker.check("describeComparison() : difference de longueur (prefixe)",
                describeComparison(new int[] {1, 2}, new int[] {1, 2, 3}).equals("a avant b"));

        ExerciseChecker.check("describeMismatch() : tableaux IDENTIQUES",
                describeMismatch(new int[] {1, 2, 3}, new int[] {1, 2, 3}).equals("identiques"));
        ExerciseChecker.check("describeMismatch() : premiere divergence a l'index 2",
                describeMismatch(new int[] {1, 2, 3}, new int[] {1, 2, 4}).equals("diverge a l'index 2"));

        ExerciseChecker.summary();
    }
}
