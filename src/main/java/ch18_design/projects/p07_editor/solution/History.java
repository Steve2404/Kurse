package ch18_design.projects.p07_editor.solution;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * L'historique : deux piles de commandes. Il ne connait AUCUNE commande concrete : il ne sait que
 * execute(), undo() et label(). Une nouvelle commande marche sans le modifier.
 */
public final class History {

    private final int capacity;
    private final Deque<Command> undoStack = new ArrayDeque<>();
    private final Deque<Command> redoStack = new ArrayDeque<>();

    public History(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacite invalide : " + capacity);
        }
        this.capacity = capacity;
    }

    // Une nouvelle action efface l'avenir : ce qu'on avait annule ne peut plus etre refait.
    public void run(Command command) {
        command.execute();
        undoStack.push(command);
        if (undoStack.size() > capacity) {
            undoStack.removeLast();
        }
        redoStack.clear();
    }

    public boolean undo() {
        if (undoStack.isEmpty()) {
            return false;
        }
        Command command = undoStack.pop();
        command.undo();
        redoStack.push(command);
        return true;
    }

    public boolean redo() {
        if (redoStack.isEmpty()) {
            return false;
        }
        Command command = redoStack.pop();
        command.execute();
        undoStack.push(command);
        return true;
    }

    /** Les libelles de ce qu'on peut annuler, la plus recente d'abord (le menu "Annuler ..."). */
    public List<String> undoLabels() {
        return undoStack.stream().map(Command::label).toList();
    }
}
