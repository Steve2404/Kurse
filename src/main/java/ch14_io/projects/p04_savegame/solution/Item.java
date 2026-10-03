package ch14_io.projects.p04_savegame.solution;

import java.io.Serializable;

/**
 * SOLUTION - un record serialisable. A la deserialisation, un record passe par son constructeur CANONIQUE
 * (contrairement a une classe) : la validation du constructeur compact s'execute aussi.
 */
public record Item(String name, int power) implements Serializable {

    static int validations;

    public Item {
        validations++;
        if (power < 0) {
            throw new IllegalArgumentException("puissance negative");
        }
    }
}
