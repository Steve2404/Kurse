package ch18_design.drills.r05_commands.solution;

import java.util.ArrayList;
import java.util.List;

/** Le sujet : une valeur entiere ; chaque changement previent les abonnes (sur une copie de la liste). */
public final class Counter {

    private int value;
    private final List<CounterListener> listeners = new ArrayList<>();

    public int value() {
        return value;
    }

    public void set(int newValue) {
        int old = value;
        value = newValue;
        List.copyOf(listeners).forEach(listener -> listener.changed(old, newValue));
    }

    public void addListener(CounterListener listener) {
        listeners.add(listener);
    }

    public void removeListener(CounterListener listener) {
        listeners.remove(listener);
    }
}
