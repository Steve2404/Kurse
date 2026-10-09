package ch18_design.drills.r05_commands.solution;

/** Remettre a zero : pas d'inverse calculable, on retient l'ancienne valeur (un memento) a chaque apply. */
public final class Reset implements Action {

    private final Counter counter;
    private int before;

    public Reset(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void apply() {
        before = counter.value();
        counter.set(0);
    }

    @Override
    public void revert() {
        counter.set(before);
    }
}
