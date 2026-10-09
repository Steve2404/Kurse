package ch17_algorithms.projects.p08_heaps.solution;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/**
 * La mediane d'un flux de nombres, a tout moment, en O(log n) par ajout.
 * Deux tas : un tas MAX pour la moitie basse, un tas MIN pour la moitie haute ; la mediane est a leurs sommets.
 */
public final class MedianFinder {

    private final PriorityQueue<Integer> low = new PriorityQueue<>(Collections.reverseOrder());
    private final PriorityQueue<Integer> high = new PriorityQueue<>();

    // Toujours : tout element de low <= tout element de high, et low a autant ou UN de plus que high.
    public void add(int v) {
        low.add(v);
        high.add(low.poll());
        if (high.size() > low.size()) {
            low.add(high.poll());
        }
    }

    // Piege : la moyenne de deux int se calcule en long, sinon elle deborde pres de Integer.MAX_VALUE.
    public double median() {
        if (low.isEmpty()) {
            throw new NoSuchElementException("aucun nombre");
        }
        if (low.size() > high.size()) {
            return low.peek();
        }
        return ((long) low.peek() + high.peek()) / 2.0;
    }
}
