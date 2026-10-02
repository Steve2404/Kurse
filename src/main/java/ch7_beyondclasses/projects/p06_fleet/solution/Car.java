package ch7_beyondclasses.projects.p06_fleet.solution;

/**
 * SOLUTION - une voiture.
 */
public class Car extends Vehicle implements Drivable {

    public Car(String name) {
        super(name);
    }

    @Override
    public String kind() {
        return "voiture";
    }

    @Override
    public int roadSpeed() {
        return 90;
    }
}
