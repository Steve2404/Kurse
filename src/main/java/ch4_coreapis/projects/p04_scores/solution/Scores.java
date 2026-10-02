package ch4_coreapis.projects.p04_scores.solution;

import ch4_coreapis.projects.p04_scores.Data;

import java.util.Arrays;

/**
 * SOLUTION du projet 4 - une conception possible.
 */
public class Scores {

    // Recherche dichotomique ecrite a la main, avec la meme convention que Arrays.binarySearch.
    static int binarySearch(int[] sorted, int key) {
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
        return -(low + 1);   // -(point d'insertion) - 1 : toujours negatif, meme pour un point d'insertion 0
    }

    // Fusion de deux tableaux tries : on avance dans celui dont la tete est la plus petite.
    static int[] merge(int[] a, int[] b) {
        int[] result = new int[a.length + b.length];
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < a.length && j < b.length) {
            result[k++] = a[i] <= b[j] ? a[i++] : b[j++];
        }
        while (i < a.length) {
            result[k++] = a[i++];
        }
        while (j < b.length) {
            result[k++] = b[j++];
        }
        return result;
    }

    // Rotation a droite de k cases, sans tableau intermediaire de la meme taille : trois inversions.
    static void reverse(int[] t, int from, int to) {
        for (int i = from, j = to; i < j; i++, j--) {
            int tmp = t[i];
            t[i] = t[j];
            t[j] = tmp;
        }
    }

    static void rotateRight(int[] t, int k) {
        k %= t.length;
        reverse(t, 0, t.length - 1);
        reverse(t, 0, k - 1);
        reverse(t, k, t.length - 1);
    }

    static double median(int[] sorted) {
        int n = sorted.length;
        return n % 2 == 1 ? sorted[n / 2] : (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0;
    }

    public static void main(String[] args) {
        int[] a = Data.GROUP_A;
        System.out.println("NOTES A : " + Arrays.toString(a) + " (" + a.length + " copies)");

        int[] sorted = Arrays.copyOf(a, a.length);
        Arrays.sort(sorted);
        System.out.println("TRIEES : " + Arrays.toString(sorted) + ", original intact : " + Arrays.toString(a));

        int sum = 0;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int note : a) {
            sum += note;
            min = Math.min(min, note);
            max = Math.max(max, note);
        }
        double mean = (double) sum / a.length;
        double variance = 0;
        for (int note : a) {
            variance += Math.pow(note - mean, 2);
        }
        double deviation = Math.sqrt(variance / a.length);
        // Math.round(double) rend un long ; *100 puis /100.0 pour garder 2 decimales.
        System.out.println("MIN " + min + ", MAX " + max + ", MOYENNE " + Math.round(mean * 100) / 100.0 + ", MEDIANE " + median(sorted)
                + ", ECART-TYPE " + Math.round(deviation * 100) / 100.0);
        System.out.println("ARRONDIS de la moyenne : round " + Math.round(mean) + ", ceil " + Math.ceil(mean) + ", floor " + Math.floor(mean)
                + ", round(-2.5) " + Math.round(-2.5) + ", round(2.5f) " + Math.round(2.5f) + ", abs(-7) " + Math.abs(-7));

        System.out.println("RECHERCHE 78 : main " + binarySearch(sorted, 78) + " / Arrays " + Arrays.binarySearch(sorted, 78)
                + " | 60 : " + binarySearch(sorted, 60) + " / " + Arrays.binarySearch(sorted, 60)
                + " | 10 : " + binarySearch(sorted, 10) + " / " + Arrays.binarySearch(sorted, 10));

        int[] all = merge(sorted, Data.GROUP_B);
        System.out.println("FUSION A+B : " + Arrays.toString(all) + ", mediane " + median(all));

        int[] rotated = Arrays.copyOf(Data.GROUP_B, Data.GROUP_B.length);
        rotateRight(rotated, 2);
        System.out.println("ROTATION de 2 : " + Arrays.toString(rotated));

        int[] copy = Arrays.copyOf(Data.GROUP_B, 5);
        int[] longer = Arrays.copyOf(Data.GROUP_B, 6);
        System.out.println("COMPARE : equals " + Arrays.equals(Data.GROUP_B, copy) + ", == " + (Data.GROUP_B == copy)
                + ", compare(B, plus long) " + Arrays.compare(Data.GROUP_B, longer)
                + ", compare(A trie, B) " + Arrays.compare(sorted, Data.GROUP_B)
                + ", mismatch(B, rotation) " + Arrays.mismatch(Data.GROUP_B, rotated) + ", mismatch(B, copie) " + Arrays.mismatch(Data.GROUP_B, copy));

        String[] names = Arrays.copyOf(Data.NAMES, Data.NAMES.length);
        Arrays.sort(names);
        System.out.println("NOMS TRIES : " + Arrays.toString(names) + " (majuscules avant minuscules)");

        // Classement : on cherche le rang de chaque note dans le tableau trie decroissant.
        String podium = "";
        for (int rank = 0; rank < 3; rank++) {
            int note = sorted[sorted.length - 1 - rank];
            String who = "";
            for (int i = 0; i < a.length; i++) {
                if (a[i] == note) {
                    who = who.isEmpty() ? Data.NAMES[i] : who + "/" + Data.NAMES[i];
                }
            }
            // Tableau irregulier : la ligne "rank" n'a que (3 - rank) bonus ; on prend le premier.
            podium += (rank + 1) + ". " + who + " " + note + " (+" + Data.BONUSES[rank][0] + ", " + Data.BONUSES[rank].length + " bonus possibles) ";
        }
        System.out.println("PODIUM : " + podium.strip());

        int[] empty = new int[3];
        String[] noNames = new String[2];
        double[][] grid = new double[2][];
        Arrays.fill(empty, 7);
        System.out.println("DEFAUTS : " + Arrays.toString(new int[3]) + " " + Arrays.toString(noNames) + " " + Arrays.toString(grid)
                + ", fill " + Arrays.toString(empty) + ", Math.pow(2, 10) " + Math.pow(2, 10) + ", min(-0.0, 0.0) " + Math.min(-0.0, 0.0));
    }
}
