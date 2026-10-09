package ch18_design.drills.r05_commands.solution;

import java.util.ArrayDeque;
import java.util.Deque;

/** L'historique : deux piles ; une nouvelle action vide la pile "refaire". */
public final class Undo {

    private final Deque<Action> done = new ArrayDeque<>();
    private final Deque<Action> undone = new ArrayDeque<>();

    public void perform(Action action) {
        action.apply();
        done.push(action);
        undone.clear();
    }

    public boolean undo() {
        if (done.isEmpty()) {
            return false;
        }
        Action action = done.pop();
        action.revert();
        undone.push(action);
        return true;
    }

    public boolean redo() {
        if (undone.isEmpty()) {
            return false;
        }
        Action action = undone.pop();
        action.apply();
        done.push(action);
        return true;
    }
}
