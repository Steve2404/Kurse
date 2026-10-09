package ch17_algorithms.projects.p08_heaps.solution;

/** Un patient des urgences : la gravite (5 = vital, 1 = leger) et son ordre d'arrivee. */
public record Patient(String name, int severity, int arrival) {
}
