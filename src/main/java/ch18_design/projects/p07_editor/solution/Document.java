package ch18_design.projects.p07_editor.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * Le document : le SUJET observe. Il ne sait faire que des operations simples (inserer, supprimer,
 * photographier, restaurer) et previent ses abonnes apres chacune.
 */
public final class Document {

    /**
     * Le MEMENTO : une photographie du texte, OPAQUE. Personne d'autre que Document ne peut la lire
     * ni la fabriquer ; on peut seulement la garder et la rendre a restore().
     */
    public static final class Snapshot {
        private final String text;

        private Snapshot(String text) {
            this.text = text;
        }
    }

    private final StringBuilder text = new StringBuilder();
    private final List<DocumentListener> listeners = new ArrayList<>();

    public String text() {
        return text.toString();
    }

    public void insert(int position, String inserted) {
        checkPosition(position);
        text.insert(position, inserted);
        fire(new DocumentEvent("insert", position, inserted));
    }

    /** Supprime length caracteres a partir de position, et rend le texte retire (pour pouvoir le remettre). */
    public String delete(int position, int length) {
        checkPosition(position);
        if (length < 0 || position + length > text.length()) {
            throw new IllegalArgumentException("longueur invalide : " + length);
        }
        String removed = text.substring(position, position + length);
        text.delete(position, position + length);
        fire(new DocumentEvent("delete", position, removed));
        return removed;
    }

    public Snapshot snapshot() {
        return new Snapshot(text.toString());
    }

    public void restore(Snapshot snapshot) {
        text.setLength(0);
        text.append(snapshot.text);
        fire(new DocumentEvent("restore", 0, snapshot.text));
    }

    public void addListener(DocumentListener listener) {
        listeners.add(listener);
    }

    public void removeListener(DocumentListener listener) {
        listeners.remove(listener);
    }

    // De 0 a la longueur INCLUSE : on peut inserer a la toute fin du texte.
    private void checkPosition(int position) {
        if (position < 0 || position > text.length()) {
            throw new IllegalArgumentException("position invalide : " + position);
        }
    }

    // Sur une COPIE de la liste : un abonne peut se desabonner pendant qu'on le previent.
    private void fire(DocumentEvent event) {
        List.copyOf(listeners).forEach(listener -> listener.changed(event));
    }
}
