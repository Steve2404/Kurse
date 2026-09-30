package ch9_collections.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 17.
 */
public class Solution17_GenericsPecs {

    static class Box<T> {
        private T content;

        void set(T content) {
            this.content = content;
        }

        T get() {
            return content;
        }

        void copyContentTo(Box<? super T> destination) {
            // Consumer Super : la destination peut recevoir un T (Box<T>, Box<Number>, Box<Object>...).
            destination.set(this.content);
        }
    }

    public static <T> void copy(List<? extends T> src, List<? super T> dest) {
        // PECS complet : on LIT src (extends) et on ECRIT dans dest (super).
        for (T item : src) {
            dest.add(item);
        }
    }

    public static double sumNumbers(List<? extends Number> list) {
        // Producer Extends : on ne fait que lire des Number ; add serait refuse par le compilateur.
        double total = 0;
        for (Number n : list) {
            total += n.doubleValue();
        }
        return total;
    }
}