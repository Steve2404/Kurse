package ch9_collections.solutions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Corrige de l'exercice 20.
 */
public class Solution20_TypeSafeContainer {

    public static class Fruit implements Comparable<Fruit> {
        private final int grams;

        public Fruit(int grams) {
            this.grams = grams;
        }

        public int grams() {
            return grams;
        }

        @Override
        public int compareTo(Fruit other) {
            return Integer.compare(grams, other.grams);
        }

        @Override
        public String toString() {
            return grams + " g";
        }
    }

    public static class Apple extends Fruit {
        public Apple(int grams) {
            super(grams);
        }
    }

    public static class Favorites {
        private final Map<Class<?>, Object> values = new HashMap<>();

        public <T> void put(Class<T> type, T value) {
            // La signature relie la cle et la valeur : put(Integer.class, "x") ne compile pas.
            values.put(type, value);
        }

        public <T> T get(Class<T> type) {
            // type.cast verifie a l'execution et evite le cast (T) "unchecked" que l'effacement rendrait aveugle.
            return type.cast(values.get(type));
        }
    }

    public static <T extends Comparable<? super T>> T max(Collection<? extends T> items) {
        // ? super T : un Apple se compare en tant que Fruit ; ? extends T : on ne fait que lire la collection.
        if (items.isEmpty()) {
            throw new NoSuchElementException("collection vide");
        }
        T best = null;
        for (T item : items) {
            if (best == null || item.compareTo(best) > 0) {
                best = item;
            }
        }
        return best;
    }

    public static <K, V extends Comparable<? super V>> K argMax(Map<K, V> map) {
        // Remplacement seulement si STRICTEMENT plus grand : la 1re cle rencontree gagne les egalites.
        K bestKey = null;
        V bestValue = null;
        for (Map.Entry<K, V> e : map.entrySet()) {
            if (bestValue == null || e.getValue().compareTo(bestValue) > 0) {
                bestKey = e.getKey();
                bestValue = e.getValue();
            }
        }
        return bestKey;
    }

    public static <T, R> List<R> mapAll(List<? extends T> items, Function<? super T, ? extends R> f) {
        // f.apply rend un "? extends R" : c'est bien un R, on peut l'ajouter a une List<R>.
        List<R> result = new ArrayList<>();
        for (T item : items) {
            result.add(f.apply(item));
        }
        return result;
    }

    public static <T> Map<Boolean, List<T>> partition(Collection<? extends T> items, Predicate<? super T> test) {
        // Les deux cles sont creees d'avance pour que get(true)/get(false) ne rendent jamais null.
        Map<Boolean, List<T>> result = new HashMap<>();
        result.put(false, new ArrayList<>());
        result.put(true, new ArrayList<>());
        for (T item : items) {
            result.get(test.test(item)).add(item);
        }
        return result;
    }

    public record Pair<A, B>(A first, B second) {
        public Pair<B, A> swap() {
            // Les parametres de type s'echangent avec les valeurs : le compilateur verifie l'ordre.
            return new Pair<>(second, first);
        }
    }
}
