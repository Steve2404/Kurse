package ch8_lambdas.projects.p01_pipeline.solution;

/**
 * SOLUTION - memoisation sans collection : un petit cache dans deux tableaux, et un compteur de succes.
 */
public class Memo {

    private final String[] keys;
    private final String[] values;
    private int size;
    private int hits;
    private int calls;

    public Memo(int capacity) {
        keys = new String[capacity];
        values = new String[capacity];
    }

    // Rend une Step qui consulte le cache avant d'appeler f. La lambda capture this (le Memo) et f.
    public Step wrap(Step f) {
        return s -> {
            calls++;                       // modifier un CHAMP depuis une lambda est permis
            for (int i = 0; i < size; i++) {
                if (keys[i].equals(s)) {
                    hits++;
                    return values[i];
                }
            }
            String result = f.apply(s);
            if (size < keys.length) {
                keys[size] = s;
                values[size++] = result;
            }
            return result;
        };
    }

    public String stats() {
        return calls + " appels, " + hits + " depuis le cache";
    }
}
