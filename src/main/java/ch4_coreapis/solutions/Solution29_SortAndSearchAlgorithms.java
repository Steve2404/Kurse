package ch4_coreapis.solutions;

import java.util.Arrays;

/**
 * Corrige de l'exercice 29. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise29_SortAndSearchAlgorithms.
 */
public class Solution29_SortAndSearchAlgorithms {

    public static void bubbleSort(int[] arr) {
        // do/while : on repasse tant qu'un passage a echange quelque chose (arret anticipe si deja trie).
        boolean swapped;
        do {
            swapped = false;
            for (int i = 1; i < arr.length; i++) {
                if (arr[i - 1] > arr[i]) {
                    swap(arr, i - 1, i);
                    swapped = true;
                }
            }
        } while (swapped);
    }

    private static void swap(int[] arr, int i, int j) {
        // Variable temporaire : sans elle, la premiere valeur serait ecrasee.
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void insertionSort(int[] arr) {
        // On decale les plus grands vers la droite, puis on pose key dans le trou.
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    public static int myBinarySearch(int[] sorted, int key) {
        // A la sortie de boucle, low est le point d'insertion : meme formule que Arrays.binarySearch.
        int low = 0;
        int high = sorted.length - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (sorted[mid] < key) {
                low = mid + 1;
            } else if (sorted[mid] > key) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low) - 1;
    }

    public static int[] mergeSorted(int[] a, int[] b) {
        // Trois index : un par entree, un pour la sortie ; puis on vide ce qui reste.
        int[] out = new int[a.length + b.length];
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < a.length && j < b.length) {
            out[k++] = a[i] <= b[j] ? a[i++] : b[j++];
        }
        while (i < a.length) {
            out[k++] = a[i++];
        }
        while (j < b.length) {
            out[k++] = b[j++];
        }
        return out;
    }

    public static String pairWithSum(int[] sorted, int target) {
        // Deux pointeurs : le tableau trie dit dans quel sens bouger.
        int i = 0;
        int j = sorted.length - 1;
        while (i < j) {
            int sum = sorted[i] + sorted[j];
            if (sum == target) {
                return i + "," + j;
            }
            if (sum > target) {
                j--;
            } else {
                i++;
            }
        }
        return "aucune";
    }

    public static int kthSmallest(int[] arr, int k) {
        // Trier une copie : l'appelant garde son ordre.
        int[] copy = arr.clone();
        Arrays.sort(copy);
        return copy[k - 1];
    }
}
