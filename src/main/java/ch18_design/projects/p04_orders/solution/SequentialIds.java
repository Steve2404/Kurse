package ch18_design.projects.p04_orders.solution;

/** Un adaptateur : CMD-1, CMD-2, CMD-3... previsible, donc testable (et sans doublon, contrairement au hasard). */
public final class SequentialIds implements IdGenerator {

    private final String prefix;
    private int counter;

    public SequentialIds(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String next() {
        counter++;
        return prefix + counter;
    }
}
