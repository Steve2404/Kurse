package ch8_lambdas.exercises;

import ch8_lambdas.ExerciseChecker;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * EXERCICE 14 - Pile Annuler/Refaire avec des commandes fonctionnelles (niveau : capstone, style entretien)
 * ===================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CustomFunctionalInterface.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine un editeur de texte avec un bouton "Annuler" (Ctrl+Z) et un
 * bouton "Refaire" (Ctrl+Y). Chaque action que tu fais (taper un mot,
 * le supprimer...) est en realite fabriquee en DOUBLE des le depart :
 * "comment la FAIRE" (Runnable doAction) ET "comment DEFAIRE ce
 * qu'elle vient de faire" (Runnable undoAction) - les deux ensemble
 * forment une Command. Runnable est ici la boite magique la plus
 * simple qui existe : "fais quelque chose, sans ingredient, sans rien
 * rendre".
 *
 * Ce capstone reutilise le patron a-deux-piles de
 * collections.exercises.Exercise04_BrowserHistory (Deque comme pile),
 * mais chaque "page" devient ici une PAIRE de fonctions plutot qu'une
 * simple chaine de caracteres.
 *
 *
 * ==================================================================
 * TODO 1 : UndoRedoManager.execute(command)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Executer VRAIMENT l'action : command.doAction.run().
 *   2. Empiler cette commande sur la pile "annuler" (undoStack), pour
 *      pouvoir un jour revenir en arriere dessus.
 *   3. Vider COMPLETEMENT la pile "refaire" (redoStack) : exactement
 *      comme dans BrowserHistory, une NOUVELLE action rend caduques
 *      toutes les anciennes actions "a refaire".
 *
 *
 * ==================================================================
 * TODO 2 : UndoRedoManager.undo()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Si la pile "annuler" est vide, ne rien faire (rien a annuler).
 *   2. Sinon : depiler la derniere commande de undoStack.
 *   3. Executer SON undoAction (defaire vraiment ce qu'elle avait
 *      fait).
 *   4. Empiler cette MEME commande sur redoStack (pour pouvoir la
 *      refaire plus tard si besoin).
 *
 *
 * ==================================================================
 * TODO 3 : UndoRedoManager.redo()
 * ==================================================================
 *
 * -- Le plan --
 *
 * Exactement le symetrique de undo() : depiler de redoStack, executer
 * le doAction (pas le undoAction, cette fois) de la commande depilee,
 * et la remettre sur undoStack.
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee entre eux ? --
 *
 * Non : chacun est deja sa propre methode publique, courte (3-4
 * lignes chacune). Remarque, comme dans BrowserHistory, que undo() et
 * redo() sont quasiment le miroir l'un de l'autre.
 *
 * Exemple a verifier : sur un compteur qui demarre a 0, on empile 3
 * commandes "+1" (chacune avec son "-1" pour annuler). Apres les 3
 * executions : compteur == 3. Apres 2x undo() : compteur == 1. Apres
 * 1x redo() : compteur == 2. Une NOUVELLE commande executee apres un
 * undo() doit effacer la pile "refaire" restante (exactement comme
 * une nouvelle visite efface le forward() dans BrowserHistory).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - execute() : command.doAction.run(); undoStack.push(command);
 *     redoStack.clear();
 *   - undo() : si undoStack.isEmpty(), return. Sinon : Command c =
 *     undoStack.pop(); c.undoAction.run(); redoStack.push(c);
 *   - redo() : symetrique, en inversant les deux piles et en
 *     executant doAction au lieu de undoAction.
 */
public class Exercise14_UndoRedoCommands {

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
            throw new UnsupportedOperationException("TODO 1 : implementer execute()");
        }

        void undo() {
            throw new UnsupportedOperationException("TODO 2 : implementer undo()");
        }

        void redo() {
            throw new UnsupportedOperationException("TODO 3 : implementer redo()");
        }
    }

    public static void main(String[] args) {
        int[] counter = {0};
        UndoRedoManager manager = new UndoRedoManager();

        Command increment = new Command(() -> counter[0]++, () -> counter[0]--);

        manager.execute(increment);
        manager.execute(increment);
        manager.execute(increment);
        ExerciseChecker.check("3 commandes +1 executees -> compteur == 3", counter[0] == 3);

        manager.undo();
        manager.undo();
        ExerciseChecker.check("2x undo() -> compteur == 1", counter[0] == 1);

        manager.redo();
        ExerciseChecker.check("1x redo() -> compteur == 2", counter[0] == 2);

        manager.undo();
        Command decrement = new Command(() -> counter[0] -= 10, () -> counter[0] += 10);
        manager.execute(decrement);
        ExerciseChecker.check("nouvelle commande apres un undo() -> compteur == 1 - 10 == -9", counter[0] == -9);

        manager.redo();
        ExerciseChecker.check("redo() apres une nouvelle commande ne fait rien (pile refaire videe)",
                counter[0] == -9);

        ExerciseChecker.summary();
    }
}
