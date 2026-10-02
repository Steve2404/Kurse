package ch7_beyondclasses.projects.p03_turtle.solution;

/**
 * SOLUTION - non-sealed : la hierarchie scellee se ROUVRE ici ; n'importe quelle classe peut etendre Macro.
 * Une macro se deplie en commandes de base.
 */
public non-sealed abstract class Macro implements Command {

    private final String name;

    protected Macro(String name) {
        this.name = name;
    }

    public abstract Command[] expand();

    public String name() {
        return name;
    }

    @Override
    public int size() {
        int total = 0;
        for (Command c : expand()) {
            total += c.size();
        }
        return total;
    }
}
