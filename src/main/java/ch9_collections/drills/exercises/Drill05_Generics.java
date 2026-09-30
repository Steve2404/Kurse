package ch9_collections.drills.exercises;

import ch9_collections.ExerciseChecker;
import ch9_collections.drills.Pantry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * DRILL 05 - Generiques : methodes generiques, bornes, wildcards
 * ==============================================================
 *
 * Mode d'emploi : voir Drill01_ListAndSet. Ici les SIGNATURES sont
 * donnees : lis-les a voix haute ("T qui s'etend en comparable d'un
 * ancetre de T...") AVANT d'ecrire le corps. Au 2e passage, cache la
 * signature et reecris-la de memoire en commentaire au-dessus.
 *
 *
 * -- Les TODO (notion visee entre crochets) --
 *
 * TODO 1  : firstOrDefault(list, fallback)  [<T> avant le type de retour] premier element, ou fallback si vide.
 * TODO 2  : maxOf(items)                    [<T extends Comparable<? super T>>] le plus grand -> max(NUMBERS) = 9.
 * TODO 3  : sum(nums)                       [? extends Number : lecture] la somme en double -> 28.0.
 * TODO 4  : fillWith(sink, n)               [? super Integer : ecriture] ajouter 1..n.
 * TODO 5  : count(items, test)              [Predicate<? super T>] combien passent le test.
 * TODO 6  : invert(map)                     [<K, V> deux parametres de type] valeur -> cle.
 * TODO 7  : repeat(value, times)            [List<T> construite] [value, value, ...].
 * TODO 8  : describeAll(items)              [List<?> : on ne lit que des Object] les toString separes par ",".
 * TODO 9  : Box.map(f)                      [methode generique dans une classe generique] Box<T> -> Box<R>.
 * TODO 10 : swapped(pair)                   [record generique] Pair<A, B> -> Pair<B, A>.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   static <T> T m(...)                   le <T> se place AVANT le type de retour (apres static)
 *   class Box<T> ; record Pair<A, B>(A first, B second)
 *   <T extends Number>                    borne haute ; <T extends Number & Comparable<T>> : classe d'abord
 *   ? extends X   Producer : on LIT des X ; add interdit (sauf null)
 *   ? super X     Consumer : on ECRIT des X ; get rend Object
 *   ?             lecture en Object seulement
 *   Pas de new T(), pas de new T[n], pas de instanceof List<String>, pas de static T : effacement de type
 *   List<Integer> n'est PAS une List<Number> ; List<Integer> EST une List<? extends Number>
 * ---------------------------------------------------------------------
 */
public class Drill05_Generics {

    public static class Box<T> {
        private final T value;

        public Box(T value) {
            this.value = value;
        }

        public T get() {
            return value;
        }

        public <R> Box<R> map(Function<? super T, ? extends R> f) {
            throw new UnsupportedOperationException("TODO 9 : implementer map()");
        }
    }

    public record Pair<A, B>(A first, B second) {
    }

    public static <T> T firstOrDefault(List<T> list, T fallback) {
        throw new UnsupportedOperationException("TODO 1 : implementer firstOrDefault()");
    }

    public static <T extends Comparable<? super T>> T maxOf(Collection<? extends T> items) {
        throw new UnsupportedOperationException("TODO 2 : implementer maxOf()");
    }

    public static double sum(Collection<? extends Number> nums) {
        throw new UnsupportedOperationException("TODO 3 : implementer sum()");
    }

    public static void fillWith(List<? super Integer> sink, int n) {
        throw new UnsupportedOperationException("TODO 4 : implementer fillWith()");
    }

    public static <T> int count(Collection<? extends T> items, Predicate<? super T> test) {
        throw new UnsupportedOperationException("TODO 5 : implementer count()");
    }

    public static <K, V> Map<V, K> invert(Map<K, V> map) {
        throw new UnsupportedOperationException("TODO 6 : implementer invert()");
    }

    public static <T> List<T> repeat(T value, int times) {
        throw new UnsupportedOperationException("TODO 7 : implementer repeat()");
    }

    public static String describeAll(List<?> items) {
        throw new UnsupportedOperationException("TODO 8 : implementer describeAll()");
    }

    public static <A, B> Pair<B, A> swapped(Pair<A, B> pair) {
        throw new UnsupportedOperationException("TODO 10 : implementer swapped()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  firstOrDefault(FRUITS, ?) == kiwi ; ([], ?) == ?",
                firstOrDefault(Pantry.FRUITS, "?").equals("kiwi") && firstOrDefault(List.<String>of(), "?").equals("?"));
        ExerciseChecker.check("2  maxOf(NUMBERS) == 9 ; maxOf(FRUITS) == pomme",
                maxOf(Pantry.NUMBERS) == 9 && maxOf(Pantry.FRUITS).equals("pomme"));
        ExerciseChecker.check("3  sum(NUMBERS) == 28.0 ; sum([1.5, 2.5]) == 4.0",
                sum(Pantry.NUMBERS) == 28.0 && sum(List.of(1.5, 2.5)) == 4.0);
        List<Number> sink = new ArrayList<>();
        fillWith(sink, 3);
        ExerciseChecker.check("4  fillWith(List<Number>, 3) -> [1, 2, 3]", sink.equals(List.of(1, 2, 3)));
        Predicate<Object> notNull = o -> o != null;
        ExerciseChecker.check("5  count(FRUITS, longueur > 5) == 3 ; count avec Predicate<Object> == 7",
                count(Pantry.FRUITS, s -> s.length() > 5) == 3 && count(Pantry.FRUITS, notNull) == 7);
        ExerciseChecker.check("6  invert(prices()).get(6) == cerise", invert(Pantry.prices()).get(6).equals("cerise"));
        ExerciseChecker.check("7  repeat(ab, 3) == [ab, ab, ab]", repeat("ab", 3).equals(List.of("ab", "ab", "ab")));
        ExerciseChecker.check("8  describeAll(NUMBERS) == 5,3,8,1,9,2", describeAll(Pantry.NUMBERS).equals("5,3,8,1,9,2"));
        ExerciseChecker.check("9  new Box<>(kiwi).map(String::length).get() == 4", new Box<>("kiwi").map(String::length).get() == 4);
        ExerciseChecker.check("10 swapped(Pair(kiwi, 3)) == Pair[first=3, second=kiwi]",
                swapped(new Pair<>("kiwi", 3)).toString().equals("Pair[first=3, second=kiwi]"));

        ExerciseChecker.summary();
    }
}
