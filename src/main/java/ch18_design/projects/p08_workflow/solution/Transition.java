package ch18_design.projects.p08_workflow.solution;

/** Un changement d'etat, par exemple de "payee" vers "expediee". */
public record Transition(String from, String to) {

    @Override
    public String toString() {
        return from + " -> " + to;
    }
}
