package ch7_beyondclasses.projects.p03_turtle.solution;

/**
 * SOLUTION - avancer de steps cases. Un record est implicitement final : il convient a une interface scellee.
 */
public record Move(int steps) implements Command {

    // Constructeur compact : on normalise la valeur avant l'affectation du champ.
    public Move {
        if (steps < 0) {
            steps = 0;
        }
    }

    @Override
    public int size() {
        return 1;
    }
}
