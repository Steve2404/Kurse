package ch4_coreapis.solutions;

/**
 * Corrige de l'exercice 30. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise30_ArrayAlgorithms.
 */
public class Solution30_ArrayAlgorithms {

    public static int maxSubarraySum(int[] arr) {
        // Kadane : prolonger ou repartir, et retenir le meilleur ; depart a arr[0] pour les tout-negatifs.
        int current = arr[0];
        int best = arr[0];
        for (int i = 1; i < arr.length; i++) {
            current = Math.max(arr[i], current + arr[i]);
            best = Math.max(best, current);
        }
        return best;
    }

    public static void rotateRight(int[] arr, int k) {
        // Trois inversions, sur place ; k % n evite les tours complets inutiles.
        int n = arr.length;
        if (n == 0) {
            return;
        }
        k = k % n;
        reverse(arr, 0, n - 1);
        reverse(arr, 0, k - 1);
        reverse(arr, k, n - 1);
    }

    private static void reverse(int[] arr, int from, int to) {
        // Deux pointeurs qui echangent en se rapprochant.
        while (from < to) {
            int tmp = arr[from];
            arr[from] = arr[to];
            arr[to] = tmp;
            from++;
            to--;
        }
    }

    public static int removeDuplicatesSorted(int[] arr) {
        // write n'avance que pour une valeur nouvelle ; read parcourt tout.
        if (arr.length == 0) {
            return 0;
        }
        int write = 1;
        for (int read = 1; read < arr.length; read++) {
            if (arr[read] != arr[write - 1]) {
                arr[write++] = arr[read];
            }
        }
        return write;
    }

    public static int[] prefixSums(int[] arr) {
        // Une case de plus : prefix[0] = 0 simplifie toutes les soustractions.
        int[] prefix = new int[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            prefix[i + 1] = prefix[i] + arr[i];
        }
        return prefix;
    }

    public static int rangeSum(int[] prefix, int from, int to) {
        // Somme de [from, to) en une soustraction, quelle que soit la longueur.
        return prefix[to] - prefix[from];
    }

    public static double[] movingAverage(int[] arr, int window) {
        // Fenetre glissante : + celui qui entre, - celui qui sort ; division en double.
        double[] out = new double[arr.length - window + 1];
        int sum = 0;
        for (int i = 0; i < window; i++) {
            sum += arr[i];
        }
        out[0] = sum / (double) window;
        for (int i = window; i < arr.length; i++) {
            sum += arr[i] - arr[i - window];
            out[i - window + 1] = sum / (double) window;
        }
        return out;
    }

    public static int longestIncreasingRun(int[] arr) {
        // Compteur courant remis a 1 quand la suite casse ; le meilleur est garde a part.
        if (arr.length == 0) {
            return 0;
        }
        int current = 1;
        int best = 1;
        for (int i = 1; i < arr.length; i++) {
            current = arr[i] > arr[i - 1] ? current + 1 : 1;
            best = Math.max(best, current);
        }
        return best;
    }
}
