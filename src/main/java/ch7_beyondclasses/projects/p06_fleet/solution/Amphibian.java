package ch7_beyondclasses.projects.p06_fleet.solution;

/**
 * SOLUTION - un amphibie EST une voiture (extends) et SAIT naviguer (implements).
 */
public class Amphibian extends Car implements Sailable {

    public Amphibian(String name) {
        super(name);
    }

    @Override
    public String kind() {
        return "amphibie";
    }

    @Override
    public int roadSpeed() {
        return 60;   // redefinit Car.roadSpeed()
    }

    @Override
    public int seaSpeed() {
        return 15;
    }

    @Override
    public String horn() {
        // Drivable.super.horn() serait refuse : Drivable n est pas une super-interface DIRECTE d Amphibian (c est Car qui l implemente).
        return "pouet-" + super.horn();
    }
}
