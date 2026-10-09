package ch17_algorithms.projects.p02_sorting.solution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/** Quatre tris ecrits a la main, et un tri fusion generique et STABLE pour trier des objets. */
public final class Sorting {

    private Sorting() {
    }

    // O(n^2) au pire, mais O(n) sur un tableau PRESQUE trie : chaque element ne recule que de quelques cases.
    public static void insertionSort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= 0 && a[j] > key) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }

    // O(n log n) toujours : on coupe en deux, on trie chaque moitie, on fusionne deux listes triees.
    public static void mergeSort(int[] a) {
        if (a.length > 1) {
            mergeSort(a, new int[a.length], 0, a.length);
        }
    }

    // Trie a[lo, hi[ ; tmp evite de creer un tableau a chaque fusion.
    private static void mergeSort(int[] a, int[] tmp, int lo, int hi) {
        if (hi - lo < 2) {
            return;
        }
        int mid = lo + (hi - lo) / 2;
        mergeSort(a, tmp, lo, mid);
        mergeSort(a, tmp, mid, hi);
        int i = lo;
        int j = mid;
        int k = lo;
        while (i < mid && j < hi) {
            tmp[k++] = a[i] <= a[j] ? a[i++] : a[j++];
        }
        while (i < mid) {
            tmp[k++] = a[i++];
        }
        while (j < hi) {
            tmp[k++] = a[j++];
        }
        System.arraycopy(tmp, lo, a, lo, hi - lo);
    }

    // O(n log n) en moyenne, sur place.
    // Piege 1 : un pivot fixe (le 1er element) donne O(n^2) sur un tableau DEJA trie ; d'ou un pivot au hasard.
    // Piege 2 : avec beaucoup de doublons, une partition en deux degenere aussi ; d'ou la partition en TROIS (<, =, >).
    public static void quickSort(int[] a) {
        quickSort(a, 0, a.length - 1);
    }

    private static void quickSort(int[] a, int lo, int hi) {
        if (lo >= hi) {
            return;
        }
        int pivot = a[ThreadLocalRandom.current().nextInt(lo, hi + 1)];
        int lt = lo;
        int i = lo;
        int gt = hi;
        while (i <= gt) {
            if (a[i] < pivot) {
                swap(a, lt++, i++);
            } else if (a[i] > pivot) {
                swap(a, i, gt--);
            } else {
                i++;
            }
        }
        quickSort(a, lo, lt - 1);
        quickSort(a, gt + 1, hi);
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    // O(n + max), sans aucune comparaison : on compte combien de fois apparait chaque valeur.
    public static void countingSort(int[] a, int max) {
        int[] counts = new int[max + 1];
        for (int v : a) {
            if (v < 0 || v > max) {
                throw new IllegalArgumentException("valeur hors limites : " + v);
            }
            counts[v]++;
        }
        int k = 0;
        for (int v = 0; v <= max; v++) {
            for (int c = 0; c < counts[v]; c++) {
                a[k++] = v;
            }
        }
    }

    // Un tri STABLE : deux elements egaux gardent leur ordre de depart.
    // Piege : <= 0 (prendre a GAUCHE en cas d'egalite) ; avec < 0, le tri n'est plus stable.
    public static <T> List<T> mergeSort(List<T> list, Comparator<? super T> cmp) {
        if (list.size() < 2) {
            return new ArrayList<>(list);
        }
        int mid = list.size() / 2;
        List<T> left = mergeSort(list.subList(0, mid), cmp);
        List<T> right = mergeSort(list.subList(mid, list.size()), cmp);
        List<T> result = new ArrayList<>(list.size());
        int i = 0;
        int j = 0;
        while (i < left.size() && j < right.size()) {
            result.add(cmp.compare(left.get(i), right.get(j)) <= 0 ? left.get(i++) : right.get(j++));
        }
        result.addAll(left.subList(i, left.size()));
        result.addAll(right.subList(j, right.size()));
        return result;
    }
}
