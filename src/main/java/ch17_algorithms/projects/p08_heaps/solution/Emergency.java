package ch17_algorithms.projects.p08_heaps.solution;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/** La salle d'attente des urgences : le plus grave d'abord ; a gravite egale, le premier arrive. */
public final class Emergency {

    // Pourquoi un Comparator : PriorityQueue sort TOUJOURS le plus PETIT selon son ordre ; on definit donc
    // "plus petit" comme "plus urgent".
    private final PriorityQueue<Patient> waiting = new PriorityQueue<>(
            Comparator.comparingInt(Patient::severity).reversed().thenComparingInt(Patient::arrival));

    public void arrive(Patient p) {
        waiting.add(p);
    }

    public Patient next() {
        Patient p = waiting.poll();
        if (p == null) {
            throw new NoSuchElementException("personne en attente");
        }
        return p;
    }

    public int waitingCount() {
        return waiting.size();
    }
}
