package ch8_lambdas.projects.p06_sorting.solution;

import java.util.function.ToIntFunction;

/**
 * SOLUTION - des algorithmes de tri et de recherche parametres par un Order.
 * Le compteur est un CHAMP : une lambda peut le modifier (contrairement a une variable locale).
 */
public class Sorter {

    private int comparisons;

    public int comparisons() {
        int c = comparisons;
        comparisons = 0;
        return c;
    }

    // Enveloppe un ordre pour compter chaque comparaison ; la lambda capture this et order.
    private Order counting(Order order) {
        return (a, b) -> {
            comparisons++;
            return order.compare(a, b);
        };
    }

    // Tri fusion : stable (a egalite, l'element de gauche passe d'abord), O(n log n).
    public Person[] mergeSort(Person[] input, Order order) {
        Person[] a = input.clone();
        sort(a, new Person[a.length], 0, a.length - 1, counting(order));
        return a;
    }

    private void sort(Person[] a, Person[] tmp, int lo, int hi, Order o) {
        if (lo >= hi) {
            return;
        }
        int mid = (lo + hi) / 2;
        sort(a, tmp, lo, mid, o);
        sort(a, tmp, mid + 1, hi, o);
        int i = lo;
        int j = mid + 1;
        int k = lo;
        while (i <= mid && j <= hi) {
            tmp[k++] = o.compare(a[i], a[j]) <= 0 ? a[i++] : a[j++];
        }
        while (i <= mid) {
            tmp[k++] = a[i++];
        }
        while (j <= hi) {
            tmp[k++] = a[j++];
        }
        System.arraycopy(tmp, lo, a, lo, hi - lo + 1);
    }

    public Person[] insertionSort(Person[] input, Order order) {
        Person[] a = input.clone();
        Order o = counting(order);
        for (int i = 1; i < a.length; i++) {
            Person key = a[i];
            int j = i - 1;
            while (j >= 0 && o.compare(a[j], key) > 0) {
                a[j + 1] = a[j--];
            }
            a[j + 1] = key;
        }
        return a;
    }

    // Les k premiers selon l'ordre, par selection partielle (k passes), sans trier le reste.
    public Person[] top(Person[] input, int k, Order order) {
        Person[] a = input.clone();
        Order o = counting(order);
        for (int i = 0; i < k; i++) {
            int best = i;
            for (int j = i + 1; j < a.length; j++) {
                if (o.compare(a[j], a[best]) < 0) {
                    best = j;
                }
            }
            Person t = a[i];
            a[i] = a[best];
            a[best] = t;
        }
        return java.util.Arrays.copyOf(a, k);
    }

    // Dichotomie sur une cle entiere : premier indice dont la cle vaut target, sinon -(insertion) - 1.
    public static int search(Person[] sorted, int target, ToIntFunction<Person> key) {
        int lo = 0;
        int hi = sorted.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (key.applyAsInt(sorted[mid]) < target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo < sorted.length && key.applyAsInt(sorted[lo]) == target ? lo : -(lo + 1);
    }
}
