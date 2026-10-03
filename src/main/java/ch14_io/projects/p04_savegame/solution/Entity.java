package ch14_io.projects.p04_savegame.solution;

/**
 * SOLUTION - une classe mere NON serialisable. A la deserialisation d'une sous-classe serialisable,
 * SON constructeur sans argument est appele (il doit exister), et ses champs repartent de zero.
 */
public class Entity {

    static int constructions;
    protected String origin;

    public Entity() {
        constructions++;
        origin = "neuf";
    }
}
