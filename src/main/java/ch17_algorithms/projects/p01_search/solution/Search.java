package ch17_algorithms.projects.p01_search.solution;

import java.util.function.IntPredicate;

/**
 * La recherche : lineaire O(n), dichotomique O(log n), et ses variantes.
 * L'invariant de toutes les dichotomies ici : la reponse est TOUJOURS dans [lo, hi].
 */
public final class Search {

    private Search() {
    }

    // O(n) : on regarde tout, une case apres l'autre.
    public static int linear(int[] a, int key) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == key) {
                return i;
            }
        }
        return -1;
    }

    // O(log n). Meme contrat qu'Arrays.binarySearch : l'indice, ou -(point d'insertion) - 1 si absent.
    // Piege : lo + (hi - lo) / 2, car (lo + hi) / 2 deborde quand lo + hi depasse Integer.MAX_VALUE.
    public static int binary(int[] sorted, int key) {
        int lo = 0;
        int hi = sorted.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (sorted[mid] < key) {
                lo = mid + 1;
            } else if (sorted[mid] > key) {
                hi = mid - 1;
            } else {
                return mid;
            }
        }
        return -(lo + 1);
    }

    // Le premier indice dont la valeur est >= key (sorted.length s'il n'y en a pas).
    // Pourquoi hi = length (et pas length - 1) : "aucun" est une reponse possible, l'indice length.
    public static int lowerBound(int[] sorted, int key) {
        int lo = 0;
        int hi = sorted.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (sorted[mid] < key) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    // Le premier indice dont la valeur est > key. Une seule difference avec lowerBound : <= au lieu de <.
    public static int upperBound(int[] sorted, int key) {
        int lo = 0;
        int hi = sorted.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (sorted[mid] <= key) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    // O(log n) meme s'il y a un million de doublons.
    public static int count(int[] sorted, int key) {
        return upperBound(sorted, key) - lowerBound(sorted, key);
    }

    // La plus petite version v de [1, n] telle que isBad(v), ou -1. Comme "git bisect".
    public static int firstBad(int n, IntPredicate isBad) {
        int lo = 1;
        int hi = n;
        int found = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (isBad.test(mid)) {
                found = mid;
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return found;
    }

    // La racine carree entiere : le plus grand r tel que r * r <= n.
    // Piege : mid * mid deborde pour un grand n ; mid <= n / mid ne deborde jamais.
    public static long isqrt(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("nombre negatif : " + n);
        }
        long lo = 0;
        long hi = n;
        long best = 0;
        while (lo <= hi) {
            long mid = lo + (hi - lo) / 2;
            if (mid == 0 || mid <= n / mid) {
                best = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return best;
    }

    // Dichotomie SUR LA REPONSE : la capacite minimale d'un camion pour livrer les colis, dans l'ordre, en `days` jours.
    // Pourquoi c'est possible : si une capacite suffit, toute capacite plus grande suffit aussi (la reponse est monotone).
    public static int minCapacity(int[] weights, int days) {
        if (weights.length == 0 || days < 1) {
            throw new IllegalArgumentException("livraison impossible");
        }
        int lo = 0;
        int hi = 0;
        for (int w : weights) {
            lo = Math.max(lo, w);
            hi += w;
        }
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (daysNeeded(weights, mid) <= days) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }

    // Glouton : on remplit le camion tant que ca rentre, puis on passe au jour suivant.
    private static int daysNeeded(int[] weights, int capacity) {
        int needed = 1;
        int load = 0;
        for (int w : weights) {
            if (load + w > capacity) {
                needed++;
                load = 0;
            }
            load += w;
        }
        return needed;
    }
}
