package ch9_collections.projects.p05_ranking.solution;

/**
 * SOLUTION - un intervalle [start, end], ordonne naturellement par debut puis fin.
 */
public record Interval(int start, int end) implements Comparable<Interval> {

    @Override
    public int compareTo(Interval o) {
        int c = Integer.compare(start, o.start);
        return c != 0 ? c : Integer.compare(end, o.end);
    }

    @Override
    public String toString() {
        return "[" + start + "," + end + "]";
    }
}
