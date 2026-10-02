package ch6_classdesign.drills.r09_kata.solution;

/**
 * SOLUTION du drill de rappel 9 - kata mixte du chapitre 6.
 */
public class Recall09 {

    static final StringBuilder LOG = new StringBuilder();

    public static void main(String[] args) {
        Vehicle v = new Bike("BMX");
        System.out.println("D01 : " + LOG.toString().strip());
        System.out.println("D02 : " + v.describe() + " " + v.wheels() + " " + v.kind + " " + Vehicle.category() + " " + Bike.category());
        Bike b = new Bike();
        System.out.println("D03 : " + b.describe() + " " + Vehicle.built);
        Wheel w = Wheel.of(26);
        System.out.println("D04 : " + w + " " + w.bigger(2) + " " + w + " " + w.equals(Wheel.of(26)));
        System.out.println("D05 : " + v + " " + (v instanceof Bike) + " " + v.equals(new Bike("BMX")));
    }
}

abstract class Vehicle {
    static int built;
    String kind = "vehicule";
    protected final String name;

    protected Vehicle(String name) {
        this.name = name;
        built++;
        Recall09.LOG.append(" Vehicle(").append(name).append(')');
    }

    abstract int wheels();

    String describe() {
        return name + " a " + wheels() + " roues";
    }

    static String category() {
        return "transport";
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + name + "]";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Vehicle other && other.name.equals(name) && other.wheels() == wheels();
    }

    @Override
    public int hashCode() {
        return name.hashCode() * 31 + wheels();
    }
}

class Bike extends Vehicle {
    String kind = "velo";

    Bike(String name) {
        super(name);
        Recall09.LOG.append(" Bike(String)");
    }

    Bike() {
        this("anonyme");
        Recall09.LOG.append(" Bike()");
    }

    @Override
    int wheels() {
        return 2;
    }

    @Override
    String describe() {
        return "[" + super.describe() + "]";
    }

    static String category() {
        return "deux-roues";
    }
}

final class Wheel {
    private final int inches;

    private Wheel(int inches) {
        this.inches = inches;
    }

    static Wheel of(int inches) {
        return new Wheel(inches);
    }

    Wheel bigger(int delta) {
        return new Wheel(inches + delta);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Wheel w && w.inches == inches;
    }

    @Override
    public int hashCode() {
        return inches;
    }

    @Override
    public String toString() {
        return inches + "\"";
    }
}
