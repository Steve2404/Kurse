package ch6_classdesign.projects.p02_tracer.solution;

/**
 * SOLUTION - un journal numerote : chaque etape de l'initialisation y laisse une ligne.
 */
public class Tracer {

    private static int step;

    // Rend v pour pouvoir s'utiliser dans l'initialisation d'un champ : int x = Tracer.log("...", 4);
    public static int log(String what, int v) {
        log(what + " = " + v);
        return v;
    }

    public static void log(String what) {
        step++;
        System.out.println((step < 10 ? "0" : "") + step + " " + what);
    }
}
