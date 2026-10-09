package ch18_design.drills.r06_states.solution;

import java.util.ArrayList;
import java.util.List;

/** Le contexte : il delegue chaque action a son etat et note les changements. Aucun "if" sur l'etat. */
public final class Turnstile {

    private GateState state = new Locked();
    private int coins;
    private int passages;
    private final List<String> events = new ArrayList<>();

    public void coin() {
        change(state.coin(this));
    }

    public void push() {
        change(state.push(this));
    }

    public void breakDown() {
        change(state.breakDown(this));
    }

    public void repair() {
        change(state.repair(this));
    }

    private void change(GateState next) {
        events.add(state.label() + " -> " + next.label());
        state = next;
    }

    IllegalStateException refused(String action) {
        return new IllegalStateException(action + " refuse (" + state.label() + ")");
    }

    void countCoin() {
        coins++;
    }

    void countPassage() {
        passages++;
    }

    public String status() {
        return state.label();
    }

    public int coins() {
        return coins;
    }

    public int passages() {
        return passages;
    }

    public List<String> events() {
        return List.copyOf(events);
    }
}
