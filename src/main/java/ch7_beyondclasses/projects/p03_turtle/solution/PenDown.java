package ch7_beyondclasses.projects.p03_turtle.solution;

/**
 * SOLUTION - baisser le crayon.
 */
public record PenDown() implements Command {

    @Override
    public int size() {
        return 1;
    }
}
