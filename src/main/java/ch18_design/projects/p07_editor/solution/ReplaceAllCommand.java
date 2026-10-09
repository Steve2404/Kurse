package ch18_design.projects.p07_editor.solution;

/**
 * Remplacer partout : l'operation inverse serait difficile (le remplacement peut creer de nouvelles
 * occurrences). On prend donc une PHOTOGRAPHIE (memento) avant, et l'on restaure pour annuler.
 */
public final class ReplaceAllCommand implements Command {

    private final Document document;
    private final String target;
    private final String replacement;
    private Document.Snapshot before;

    public ReplaceAllCommand(Document document, String target, String replacement) {
        this.document = document;
        this.target = target;
        this.replacement = replacement;
    }

    @Override
    public void execute() {
        before = document.snapshot();
        String replaced = document.text().replace(target, replacement);
        document.delete(0, document.text().length());
        document.insert(0, replaced);
    }

    @Override
    public void undo() {
        document.restore(before);
    }

    @Override
    public String label() {
        return "remplacer '" + target + "' par '" + replacement + "'";
    }
}
