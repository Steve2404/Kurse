package d.mid;

import d.base.Greeter;

/** SOLUTION - une methode publique qui expose un type de d.base. */
public final class Polite {
    private Polite() {
    }

    public static String wrap(Greeter g, String name) {
        return "[" + g.greet(name) + "]";
    }
}
