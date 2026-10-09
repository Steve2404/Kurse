package ch18_design.projects.p07_editor.solution;

/** Inserer : l'annulation est l'operation INVERSE (supprimer ce qu'on a insere). Elle ne garde que le texte insere. */
public final class InsertCommand implements Command {

    private final Document document;
    private final int position;
    private final String text;

    public InsertCommand(Document document, int position, String text) {
        this.document = document;
        this.position = position;
        this.text = text;
    }

    @Override
    public void execute() {
        document.insert(position, text);
    }

    @Override
    public void undo() {
        document.delete(position, text.length());
    }

    @Override
    public String label() {
        return "inserer '" + text + "'";
    }
}
