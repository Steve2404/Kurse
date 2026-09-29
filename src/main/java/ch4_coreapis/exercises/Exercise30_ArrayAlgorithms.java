package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

import java.util.Arrays;

/**
 * EXERCICE 30 - Algorithmique sur les tableaux : Kadane, rotation, doublons, sommes prefixes, fenetre glissante (niveau : avance)
 * ============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise11_ArraysBasics.java.
 *
 * -- Les schemas d'algorithme de cet exercice --
 *
 *   GARDER LE MEILLEUR EN COURS DE ROUTE : un seul passage, deux variables (courant, meilleur).
 *   INVERSER DES TRANCHES             : tourner = 3 inversions, sans tableau supplementaire.
 *   LECTEUR / ECRIVAIN                : un index lit tout, un autre n'ecrit que ce qu'on garde.
 *   SOMMES PREFIXES                   : prefix[i] = somme des i premiers ; somme(d, f) = prefix[f] - prefix[d].
 *   FENETRE GLISSANTE                 : on ajoute l'element qui entre, on retire celui qui sort.
 *
 *
 * ==================================================================
 * TODO 1 : maxSubarraySum(arr)    [garder le meilleur en cours de route]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On cherche la suite de cases VOISINES dont la somme est la plus
 * grande. Astuce de Kadane : a chaque case, soit on prolonge la suite
 * courante, soit on repart de zero a cette case (si la suite courante
 * etait devenue un poids).
 *
 * -- Essayons a la main --
 *
 *   {-2, 1, -3, 4, -1, 2, 1, -5, 4} -> 4 + (-1) + 2 + 1 = 6
 *   {-3, -1, -2} -> -1 (il faut au moins une case)
 *
 * -- Le plan --
 *
 *   1. current = best = arr[0].
 *   2. Pour i de 1 a n - 1 : current = Math.max(arr[i], current + arr[i]) ; best = Math.max(best, current).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : rotateRight(arr, k)    [inverser des tranches]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On fait tourner le tableau de k cases vers la droite, SUR PLACE.
 * Astuce : inverser tout, puis inverser les k premiers, puis le reste.
 * k plus grand que la longueur : on ne garde que k % n.
 *
 * -- Essayons a la main --
 *
 *   {1, 2, 3, 4, 5}, k = 2 : tout inverse {5, 4, 3, 2, 1} ; 2 premiers {4, 5, 3, 2, 1} ; reste {4, 5, 1, 2, 3}
 *   k = 7 -> meme resultat que k = 2
 *
 * -- Le plan --
 *
 *   1. Si n == 0, rien. k = k % n.
 *   2. reverse(arr, 0, n - 1) ; reverse(arr, 0, k - 1) ; reverse(arr, k, n - 1).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : reverse(arr, from, to) avec deux pointeurs, appelee 3 fois.
 *
 *
 * ==================================================================
 * TODO 3 : removeDuplicatesSorted(arr)    [lecteur / ecrivain]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Tableau trie : les doublons sont voisins. On garde chaque valeur une
 * fois, en ecrivant au debut du MEME tableau, et on rend combien on en
 * a garde. Ce qui reste apres n'a plus d'importance.
 *
 * -- Essayons a la main --
 *
 *   {1, 1, 2, 3, 3, 3, 4} -> rend 4 ; le debut devient {1, 2, 3, 4, ...}
 *
 * -- Le plan --
 *
 *   1. Si vide, rendre 0. write = 1.
 *   2. Pour read de 1 a n - 1 : si arr[read] != arr[write - 1], arr[write++] = arr[read].
 *   3. Rendre write.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : prefixSums(arr)    et    TODO 5 : rangeSum(prefix, from, to)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Si on doit additionner des tranches mille fois, on prepare une seule
 * fois les totaux cumules. Ensuite, toute somme de tranche est une
 * simple soustraction.
 *
 * -- Essayons a la main --
 *
 *   {3, 1, 4, 1, 5} -> prefix {0, 3, 4, 8, 9, 14} (une case de plus)
 *   rangeSum(prefix, 1, 4) = prefix[4] - prefix[1] = 9 - 3 = 6 (= 1 + 4 + 1, fin EXCLUE)
 *
 * -- Le plan --
 *
 *   1. prefix = new int[n + 1] ; prefix[i + 1] = prefix[i] + arr[i].
 *   2. rangeSum : prefix[to] - prefix[from].
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : movingAverage(arr, window)    [fenetre glissante]
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   {1, 2, 3, 4, 5}, 3 -> {2.0, 3.0, 4.0}  (moyennes de 1..3, 2..4, 3..5)
 *
 * -- Le plan --
 *
 *   1. sum = somme des window premiers ; out[0] = sum / (double) window.
 *   2. Pour i de window a n - 1 : sum += arr[i] - arr[i - window] ; out[i - window + 1] = sum / (double) window.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 7 : longestIncreasingRun(arr)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   {1, 2, 2, 3, 4, 1, 5} -> 2, 3, 4 -> 3 (2, 2 n'est pas croissant STRICT)     {} -> 0
 *
 * -- Le plan --
 *
 *   1. Meme schema que le TODO 1 : un compteur courant, remis a 1 quand ca casse, et le meilleur.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - arr[write++] = x : ecrit a write, PUIS write augmente.
 *   - sum / (double) window : sinon division entiere.
 */
public class Exercise30_ArrayAlgorithms {

    public static int maxSubarraySum(int[] arr) {
        throw new UnsupportedOperationException("TODO 1 : implementer maxSubarraySum()");
    }

    public static void rotateRight(int[] arr, int k) {
        throw new UnsupportedOperationException("TODO 2 : implementer rotateRight()");
    }

    public static int removeDuplicatesSorted(int[] arr) {
        throw new UnsupportedOperationException("TODO 3 : implementer removeDuplicatesSorted()");
    }

    public static int[] prefixSums(int[] arr) {
        throw new UnsupportedOperationException("TODO 4 : implementer prefixSums()");
    }

    public static int rangeSum(int[] prefix, int from, int to) {
        throw new UnsupportedOperationException("TODO 5 : implementer rangeSum()");
    }

    public static double[] movingAverage(int[] arr, int window) {
        throw new UnsupportedOperationException("TODO 6 : implementer movingAverage()");
    }

    public static int longestIncreasingRun(int[] arr) {
        throw new UnsupportedOperationException("TODO 7 : implementer longestIncreasingRun()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("maxSubarraySum : 6 et -1",
                maxSubarraySum(new int[] {-2, 1, -3, 4, -1, 2, 1, -5, 4}) == 6 && maxSubarraySum(new int[] {-3, -1, -2}) == -1);

        int[] a = {1, 2, 3, 4, 5};
        rotateRight(a, 2);
        int[] b = {1, 2, 3, 4, 5};
        rotateRight(b, 7);
        int[] empty = {};
        rotateRight(empty, 3);
        ExerciseChecker.check("rotateRight : {4, 5, 1, 2, 3} pour k = 2 et k = 7, tableau vide accepte",
                Arrays.equals(a, new int[] {4, 5, 1, 2, 3}) && Arrays.equals(b, new int[] {4, 5, 1, 2, 3}));

        int[] dup = {1, 1, 2, 3, 3, 3, 4};
        int n = removeDuplicatesSorted(dup);
        ExerciseChecker.check("removeDuplicatesSorted : 4 et debut {1, 2, 3, 4}",
                n == 4 && Arrays.equals(Arrays.copyOf(dup, n), new int[] {1, 2, 3, 4}) && removeDuplicatesSorted(new int[0]) == 0);

        int[] prefix = prefixSums(new int[] {3, 1, 4, 1, 5});
        ExerciseChecker.check("prefixSums : {0, 3, 4, 8, 9, 14}", Arrays.equals(prefix, new int[] {0, 3, 4, 8, 9, 14}));
        ExerciseChecker.check("rangeSum : (1, 4) -> 6 et (0, 5) -> 14", rangeSum(prefix, 1, 4) == 6 && rangeSum(prefix, 0, 5) == 14);
        ExerciseChecker.check("movingAverage : {2.0, 3.0, 4.0}",
                Arrays.equals(movingAverage(new int[] {1, 2, 3, 4, 5}, 3), new double[] {2.0, 3.0, 4.0}));
        ExerciseChecker.check("longestIncreasingRun : 3 et 0",
                longestIncreasingRun(new int[] {1, 2, 2, 3, 4, 1, 5}) == 3 && longestIncreasingRun(new int[0]) == 0);

        ExerciseChecker.summary();
    }
}
