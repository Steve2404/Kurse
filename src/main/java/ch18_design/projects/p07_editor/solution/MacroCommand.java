package ch18_design.projects.p07_editor.solution;

import java.util.List;

/**
 * Une commande faite de commandes (un COMPOSITE de commandes) : elle s'annule d'un seul coup.
 * On defait dans l'ordre INVERSE : la derniere faite est la premiere defaite.
 */
public final class MacroCommand implements Command {

    private final String label;
    private final List<Command> steps;

    public MacroCommand(String label, List<Command> steps) {
        this.label = label;
        this.steps = List.copyOf(steps);
    }

    @Override
    public void execute() {
        steps.forEach(Command::execute);
    }

    @Override
    public void undo() {
        for (int i = steps.size() - 1; i >= 0; i--) {
            steps.get(i).undo();
        }
    }

    @Override
    public String label() {
        return label;
    }
}
