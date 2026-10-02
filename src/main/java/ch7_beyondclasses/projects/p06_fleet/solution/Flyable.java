package ch7_beyondclasses.projects.p06_fleet.solution;

/**
 * SOLUTION - ce qui vole.
 */
public interface Flyable {

    int airSpeed();

    default int altitude() {
        return 1000;
    }
}
