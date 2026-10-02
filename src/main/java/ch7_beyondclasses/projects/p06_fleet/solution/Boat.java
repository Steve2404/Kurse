package ch7_beyondclasses.projects.p06_fleet.solution;

/**
 * SOLUTION - un bateau.
 */
public class Boat extends Vehicle implements Sailable {

    public Boat(String name) {
        super(name);
    }

    @Override
    public String kind() {
        return "bateau";
    }

    @Override
    public int seaSpeed() {
        return 40;
    }
}
