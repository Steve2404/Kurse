package ch18_design.projects.p07_editor.solution;

/** Un observateur : il recompte les mots a chaque changement. Le document ne sait pas qu'il existe. */
public final class WordCounter implements DocumentListener {

    private final Document document;
    private int count;

    private WordCounter(Document document) {
        this.document = document;
    }

    // Une fabrique qui abonne : on evite d'ecrire "document.addListener(this)" dans le constructeur
    // (l'objet serait publie avant d'etre fini de construire).
    public static WordCounter attachTo(Document document) {
        WordCounter counter = new WordCounter(document);
        document.addListener(counter);
        counter.count = words(document.text());
        return counter;
    }

    @Override
    public void changed(DocumentEvent event) {
        count = words(document.text());
    }

    public int count() {
        return count;
    }

    // Des mots separes par des blancs, quel que soit leur nombre ; un texte vide ou blanc en a 0.
    static int words(String text) {
        return text.isBlank() ? 0 : text.strip().split("\\s+").length;
    }
}
