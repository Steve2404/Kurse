package ch7_beyondclasses.projects.p02_poker.solution;

/**
 * SOLUTION - une interface que les deux enums implementent.
 */
public interface Symbolic {

    char symbol();

    default String describe() {
        return symbol() + "=" + this;   // toString() d'un enum = son name() par defaut
    }
}
