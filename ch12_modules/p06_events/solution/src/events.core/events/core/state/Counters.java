package events.core.state;

/**
 * SOLUTION - un etat interne PRIVE, lu seulement par l'audit grace a "opens events.core.state to events.audit".
 */
public class Counters {

    private int sent;
    private int missing;

    public void sent() {
        sent++;
    }

    public void missing() {
        missing++;
    }
}
