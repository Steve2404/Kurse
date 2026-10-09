package ch18_design.drills.r05_commands.solution;

import java.util.List;

/** Plusieurs actions comme une seule : appliquees dans l'ordre, defaites dans l'ordre inverse. */
public final class Batch implements Action {

    private final List<Action> actions;

    public Batch(List<Action> actions) {
        this.actions = List.copyOf(actions);
    }

    @Override
    public void apply() {
        actions.forEach(Action::apply);
    }

    @Override
    public void revert() {
        for (int i = actions.size() - 1; i >= 0; i--) {
            actions.get(i).revert();
        }
    }
}
