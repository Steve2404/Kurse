package ch18_design.projects.p08_workflow.solution;

/** Les etats finaux (annulee, remboursee) : ils refusent tout. Une seule classe, deux libelles. */
public final class ClosedState implements OrderState {

    private final String label;

    private ClosedState(String label) {
        this.label = label;
    }

    public static ClosedState cancelled() {
        return new ClosedState("annulee");
    }

    public static ClosedState refunded() {
        return new ClosedState("remboursee");
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public boolean isClosed() {
        return true;
    }
}
