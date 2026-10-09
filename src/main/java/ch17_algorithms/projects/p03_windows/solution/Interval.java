package ch17_algorithms.projects.p03_windows.solution;

/** Un creneau [start, end[ : end est EXCLU (une reunion de 9 a 10 libere la salle a 10). */
public record Interval(int start, int end) {

    public Interval {
        if (end <= start) {
            throw new IllegalArgumentException("intervalle vide : [" + start + ", " + end + "[");
        }
    }
}
