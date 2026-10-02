package ch6_classdesign.projects.p05_arena.solution;

/**
 * SOLUTION - un mage : sorts puissants tant qu'il reste du mana, puis coups faibles.
 */
public class Mage extends Fighter {

    public static final int SPELL_COST = 10;
    private int mana = 30;

    public Mage(String name, int hp, int attack, int speed) {
        super(name, hp, attack, speed);
    }

    private Mage(Mage other) {
        super(other);
    }

    @Override
    protected int damageTo(Fighter target) {
        if (mana >= SPELL_COST) {
            mana -= SPELL_COST;
            return attack * 3 / 2;
        }
        return attack / 2;
    }

    @Override
    public Mage copy() {
        return new Mage(this);
    }

    @Override
    public String status() {
        return super.status() + " mana " + mana;   // completer la version du parent
    }
}
