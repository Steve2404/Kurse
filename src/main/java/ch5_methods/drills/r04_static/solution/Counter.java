package ch5_methods.drills.r04_static.solution;

/**
 * SOLUTION - une classe dont l'initialisation laisse des traces.
 */
public class Counter {

    // Initialiseurs static : dans l'ordre du fichier, une seule fois, au premier usage de la classe.
    static int base = trace("champ static base", 10);
    static final int STEP;

    static {
        STEP = base / 5;   // un static final se fixe une seule fois, ici
        trace("bloc static", STEP);
    }

    static int created;
    final int id;          // final d'instance : fixe dans le bloc d'instance
    int value = trace("champ d'instance value", base);

    {
        id = ++created;
        trace("bloc d'instance id", id);
    }

    static int trace(String what, int v) {
        System.out.println("D03 : " + what + " = " + v);
        return v;
    }

    int next() {
        value += STEP;     // une methode d'instance lit librement le static
        return value;
    }

    static int total() {
        return created;    // une methode static ne voit pas value ni id (pas de this)
    }
}
