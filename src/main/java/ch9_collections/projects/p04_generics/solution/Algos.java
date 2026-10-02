package ch9_collections.projects.p04_generics.solution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * SOLUTION - des algorithmes GENERIQUES et les trois jokers.
 * PECS : Producer Extends, Consumer Super.
 */
public final class Algos {

    private Algos() {
    }

    // Borne recursive : T doit se comparer a lui-meme (ou a un parent). La collection PRODUIT des T : ? extends T.
    public static <T extends Comparable<? super T>> T max(Collection<? extends T> values) {
        T best = null;
        for (T v : values) {
            if (best == null || v.compareTo(best) > 0) {
                best = v;
            }
        }
        return best;
    }

    // Tri fusion generique, stable, parametre par un comparateur de T (ou d'un parent de T).
    public static <T> List<T> mergeSort(List<T> list, Comparator<? super T> cmp) {
        if (list.size() <= 1) {
            return new ArrayList<>(list);
        }
        int mid = list.size() / 2;
        List<T> left = mergeSort(list.subList(0, mid), cmp);
        List<T> right = mergeSort(list.subList(mid, list.size()), cmp);
        List<T> out = new ArrayList<>(list.size());
        int i = 0;
        int j = 0;
        while (i < left.size() && j < right.size()) {
            out.add(cmp.compare(left.get(i), right.get(j)) <= 0 ? left.get(i++) : right.get(j++));
        }
        out.addAll(left.subList(i, left.size()));
        out.addAll(right.subList(j, right.size()));
        return out;
    }

    public static <T> int binarySearch(List<? extends T> sorted, T key, Comparator<? super T> cmp) {
        int lo = 0;
        int hi = sorted.size() - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int c = cmp.compare(sorted.get(mid), key);
            if (c < 0) {
                lo = mid + 1;
            } else if (c > 0) {
                hi = mid - 1;
            } else {
                return mid;
            }
        }
        return -(lo + 1);
    }

    // La cle de la plus grande valeur : deux parametres de type, dont un borne.
    public static <K, V extends Comparable<? super V>> K argMax(Map<K, V> map) {
        K bestKey = null;
        V best = null;
        for (Map.Entry<K, V> e : map.entrySet()) {
            if (best == null || e.getValue().compareTo(best) > 0) {
                best = e.getValue();
                bestKey = e.getKey();
            }
        }
        return bestKey;
    }

    // ? extends Number : on peut LIRE des Number (Integer, Double...), pas en ajouter.
    public static double sum(Collection<? extends Number> values) {
        double s = 0;
        for (Number n : values) {
            s += n.doubleValue();
        }
        return s;
    }

    // ? super Integer : on peut AJOUTER des Integer (dans une List<Integer>, List<Number> ou List<Object>).
    public static void fillSquares(List<? super Integer> target, int n) {
        for (int i = 1; i <= n; i++) {
            target.add(i * i);
        }
    }

    // La signature de Collections.copy : la source produit (extends), la destination consomme (super).
    public static <T> void copy(List<? super T> dst, List<? extends T> src) {
        for (T t : src) {
            dst.add(t);
        }
    }

    // ? seul : on ne connait pas le type ; on ne peut que lire des Object (ou appeler size, isEmpty...).
    public static String describe(Collection<?> c) {
        StringBuilder sb = new StringBuilder(c.size() + " elements :");
        for (Object o : c) {
            sb.append(' ').append(o.getClass().getSimpleName());
        }
        return sb.toString();
    }

    public static <T> List<T> repeat(T value, int times) {
        List<T> out = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            out.add(value);
        }
        return out;
    }
}
