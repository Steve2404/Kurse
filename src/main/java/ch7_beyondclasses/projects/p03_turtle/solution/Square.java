package ch7_beyondclasses.projects.p03_turtle.solution;

/**
 * SOLUTION - un carre de cote donne : une macro (Macro est non-sealed, donc extensible librement).
 */
public class Square extends Macro {

    private final int side;

    public Square(int side) {
        super("SQUARE");
        this.side = side;
    }

    @Override
    public Command[] expand() {
        return new Command[] {new Repeat(4, new Command[] {new Move(side), Turn.RIGHT})};
    }
}
