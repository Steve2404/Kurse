package collections.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 8.
 */
public class Solution08_GenericsPecs {

    static class Box<T> {
        private T content;

        void set(T content) {
            this.content = content;
        }

        T get() {
            return content;
        }

        void copyContentTo(Box<? super T> destination) {
            destination.set(this.content);
        }
    }

    public static <T> void copy(List<? extends T> src, List<? super T> dest) {
        for (T item : src) {
            dest.add(item);
        }
    }

    public static double sumNumbers(List<? extends Number> list) {
        double total = 0;
        for (Number n : list) {
            total += n.doubleValue();
        }
        return total;
    }
}