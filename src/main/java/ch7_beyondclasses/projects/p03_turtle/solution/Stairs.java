package ch7_beyondclasses.projects.p03_turtle.solution;

/**
 * SOLUTION - un escalier de n marches, vers le bas a droite.
 */
public class Stairs extends Macro {

    private final int steps;

    public Stairs(int steps) {
        super("STAIRS");
        this.steps = steps;
    }

    @Override
    public Command[] expand() {
        return new Command[] {new Repeat(steps, new Command[] {new Move(2), Turn.RIGHT, new Move(1), Turn.LEFT})};
    }
}
