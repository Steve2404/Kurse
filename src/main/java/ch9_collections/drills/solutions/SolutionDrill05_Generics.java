package ch9_collections.drills.solutions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Corrige du drill 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch9_collections.drills.exercises.Drill05_Generics.
 */
public class SolutionDrill05_Generics {

    public static class Box<T> {
        private final T value;

        public Box(T value) {
            this.value = value;
        }

        public T get() {
            return value;
        }

        public <R> Box<R> map(Function<? super T, ? extends R> f) {
            // R est propre a la methode : une Box<String> peut devenir une Box<Integer>.
            return new Box<>(f.apply(value));
        }
    }

    public record Pair<A, B>(A first, B second) {
    }

    public static <T> T firstOrDefault(List<T> list, T fallback) {
        // Le meme T relie la liste, la valeur de secours et le retour.
        return list.isEmpty() ? fallback : list.get(0);
    }

    public static <T extends Comparable<? super T>> T maxOf(Collection<? extends T> items) {
        // La borne garantit compareTo ; ? super T accepte aussi un T qui herite son compareTo d'un parent.
        T best = null;
        for (T item : items) {
            if (best == null || item.compareTo(best) > 0) {
                best = item;
            }
        }
        return best;
    }

    public static double sum(Collection<? extends Number> nums) {
        // On lit des Number (doubleValue) : marche pour Integer, Double, Long...
        double total = 0;
        for (Number n : nums) {
            total += n.doubleValue();
        }
        return total;
    }

    public static void fillWith(List<? super Integer> sink, int n) {
        // ? super Integer : on peut y ECRIRE des Integer (List<Integer>, List<Number>, List<Object>).
        for (int i = 1; i <= n; i++) {
            sink.add(i);
        }
    }

    public static <T> int count(Collection<? extends T> items, Predicate<? super T> test) {
        // Predicate<? super T> : un test sur Object convient aussi a des String.
        int count = 0;
        for (T item : items) {
            if (test.test(item)) {
                count++;
            }
        }
        return count;
    }

    public static <K, V> Map<V, K> invert(Map<K, V> map) {
        // Les parametres de type s'echangent dans le type de retour.
        Map<V, K> result = new HashMap<>();
        map.forEach((k, v) -> result.put(v, k));
        return result;
    }

    public static <T> List<T> repeat(T value, int times) {
        // Le diamant deduit ArrayList<T> du type de retour.
        List<T> result = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            result.add(value);
        }
        return result;
    }

    public static String describeAll(List<?> items) {
        // Avec <?>, on ne peut lire que des Object : toString suffit ici.
        StringJoiner joiner = new StringJoiner(",");
        for (Object o : items) {
            joiner.add(String.valueOf(o));
        }
        return joiner.toString();
    }

    public static <A, B> Pair<B, A> swapped(Pair<A, B> pair) {
        // Le compilateur verifie que second (un B) va bien en premiere position d'une Pair<B, A>.
        return new Pair<>(pair.second(), pair.first());
    }
}
