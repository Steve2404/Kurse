package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

import java.util.Arrays;
import java.util.Random;

/**
 * EXERCICE 29 - Algorithmique : trier et chercher a la main, compares a Arrays.sort et Arrays.binarySearch (niveau : avance)
 * ========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise11_ArraysBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Arrays.sort et Arrays.binarySearch sont des boites magiques. Ici, tu
 * construis toi-meme ce qu'il y a dedans. main() compare tes versions
 * aux VRAIES sur des centaines de tableaux tires au hasard (graine fixe,
 * donc toujours les memes tableaux).
 *
 *
 * ==================================================================
 * TODO 1 : bubbleSort(arr)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On compare chaque voisin et on echange s'ils sont dans le mauvais
 * ordre : le plus grand "remonte" a la fin comme une bulle. On recommence
 * tant qu'un passage a fait au moins un echange.
 *
 * -- Essayons a la main --
 *
 *   [5, 1, 4, 2] -> passage 1 : [1, 4, 2, 5] -> passage 2 : [1, 2, 4, 5] -> passage 3 : aucun echange, fini
 *
 * -- Le plan --
 *
 *   1. do { swapped = false ; pour i de 1 a n - 1 : si arr[i - 1] > arr[i], echanger, swapped = true ; }
 *      while (swapped).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : swap(arr, i, j) (avec une variable temporaire).
 *
 *
 * ==================================================================
 * TODO 2 : insertionSort(arr)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Comme des cartes a jouer : la partie gauche est deja rangee. On prend
 * la carte suivante, on decale vers la droite toutes les cartes plus
 * grandes, et on la glisse dans le trou.
 *
 * -- Le plan --
 *
 *   1. Pour i de 1 a n - 1 : key = arr[i] ; j = i - 1.
 *   2. Tant que j >= 0 et arr[j] > key : arr[j + 1] = arr[j] ; j--.
 *   3. arr[j + 1] = key.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : myBinarySearch(sorted, key)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On ouvre le dictionnaire au milieu : trop loin ? on garde la moitie
 * gauche ; pas assez ? la moitie droite. A la fin, si on n'a rien
 * trouve, low est exactement le point d'insertion : on rend
 * -(low) - 1, EXACTEMENT comme Arrays.binarySearch.
 *
 * -- Essayons a la main --
 *
 *   {3, 8, 15, 23, 42, 57}, 23 -> 3      10 -> low finit a 2 -> -3      99 -> -7
 *
 * -- Le plan --
 *
 *   1. low = 0 ; high = longueur - 1.
 *   2. Tant que low <= high : mid = (low + high) >>> 1 ; comparer ; ajuster low = mid + 1 ou high = mid - 1.
 *   3. Rendre -(low) - 1.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. (>>> 1 divise par 2 sans risque de debordement quand low + high depasse int.)
 *
 *
 * ==================================================================
 * TODO 4 : mergeSorted(a, b)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Deux piles de cartes deja triees : on regarde les deux cartes du
 * dessus, on prend la plus petite, et on recommence. Quand une pile est
 * vide, on recopie l'autre.
 *
 * -- Essayons a la main --
 *
 *   {1, 4, 9}, {2, 3, 10, 11} -> {1, 2, 3, 4, 9, 10, 11}
 *
 * -- Le plan --
 *
 *   1. out = new int[a.length + b.length] ; i, j, k a 0.
 *   2. Tant que i < a.length et j < b.length : out[k++] = le plus petit (a[i++] ou b[j++]).
 *   3. Recopier le reste de a, puis le reste de b.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : pairWithSum(sorted, target)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Deux pointeurs sur un tableau trie : somme trop grande ? le doigt de
 * droite recule ; trop petite ? celui de gauche avance. Une seule
 * traversee au lieu de tester toutes les paires.
 *
 * -- Essayons a la main --
 *
 *   {1, 3, 4, 6, 8, 11}, 10 : 1+11=12 > 10 ; 1+8=9 < 10 ; 3+8=11 > 10 ; 3+6=9 < 10 ; 4+6=10 -> "2,3"
 *   target 100 -> "aucune"
 *
 * -- Le plan --
 *
 *   1. i = 0 ; j = longueur - 1 ; tant que i < j : comparer arr[i] + arr[j] a target.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : kthSmallest(arr, k)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   {42, 7, 19, 88, 7, 63}, k = 1 -> 7 ; k = 3 -> 19 (le tableau d'origine ne bouge pas)
 *
 * -- Le plan --
 *
 *   1. copy = arr.clone() ; Arrays.sort(copy) ; rendre copy[k - 1].
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
 *   - swap : int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
 *   - k++ dans out[k++] : on ecrit a k, PUIS k augmente.
 */
public class Exercise29_SortAndSearchAlgorithms {

    public static void bubbleSort(int[] arr) {
        throw new UnsupportedOperationException("TODO 1 : implementer bubbleSort()");
    }

    public static void insertionSort(int[] arr) {
        throw new UnsupportedOperationException("TODO 2 : implementer insertionSort()");
    }

    public static int myBinarySearch(int[] sorted, int key) {
        throw new UnsupportedOperationException("TODO 3 : implementer myBinarySearch()");
    }

    public static int[] mergeSorted(int[] a, int[] b) {
        throw new UnsupportedOperationException("TODO 4 : implementer mergeSorted()");
    }

    public static String pairWithSum(int[] sorted, int target) {
        throw new UnsupportedOperationException("TODO 5 : implementer pairWithSum()");
    }

    public static int kthSmallest(int[] arr, int k) {
        throw new UnsupportedOperationException("TODO 6 : implementer kthSmallest()");
    }

    public static void main(String[] args) {
        Random random = new Random(42);
        boolean bubbleOk = true;
        boolean insertionOk = true;
        for (int t = 0; t < 300; t++) {
            int[] data = randomArray(random);
            int[] expected = data.clone();
            Arrays.sort(expected);
            int[] b = data.clone();
            bubbleSort(b);
            int[] ins = data.clone();
            insertionSort(ins);
            bubbleOk &= Arrays.equals(b, expected);
            insertionOk &= Arrays.equals(ins, expected);
        }
        ExerciseChecker.check("bubbleSort == Arrays.sort sur 300 tableaux au hasard", bubbleOk);
        ExerciseChecker.check("insertionSort == Arrays.sort sur 300 tableaux au hasard", insertionOk);

        int[] sorted = {3, 8, 15, 23, 42, 57};
        boolean searchOk = myBinarySearch(new int[0], 5) == Arrays.binarySearch(new int[0], 5);
        for (int key = 0; key <= 60; key++) {
            searchOk &= myBinarySearch(sorted, key) == Arrays.binarySearch(sorted, key);
        }
        ExerciseChecker.check("myBinarySearch == Arrays.binarySearch pour les cles 0 a 60 (trouve ET absent)", searchOk);

        ExerciseChecker.check("mergeSorted", Arrays.equals(mergeSorted(new int[] {1, 4, 9}, new int[] {2, 3, 10, 11}),
                new int[] {1, 2, 3, 4, 9, 10, 11}) && mergeSorted(new int[0], new int[0]).length == 0);
        ExerciseChecker.check("pairWithSum : 2,3 et aucune",
                pairWithSum(new int[] {1, 3, 4, 6, 8, 11}, 10).equals("2,3") && pairWithSum(new int[] {1, 3, 4, 6, 8, 11}, 100).equals("aucune"));
        int[] scores = {42, 7, 19, 88, 7, 63};
        ExerciseChecker.check("kthSmallest : 7 et 19, tableau intact",
                kthSmallest(scores, 1) == 7 && kthSmallest(scores, 3) == 19 && scores[0] == 42);

        ExerciseChecker.summary();
    }

    // Deja ecrit : un tableau de 0 a 30 nombres entre -50 et 50.
    private static int[] randomArray(Random random) {
        int[] data = new int[random.nextInt(31)];
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextInt(101) - 50;
        }
        return data;
    }
}
