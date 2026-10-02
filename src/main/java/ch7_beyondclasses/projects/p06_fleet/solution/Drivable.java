package ch7_beyondclasses.projects.p06_fleet.solution;

/**
 * SOLUTION - ce qui roule.
 */
public interface Drivable {

    int roadSpeed();

    default String horn() {
        return "tut";
    }
}
