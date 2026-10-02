package ch5_methods.projects.p01_stats.solution;

import java.util.Arrays;

/**
 * SOLUTION du projet 1 - la boite a outils statistique (methodes static et varargs).
 */
public class Stats {

    // Un varargs se comporte comme un tableau : sum() recoit un tableau VIDE, jamais null.
    public static int sum(int... values) {
        int total = 0;
        for (int v : values) {
            total += v;
        }
        return total;
    }

    // Seul un appel explicite avec null (cast en int[]) donne un tableau null : on le traite a part.
    public static int count(int... values) {
        if (values == null) {
            return -1;
        }
        return values.length;
    }

    // Un parametre fixe AVANT le varargs impose au moins une valeur : average() ne compile pas.
    public static double average(int first, int... rest) {
        return round2((first + sum(rest)) / (1.0 + rest.length));
    }

    public static int max(int first, int... rest) {
        int best = first;
        for (int v : rest) {
            best = Math.max(best, v);
        }
        return best;
    }

    // private : un detail d'implementation que le reste du programme n'a pas a connaitre.
    private static double round2(double x) {
        return Math.round(x * 100) / 100.0;
    }

    public static String join(String separator, String... parts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(parts[i]);
        }
        return sb.toString();
    }

    // Une copie triee : le tableau de l'appelant n'est jamais modifie.
    public static double median(int... values) {
        int[] s = Arrays.copyOf(values, values.length);
        Arrays.sort(s);
        int n = s.length;
        return n % 2 == 1 ? s[n / 2] : (s[n / 2 - 1] + s[n / 2]) / 2.0;
    }

    // Retourner un tableau : toutes les valeurs les plus frequentes, dans l'ordre croissant.
    public static int[] modes(int... values) {
        int[] s = Arrays.copyOf(values, values.length);
        Arrays.sort(s);
        int best = 0;
        for (int i = 0; i < s.length; ) {
            int j = i;
            while (j < s.length && s[j] == s[i]) {
                j++;
            }
            best = Math.max(best, j - i);
            i = j;
        }
        int[] result = new int[s.length];
        int k = 0;
        for (int i = 0; i < s.length; ) {
            int j = i;
            while (j < s.length && s[j] == s[i]) {
                j++;
            }
            if (j - i == best) {
                result[k++] = s[i];
            }
            i = j;
        }
        return Arrays.copyOf(result, k);
    }

    // Quickselect : le k-ieme plus petit (k commence a 1) sans trier tout le tableau. O(n) en moyenne.
    public static int kth(int k, int... values) {
        int[] a = values.clone();
        int low = 0;
        int high = a.length - 1;
        int target = k - 1;
        while (true) {
            int p = partition(a, low, high);
            if (p == target) {
                return a[p];
            } else if (p < target) {
                low = p + 1;
            } else {
                high = p - 1;
            }
        }
    }

    // Partition de Lomuto : le pivot (dernier element) finit a sa place definitive.
    private static int partition(int[] a, int low, int high) {
        int pivot = a[high];
        int i = low;
        for (int j = low; j < high; j++) {
            if (a[j] < pivot) {
                swap(a, i++, j);
            }
        }
        swap(a, i, high);
        return i;
    }

    // Le tableau est passe par valeur de REFERENCE : l'echange se voit chez l'appelant.
    private static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static double[] movingAverage(int window, int... values) {
        double[] out = new double[values.length - window + 1];
        int current = 0;
        for (int i = 0; i < values.length; i++) {
            current += values[i];
            if (i >= window) {
                current -= values[i - window];
            }
            if (i >= window - 1) {
                out[i - window + 1] = round2((double) current / window);
            }
        }
        return out;
    }

    // Un varargs de TABLEAUX : chaque argument est un int[].
    public static int[] concat(int[]... arrays) {
        int size = 0;
        for (int[] a : arrays) {
            size += a.length;
        }
        int[] out = new int[size];
        int pos = 0;
        for (int[] a : arrays) {
            System.arraycopy(a, 0, out, pos, a.length);
            pos += a.length;
        }
        return out;
    }

    // Object... accepte tout ; un element null est different d'un tableau null.
    public static String describe(Object... items) {
        if (items == null) {
            return "tableau null";
        }
        StringBuilder sb = new StringBuilder(items.length + " element(s) :");
        for (Object o : items) {
            sb.append(' ').append(o);
        }
        return sb.toString();
    }

    // Une methode void peut sortir tot avec un simple return;
    public static void histogram(String[] labels, int... counts) {
        if (labels.length != counts.length) {
            System.out.println("histogramme impossible : " + labels.length + " etiquettes pour " + counts.length + " valeurs");
            return;
        }
        int top = max(counts[0], counts);
        for (int level = top; level >= 1; level--) {
            StringBuilder line = new StringBuilder(String.format("%2d |", level));
            for (int c : counts) {
                line.append(c >= level ? " ## " : "    ");
            }
            System.out.println(line.toString().stripTrailing());
        }
        StringBuilder axis = new StringBuilder("   +");
        StringBuilder names = new StringBuilder("    ");
        for (String label : labels) {
            axis.append("----");
            names.append(String.format("%-4s", label));
        }
        System.out.println(axis);
        System.out.println(names.toString().stripTrailing());
    }
}
