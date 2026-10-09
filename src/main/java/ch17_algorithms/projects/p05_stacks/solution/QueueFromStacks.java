package ch17_algorithms.projects.p05_stacks.solution;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Une file (premier entre, premier sorti) faite de DEUX piles : on entre dans l'une, on sort de l'autre.
 * Quand la pile de sortie est vide, on y verse toute la pile d'entree : l'ordre s'inverse, donc se retablit.
 * Chaque element est verse UNE seule fois : O(1) en moyenne (amorti).
 */
public final class QueueFromStacks<T> {

    private final Deque<T> in = new ArrayDeque<>();
    private final Deque<T> out = new ArrayDeque<>();

    public void offer(T value) {
        in.push(value);
    }

    public T poll() {
        refill();
        return out.isEmpty() ? null : out.pop();
    }

    public T peek() {
        refill();
        return out.peek();
    }

    public int size() {
        return in.size() + out.size();
    }

    // Piege : ne verser QUE si la sortie est vide ; sinon on melangerait l'ordre.
    private void refill() {
        if (out.isEmpty()) {
            while (!in.isEmpty()) {
                out.push(in.pop());
            }
        }
    }
}
