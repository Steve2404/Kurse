package ch6_classdesign.projects.p05_arena.solution;

/**
 * SOLUTION - un combattant abstrait : l'etat et le comportement communs.
 */
public abstract class Fighter {

    private final String name;
    private final int maxHp;
    private int hp;
    protected final int attack;
    private final int speed;

    protected Fighter(String name, int hp, int attack, int speed) {
        this.name = name;
        this.maxHp = hp;
        this.hp = hp;
        this.attack = attack;
        this.speed = speed;
    }

    // Le constructeur de copie : chaque sous-classe l'appelle via super(other). La copie repart a pv pleins.
    protected Fighter(Fighter other) {
        this(other.name, other.maxHp, other.attack, other.speed);
    }

    // Chaque classe concrete calcule ses degats a sa facon.
    protected abstract int damageTo(Fighter target);

    // Type de retour COVARIANT : les sous-classes redefinissent avec leur propre type (Warrior copy()...).
    public abstract Fighter copy();

    // Rend les degats reellement encaisses. Tank la redefinit pour appliquer son armure.
    public int takeDamage(int damage) {
        int before = hp;
        hp = Math.max(0, hp - damage);
        return before - hp;
    }

    public int heal(int amount) {
        int before = hp;
        hp = Math.min(maxHp, hp + amount);
        return hp - before;
    }

    // final : la regle "vivant = pv > 0" ne doit pas changer selon la classe.
    public final boolean isAlive() {
        return hp > 0;
    }

    // Le tour par defaut : frapper l'ennemi vivant le plus faible. Healer le redefinit.
    public String act(Fighter[] allies, Fighter[] enemies) {
        Fighter target = weakest(enemies);
        int dealt = target.takeDamage(damageTo(target));
        return name + " -> " + target.name + " : " + dealt + " (" + target.status() + ")";
    }

    // L'allie ou l'ennemi vivant qui a le moins de pv (le premier en cas d'egalite).
    static Fighter weakest(Fighter[] team) {
        Fighter best = null;
        for (Fighter f : team) {
            if (f.isAlive() && (best == null || f.hp < best.hp)) {
                best = f;
            }
        }
        return best;
    }

    public String status() {
        return name + " " + hp + "/" + maxHp;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + status();
    }
}
