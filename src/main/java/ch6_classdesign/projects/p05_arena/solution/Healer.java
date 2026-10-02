package ch6_classdesign.projects.p05_arena.solution;

/**
 * SOLUTION - un soigneur : il soigne l'allie le plus faible s'il est sous la moitie de ses pv, sinon il frappe.
 */
public class Healer extends Fighter {

    public static final int HEAL = 25;

    public Healer(String name, int hp, int attack, int speed) {
        super(name, hp, attack, speed);
    }

    private Healer(Healer other) {
        super(other);
    }

    @Override
    protected int damageTo(Fighter target) {
        return attack;
    }

    @Override
    public String act(Fighter[] allies, Fighter[] enemies) {
        Fighter weak = weakest(allies);
        if (weak.getHp() * 2 < weak.getMaxHp()) {
            int healed = weak.heal(HEAL);
            return getName() + " soigne " + weak.getName() + " +" + healed + " (" + weak.status() + ")";
        }
        return super.act(allies, enemies);   // sinon : le comportement par defaut du parent
    }

    @Override
    public Healer copy() {
        return new Healer(this);
    }
}
