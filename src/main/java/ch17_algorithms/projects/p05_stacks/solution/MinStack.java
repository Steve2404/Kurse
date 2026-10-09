package ch17_algorithms.projects.p05_stacks.solution;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

/** Une pile qui connait son minimum en O(1) : une 2e pile retient le minimum A CHAQUE hauteur. */
public final class MinStack {

    private final Deque<Integer> values = new ArrayDeque<>();
    private final Deque<Integer> minimums = new ArrayDeque<>();

    public void push(int v) {
        values.push(v);
        minimums.push(minimums.isEmpty() ? v : Math.min(v, minimums.peek()));
    }

    public int pop() {
        check();
        minimums.pop();
        return values.pop();
    }

    public int peek() {
        check();
        return values.peek();
    }

    public int min() {
        check();
        return minimums.peek();
    }

    public int size() {
        return values.size();
    }

    private void check() {
        if (values.isEmpty()) {
            throw new NoSuchElementException("pile vide");
        }
    }
}
