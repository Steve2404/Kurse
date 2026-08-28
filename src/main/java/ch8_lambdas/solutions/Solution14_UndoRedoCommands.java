package ch8_lambdas.solutions;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Corrige de l'exercice 14. A ne consulter qu'apres avoir essaye par
 * vous-meme dans lambdas.exercises.Exercise14_UndoRedoCommands.
 */
public class Solution14_UndoRedoCommands {

    static final class Command {
        final Runnable doAction;
        final Runnable undoAction;

        Command(Runnable doAction, Runnable undoAction) {
            this.doAction = doAction;
            this.undoAction = undoAction;
        }
    }

    static class UndoRedoManager {
        private final Deque<Command> undoStack = new ArrayDeque<>();
        private final Deque<Command> redoStack = new ArrayDeque<>();

        void execute(Command command) {
            command.doAction.run();
            undoStack.push(command);
            redoStack.clear();
        }

        void undo() {
            if (undoStack.isEmpty()) {
                return;
            }
            Command command = undoStack.pop();
            command.undoAction.run();
            redoStack.push(command);
        }

        void redo() {
            if (redoStack.isEmpty()) {
                return;
            }
            Command command = redoStack.pop();
            command.doAction.run();
            undoStack.push(command);
        }
    }
}
