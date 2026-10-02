package ch8_lambdas.projects.p03_events.solution;

/**
 * SOLUTION - un evenement planifie : quand, dans quel ordre a egalite (seq), et quoi faire (un Runnable).
 */
public record Event(int time, int seq, String label, Runnable action) {

    boolean before(Event other) {
        return time < other.time || time == other.time && seq < other.seq;
    }
}
