package ch8_lambdas.solutions;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Corrige de l'exercice 20. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise20_UndoRedoCommands.
 */
public class Solution20_UndoRedoCommands {

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
            // Executer, empiler pour annuler, et vider la pile refaire (une nouvelle action la rend caduque).
            command.doAction.run();
            undoStack.push(command);
            redoStack.clear();
        }

        void undo() {
            // Deplacer la commande de la pile annuler vers la pile refaire, en appelant son undoAction.
            if (undoStack.isEmpty()) {
                return;
            }
            Command command = undoStack.pop();
            command.undoAction.run();
            redoStack.push(command);
        }

        void redo() {
            // Le chemin inverse de undo.
            if (redoStack.isEmpty()) {
                return;
            }
            Command command = redoStack.pop();
            command.doAction.run();
            undoStack.push(command);
        }
    }
}
