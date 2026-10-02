package ch7_beyondclasses.projects.p03_turtle.solution;

/**
 * SOLUTION - un record SANS composant : il ne porte aucune donnee, juste un type.
 */
public record PenUp() implements Command {

    @Override
    public int size() {
        return 1;
    }
}
