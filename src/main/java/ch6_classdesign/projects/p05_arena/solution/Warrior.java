package ch6_classdesign.projects.p05_arena.solution;

/**
 * SOLUTION - un guerrier : un coup sur trois est critique (double).
 */
public class Warrior extends Fighter {

    private int hits;

    public Warrior(String name, int hp, int attack, int speed) {
        super(name, hp, attack, speed);
    }

    private Warrior(Warrior other) {
        super(other);
    }

    @Override
    protected int damageTo(Fighter target) {
        hits++;
        return hits % 3 == 0 ? attack * 2 : attack;
    }

    // Covariant : rend un Warrior, pas seulement un Fighter.
    @Override
    public Warrior copy() {
        return new Warrior(this);
    }
}
