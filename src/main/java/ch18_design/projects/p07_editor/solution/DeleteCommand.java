package ch18_design.projects.p07_editor.solution;

/** Supprimer : on ne connait le texte retire qu'a l'execution ; on le garde pour pouvoir le remettre. */
public final class DeleteCommand implements Command {

    private final Document document;
    private final int position;
    private final int length;
    private String removed = "";

    public DeleteCommand(Document document, int position, int length) {
        this.document = document;
        this.position = position;
        this.length = length;
    }

    @Override
    public void execute() {
        removed = document.delete(position, length);
    }

    @Override
    public void undo() {
        document.insert(position, removed);
    }

    @Override
    public String label() {
        return "supprimer " + length;
    }
}
