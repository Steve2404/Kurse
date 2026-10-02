package ch7_beyondclasses.projects.p06_fleet.solution;

/**
 * SOLUTION - la racine de la flotte. Les capacites (rouler, naviguer, voler) viennent des INTERFACES.
 */
public abstract class Vehicle {

    protected final String name;

    protected Vehicle(String name) {
        this.name = name;
    }

    public abstract String kind();

    // Le cast vers une interface compile toujours pour une classe non finale ; il est SUR seulement
    // apres le test instanceof (sinon ClassCastException a l'execution).
    public int speedOn(Mode mode) {
        return switch (mode) {
            case ROAD -> this instanceof Drivable ? ((Drivable) this).roadSpeed() : 0;
            case SEA -> this instanceof Sailable ? ((Sailable) this).seaSpeed() : 0;
            case AIR -> this instanceof Flyable ? ((Flyable) this).airSpeed() : 0;
        };
    }

    public String capabilities() {
        StringBuilder sb = new StringBuilder();
        for (Mode m : Mode.values()) {
            if (speedOn(m) > 0) {
                sb.append(m).append('@').append(speedOn(m)).append(' ');
            }
        }
        return sb.toString().strip();
    }

    @Override
    public String toString() {
        return kind() + " " + name;
    }
}
