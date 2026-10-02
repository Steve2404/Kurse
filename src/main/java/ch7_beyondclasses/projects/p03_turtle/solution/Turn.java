package ch7_beyondclasses.projects.p03_turtle.solution;

/**
 * SOLUTION - tourner d'un quart de tour : +1 a droite, -1 a gauche.
 */
public record Turn(int quarters) implements Command {

    public static final Turn RIGHT = new Turn(1);   // un champ static est permis dans un record
    public static final Turn LEFT = new Turn(-1);

    @Override
    public int size() {
        return 1;
    }
}
