package ch6_classdesign.projects.p05_arena.solution;

/**
 * SOLUTION - un tank : son armure retire des points a chaque coup recu (minimum 1).
 */
public class Tank extends Fighter {

    public static final int ARMOR = 4;

    public Tank(String name, int hp, int attack, int speed) {
        super(name, hp, attack, speed);
    }

    private Tank(Tank other) {
        super(other);
    }

    @Override
    protected int damageTo(Fighter target) {
        return attack;
    }

    // Redefinition qui REUTILISE le parent : on reduit les degats, puis super fait le reste.
    @Override
    public int takeDamage(int damage) {
        return super.takeDamage(Math.max(1, damage - ARMOR));
    }

    @Override
    public Tank copy() {
        return new Tank(this);
    }
}
