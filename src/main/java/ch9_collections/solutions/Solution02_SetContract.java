package ch9_collections.solutions;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Corrige de l'exercice 2.
 */
public class Solution02_SetContract {

    static final class Point {
        final int x;
        final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Point)) {
                return false;
            }
            Point other = (Point) o;
            return this.x == other.x;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x);
        }
    }

    public static <T> Set<T> symmetricDifference(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<>();
        for (T item : a) {
            if (!b.contains(item)) {
                result.add(item);
            }
        }
        for (T item : b) {
            if (!a.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public static TreeSet<String> caseInsensitiveTreeSet(Iterable<String> words) {
        TreeSet<String> result = new TreeSet<>(Comparator.comparing(String::toLowerCase));
        for (String word : words) {
            result.add(word);
        }
        return result;
    }
}