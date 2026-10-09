package ch17_algorithms.drills.r01_search_sort.solution;

import java.util.concurrent.ThreadLocalRandom;

/** Le corrige du drill 1 : les gabarits de la dichotomie et des tris. */
public final class Recall01 {

    private Recall01() {
    }

    // D01
    public static int binary(int[] a, int key) {
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] < key) {
                lo = mid + 1;
            } else if (a[mid] > key) {
                hi = mid - 1;
            } else {
                return mid;
            }
        }
        return -(lo + 1);
    }

    // D02
    public static int lowerBound(int[] a, int key) {
        int lo = 0;
        int hi = a.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] < key) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    // D03
    public static int upperBound(int[] a, int key) {
        int lo = 0;
        int hi = a.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] <= key) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    // D04
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

    // D05
    public static void mergeSort(int[] a) {
        if (a.length > 1) {
            merge(a, new int[a.length], 0, a.length);
        }
    }

    private static void merge(int[] a, int[] tmp, int lo, int hi) {
        if (hi - lo < 2) {
            return;
        }
        int mid = lo + (hi - lo) / 2;
        merge(a, tmp, lo, mid);
        merge(a, tmp, mid, hi);
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

    // D06
    public static void quickSort(int[] a) {
        quick(a, 0, a.length - 1);
    }

    private static void quick(int[] a, int lo, int hi) {
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
        quick(a, lo, lt - 1);
        quick(a, gt + 1, hi);
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    // D07
    public static int minCapacity(int[] weights, int days) {
        int lo = 0;
        int hi = 0;
        for (int w : weights) {
            lo = Math.max(lo, w);
            hi += w;
        }
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            int needed = 1;
            int load = 0;
            for (int w : weights) {
                if (load + w > mid) {
                    needed++;
                    load = 0;
                }
                load += w;
            }
            if (needed <= days) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }
}
