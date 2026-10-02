package ch7_beyondclasses.projects.p06_fleet.solution;

/**
 * SOLUTION - un avion : il vole et roule (au sol, lentement). Deux interfaces, une classe parente.
 */
public class Plane extends Vehicle implements Flyable, Drivable {

    public Plane(String name) {
        super(name);
    }

    @Override
    public String kind() {
        return "avion";
    }

    @Override
    public int airSpeed() {
        return 600;
    }

    @Override
    public int roadSpeed() {
        return 30;
    }
}
