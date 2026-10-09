package ch18_design.projects.p07_editor.solution;

/**
 * L'OBSERVATEUR : quiconque veut etre prevenu des changements s'abonne. Le document ne connait que
 * cette interface : il ignore s'il y a un compteur de mots, un journal, un ecran ou rien du tout.
 */
@FunctionalInterface
public interface DocumentListener {

    void changed(DocumentEvent event);
}
