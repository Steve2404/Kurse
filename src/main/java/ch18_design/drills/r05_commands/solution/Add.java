package ch18_design.drills.r05_commands.solution;

/** Ajouter : l'inverse est de retirer la meme quantite. */
public final class Add implements Action {

    private final Counter counter;
    private final int amount;

    public Add(Counter counter, int amount) {
        this.counter = counter;
        this.amount = amount;
    }

    @Override
    public void apply() {
        counter.set(counter.value() + amount);
    }

    @Override
    public void revert() {
        counter.set(counter.value() - amount);
    }
}
