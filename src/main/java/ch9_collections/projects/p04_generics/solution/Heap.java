package ch9_collections.projects.p04_generics.solution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * SOLUTION - un tas binaire GENERIQUE : il fonctionne pour tout T, grace au Comparator recu.
 * Comparator<? super T> : un comparateur de T ou d'un de ses parents convient (un Comparator<Object> marche pour des String).
 */
public class Heap<T> {

    private final List<T> items = new ArrayList<>();       // on ne peut pas faire new T[n] : effacement de type
    private final Comparator<? super T> order;

    public Heap(Comparator<? super T> order) {
        this.order = order;
    }

    public void push(T value) {
        items.add(value);
        int i = items.size() - 1;
        while (i > 0 && order.compare(items.get(i), items.get((i - 1) / 2)) < 0) {
            swap(i, (i - 1) / 2);
            i = (i - 1) / 2;
        }
    }

    public T pop() {
        T top = items.get(0);
        T last = items.remove(items.size() - 1);
        if (!items.isEmpty()) {
            items.set(0, last);
            int i = 0;
            while (true) {
                int l = 2 * i + 1;
                int r = l + 1;
                int m = i;
                if (l < items.size() && order.compare(items.get(l), items.get(m)) < 0) {
                    m = l;
                }
                if (r < items.size() && order.compare(items.get(r), items.get(m)) < 0) {
                    m = r;
                }
                if (m == i) {
                    break;
                }
                swap(i, m);
                i = m;
            }
        }
        return top;
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    private void swap(int a, int b) {
        T t = items.get(a);
        items.set(a, items.get(b));
        items.set(b, t);
    }
}
