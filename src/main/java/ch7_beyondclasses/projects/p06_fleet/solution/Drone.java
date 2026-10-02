package ch7_beyondclasses.projects.p06_fleet.solution;

/**
 * SOLUTION - un drone : il vole bas (il redefinit la methode default altitude()).
 */
public class Drone extends Vehicle implements Flyable {

    public Drone(String name) {
        super(name);
    }

    @Override
    public String kind() {
        return "drone";
    }

    @Override
    public int airSpeed() {
        return 80;
    }

    @Override
    public int altitude() {
        return 120;
    }
}
